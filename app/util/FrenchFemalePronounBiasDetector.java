package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class FrenchFemalePronounBiasDetector extends BiasDetector {
    private static final Set<String> PRONOUNS = new HashSet(Arrays.asList("elle", "son", "sienne"));

    @Override
    Locale getLocale() {
        return BiasCorrectLocale.FRENCH;
    }

    @Override
    Set<String> getPronouns() {
        return PRONOUNS;
    }
}
