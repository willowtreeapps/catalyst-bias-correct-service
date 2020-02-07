package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class EnglishFemalePronounBiasDetector extends BiasDetector {
    private static final Set<String> PRONOUNS = new HashSet(Arrays.asList("she", "her", "she's", "she'll", "her's", "hers"));

    @Override
    Locale getLocale() {
        return BiasCorrectLocale.ENGLISH;
    }

    @Override
    Set<String> getPronouns() {
        return PRONOUNS;
    }
}