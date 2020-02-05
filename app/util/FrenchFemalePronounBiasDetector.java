package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

public class FrenchFemalePronounBiasDetector implements BiasDetector {
    @Override
    public boolean isBiasDetected(TextTokens input) {
        var lowercaseTokens = Arrays.stream(input.getTokens())
                .map(token -> token.toLowerCase(BiasCorrectLocale.FRENCH))
                .collect(Collectors.toList());
        var words = new HashSet(lowercaseTokens);
        return words.contains("elle")
                || words.contains("son")
                || words.contains("sienne");
    }

    @Override
    public Locale getBiasDetectedLocale(TextTokens input) {
        return isBiasDetected(input) ? BiasCorrectLocale.FRENCH : null;
    }

}
