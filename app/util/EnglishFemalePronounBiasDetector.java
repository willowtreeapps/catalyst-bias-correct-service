package util;

import java.util.Arrays;
import java.util.HashSet;

public class EnglishFemalePronounBiasDetector implements BiasDetector {
    @Override
    public boolean isBiasDetected(TextTokens input) {
        var words = new HashSet(Arrays.asList(input.getTokens()));
        return words.contains("she")
                || words.contains("her")
                || words.contains("she's")
                || words.contains("she'll")
                || words.contains("her's")
                || words.contains("hers");
    }
}
