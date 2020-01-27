package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

public class SpanishFemalePronounBiasDetector implements BiasDetector {
    @Override
    public boolean isBiasDetected(TextTokens input) {
        var lowercaseTokens = Arrays.stream(input.getTokens())
                .map(token -> token.toLowerCase(SpanishLocale))
                .collect(Collectors.toList());
        var words = new HashSet(lowercaseTokens);
        return words.contains("ella");
    }

    private static Locale SpanishLocale = new Locale("es");
}
