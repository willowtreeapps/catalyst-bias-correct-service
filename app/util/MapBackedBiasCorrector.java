package util;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

        var correctedTokens = Arrays.stream(tokens.getTokens())
                .map( token -> {
                    var suggestions = corrections.get(token);
                    if (suggestions == null) {
                        return token;
                    }

                    return suggestions.stream()
                            .skip(_randomizer.next() % suggestions.size())
                            .findFirst()
                            .get();
                } )
                .toArray(String[]::new);

        return _tokenizer.detokenize(new TextTokens(input, correctedTokens, locale));
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
