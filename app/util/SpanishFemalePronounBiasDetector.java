package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class SpanishFemalePronounBiasDetector extends BiasDetector {
    private static final Set<String> PRONOUNS = new HashSet(Arrays.asList("ella", "su"));

    @Override
    Locale getLocale() {
        return BiasCorrectLocale.SPANISH;
    }

    @Override
    Set<String> getPronouns() {
        return PRONOUNS;
    }
}