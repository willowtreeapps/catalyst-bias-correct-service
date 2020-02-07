package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public abstract class BiasDetector {
    public boolean isBiasDetected(TextTokens input) {
        var words = new HashSet(Arrays.asList(input.getTokens()));
        return RegexMatcher.pronounMatchFound(getPronouns(), words);
    }

    public Locale getBiasDetectedLocale(TextTokens input) {
        return isBiasDetected(input) ? getLocale() : null;
    }

    abstract Locale getLocale();
    abstract Set<String> getPronouns();
}
