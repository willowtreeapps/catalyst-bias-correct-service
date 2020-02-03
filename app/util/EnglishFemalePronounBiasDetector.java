package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

public class EnglishFemalePronounBiasDetector implements BiasDetector {
    @Override
    public boolean isBiasDetected(TextTokens input) {
        var lowercaseTokens = Arrays.stream(input.getTokens())
                .map(token -> token.toLowerCase(BiasCorrectLocale.ENGLISH))
                .collect(Collectors.toList());
        var words = new HashSet(lowercaseTokens);
        return words.contains("she")
                || words.contains("her")
                || words.contains("she's")
                || words.contains("she'll")
                || words.contains("her's")
                || words.contains("hers");
    }

    @Override
    public Locale getBiasDetectedLocale(TextTokens input) {
        return isBiasDetected(input) ? BiasCorrectLocale.ENGLISH : null;
    }
}
