package util;

import com.google.inject.Inject;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LanguageAwareBiasDetector extends BiasDetector {
    private final Map<Locale, BiasDetector> m_detectorMap;

    @Inject
    public LanguageAwareBiasDetector(EnglishFemalePronounBiasDetector englishPronounDetector,
                                     SpanishFemalePronounBiasDetector spanishPronounDetector,
                                     FrenchFemalePronounBiasDetector frenchPronounDetector,
                                     GermanFemalePronounBiasDetector germanPronounDetector) {
        m_detectorMap = Map.of(
                BiasCorrectLocale.ENGLISH, englishPronounDetector,
                BiasCorrectLocale.SPANISH, spanishPronounDetector,
                BiasCorrectLocale.FRENCH, frenchPronounDetector,
                BiasCorrectLocale.GERMAN, germanPronounDetector
        );
    }

    @Override
    public boolean isBiasDetected(TextTokens input) {
        return getBiasDetectedLocale(input) != null;
    }

    @Override
    public Locale getBiasDetectedLocale(TextTokens input) {
        // if input matches any pronouns, return the first locale found
        var locale = m_detectorMap
                .entrySet()
                .parallelStream()
                .filter( x -> x.getValue().isBiasDetected(input))
                .findFirst();
        return locale.isEmpty() ? null : locale.get().getKey();
    }

    @Override
    Locale getLocale() {
        return BiasCorrectLocale.ENGLISH;
    }

    @Override
    Set<String> getPronouns() {
        return new HashSet<>();
    }
}
