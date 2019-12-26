package util;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MapBackedBiasCorrector implements BiasCorrector {
    public MapBackedBiasCorrector(Map<Locale, Map<String, Set<String>>> correctionsByLocale, Randomizer randomizer) {
        _correctionsByLocale = correctionsByLocale;
        _randomizer = randomizer;
    }

    @Override
    public String correct(String input, Locale locale) {
        var corrections = _correctionsByLocale.get(locale);
        if (corrections == null) {
            return null;
        }

        var suggestions = corrections.get(input);
        if (suggestions == null) {
            return null;
        }

        var randomSuggestionIndex = _randomizer.next() % suggestions.size();
        return suggestions.stream()
            .skip(randomSuggestionIndex)
            .findFirst()
            .get();
    }

    private Map<Locale, Map<String, Set<String>>> _correctionsByLocale;
    private Randomizer _randomizer;
}
