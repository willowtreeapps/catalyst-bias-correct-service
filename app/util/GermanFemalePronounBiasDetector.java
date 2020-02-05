package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

public class GermanFemalePronounBiasDetector implements BiasDetector {
    @Override
    public boolean isBiasDetected(TextTokens input) {
        var lowercaseTokens = Arrays.stream(input.getTokens())
                .map(token -> token.toLowerCase(BiasCorrectLocale.GERMAN))
                .collect(Collectors.toList());
        var words = new HashSet(lowercaseTokens);
        return words.contains("ihre")
                || words.contains("ihrer")
                || words.contains("ihres");
    }

    @Override
    public Locale getBiasDetectedLocale(TextTokens input) {
        return isBiasDetected(input) ? BiasCorrectLocale.GERMAN : null;
    }

}
