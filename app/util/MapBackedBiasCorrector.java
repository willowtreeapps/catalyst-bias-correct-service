package util;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

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
    public String correct(String input, Locale locale) {
        var tokens = _tokenizer.tokenize(input, locale);
        if (! _detector.isBiasDetected(tokens)) {
            return null;
        }

        var corrections = getCorrectionsByLocaleOrLessSpecificVariant(locale);
        if (corrections == null) {
            return null;
        }

        var matches = findTriggerWordsForCorrection(locale, tokens, corrections, _tokenizer);
        var textTokens = new ArrayList(Arrays.asList(tokens.getTokens()));

        matches.forEach( match -> replaceTriggerWords(match, textTokens, corrections, _randomizer));

        var textTokenArray = (String[]) textTokens.toArray(new String[0]);
        return _tokenizer.detokenize(new TextTokens(input, textTokenArray), locale);
    }

    private static void replaceTriggerWords(Pair<String, Optional<Pair<Integer, Integer>>> match, ArrayList textTokens, Map<String, Set<String>> corrections, Randomizer _randomizer) {
        var trigger = match.getValue0();
        var indexes = match.getValue1().get();
        var suggestions = corrections.get(trigger);
        var suggestion = suggestions.stream()
                .skip(_randomizer.next() % suggestions.size())
                .findFirst()
                .get();
        replace(suggestion, indexes.getValue0(), indexes.getValue1(), textTokens);
    }

    @NotNull
    private static Stream<Pair<String, Optional<Pair<Integer, Integer>>>> findTriggerWordsForCorrection(Locale locale, TextTokens tokens, Map<String, Set<String>> corrections, TextTokenizer _tokenizer) {
        return corrections.keySet()
                    .stream()
                    .map( trigger -> Pair.with(trigger, Utility.findMatch(trigger, tokens, _tokenizer, locale)) )
                    .filter( pairOfTriggerAndMatch -> pairOfTriggerAndMatch.getValue1().isPresent() );
    }

    private static void replace(String replacement, int startIndex, int endIndex, ArrayList<String> tokens) {
        tokens.subList(startIndex, endIndex + 1).clear();
        tokens.add(startIndex, replacement);
    }

    private Map<String, Set<String>> getCorrectionsByLocaleOrLessSpecificVariant(Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
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
