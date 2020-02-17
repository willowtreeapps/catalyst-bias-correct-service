package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class GermanFemalePronounBiasDetector extends BiasDetector {
    private static final Set<String> PRONOUNS = new HashSet(Arrays.asList(
            "ihr", "ihre", "sie ist", "sie wird", "sie hat", "sie hatte", "sie war"));

    @Override
    Locale getLocale() {
        return BiasCorrectLocale.GERMAN;
    }

    @Override
    Set<String> getPronouns() {
        return PRONOUNS;
    }
}
