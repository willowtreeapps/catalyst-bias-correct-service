package util;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class MapBackedBiasCorrector implements BiasCorrector {
    public MapBackedBiasCorrector(Map<Locale, Map<String, Set<String>>> correctionsByLocale,
                                  Randomizer randomizer,
                                  BiasDetector detector,
                                  TextTokenizer tokenizer) {
        _correctionsByLocale = correctionsByLocale;
        _randomizer = randomizer;
        _detector = detector;
        _tokenizer = tokenizer;
    }

    @Override
    public String correct(String input) {
        var tokens = _tokenizer.tokenize(input);
        var locale = _detector.getBiasDetectedLocale(tokens, _tokenizer);
        if (locale == null) {
            return null;
        }

        var corrections = getCorrectionsByLocaleOrLessSpecificVariant(locale);
        if (corrections == null) {
            return null;
        }

        // This algorithm matches trigger phrases that have multiple terms by an inverse search.
        // Instead of iterating through the tokens once and seeing what is in a Set of words,
        // we iterate through all of our triggers and perform a linear search through the tokenized
        // input.  This results in a pessimal algorithm that requires multiple passes through the data.
        // We may consider an optimization in which we implement two algorithms.  One would be optimized
        // for the most common case of single-word phrases.  That could iterate through the entire input
        // once and perform a lookup on every word.  We would need to normalize the tokens and corrections
        // so that we can perform a proper language-aware lookup.  The second pass would just operate on
        // the multi-word triggers and would iterate through the string using this algorithm.
        var matches = findTriggerWordsForCorrection(tokens, corrections, _tokenizer);
        if (matches.isEmpty()) {
            return "";
        }

        var textTokens = new ArrayList(Arrays.asList(tokens.getTokens()));
        matches.forEach( match -> replaceTriggerWords(match, textTokens, corrections, _randomizer));
        var textTokenArray = (String[]) textTokens.toArray(new String[0]);
        return _tokenizer.detokenize(new TextTokens(input, textTokenArray));
    }

    private static void replaceTriggerWords(Match match, ArrayList textTokens, Map<String, Set<String>> corrections, Randomizer _randomizer) {
        var suggestions = corrections.get(match.getTrigger());
        var suggestion = suggestions.stream()
                .skip(_randomizer.next() % suggestions.size())
                .findFirst()
                .get();
        replace(suggestion, match.getStartIndex(), match.getEndIndex(), textTokens);
    }

    @NotNull
    private static List<Match> findTriggerWordsForCorrection(TextTokens tokens, Map<String, Set<String>> corrections, TextTokenizer _tokenizer) {
        return corrections.keySet()
                    .stream()
                    .flatMap( trigger -> Utility.findMatches(trigger, tokens, _tokenizer).stream())
                    .sorted((o1, o2) -> o2.getStartIndex() - o1.getStartIndex()) // reverse order to replace last token first
                    .collect(Collectors.toList());

    }

    private static void replace(String replacement, int startIndex, int endIndex, List<String> tokens) {
        var newValue = RegexMatcher.getReplacementWithPrefixSuffix(startIndex, endIndex, replacement, tokens);

        tokens.subList(startIndex, endIndex + 1).clear();
        tokens.add(startIndex, newValue);
    }

    private Map<String, Set<String>> getCorrectionsByLocaleOrLessSpecificVariant(Locale locale) {
        if (locale == null) {
            locale = BiasCorrectLocale.ENGLISH;
        }

        var corrections = _correctionsByLocale.get(locale);
        if (corrections != null) {
            return corrections;
        }

        var languageLocale = new Locale(locale.getLanguage());
        return _correctionsByLocale.get(languageLocale);
    }

    private Map<Locale, Map<String, Set<String>>> _correctionsByLocale;
    private Randomizer _randomizer;
    private BiasDetector _detector;
    private TextTokenizer _tokenizer;
}
