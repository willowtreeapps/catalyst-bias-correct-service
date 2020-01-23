package util;

import com.google.inject.Inject;

import java.util.Locale;
import java.util.Map;

public class LanguageAwareBiasDetector implements BiasDetector {
    private final Map<Locale, BiasDetector> m_detectorMap;

    @Inject
    public LanguageAwareBiasDetector(EnglishFemalePronounBiasDetector englishPronounDetector, SpanishFemalePronounBiasDetector spanishPronounDetector) {
        m_detectorMap = Map.of(
                Locale.ENGLISH, englishPronounDetector,
                new Locale("es"), spanishPronounDetector
        );
    }

    @Override
    public boolean isBiasDetected(TextTokens input) {
        var locale = input.getLocale();
        if (locale == null) {
            return false;
        }
        var detector = m_detectorMap.get(locale);
        if (detector == null && locale.getCountry() != null && !locale.getCountry().isEmpty()) {
            var languageLocale = new Locale(locale.getLanguage());
            detector = m_detectorMap.get(languageLocale);
        }
        return detector == null ? false : detector.isBiasDetected(input);
    }
}
