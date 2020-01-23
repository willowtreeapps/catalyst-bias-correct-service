package util;

import java.util.Arrays;
import java.util.HashSet;

public class SpanishFemalePronounBiasDetector implements BiasDetector {
    @Override
    public boolean isBiasDetected(TextTokens input) {
        var words = new HashSet(Arrays.asList(input.getTokens()));
        return words.contains("ella");
    }
}
