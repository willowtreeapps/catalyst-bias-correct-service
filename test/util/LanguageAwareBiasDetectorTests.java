package util;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LanguageAwareBiasDetectorTests {
    @Test
    public void TestEnglishBiasDetectionHit() {
        var tokens = new TextTokens("she is vain", new String[] { "she", "is", "vain" }, Locale.ENGLISH);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestEnglishBiasDetectionMiss() {
        var tokens = new TextTokens("he is good", new String[] { "he", "is", "good" }, Locale.ENGLISH);
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionHit() {
        var tokens = new TextTokens("ella es sentimental", new String[] { "ella", "es", "sentimental" }, new Locale("es"));
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionMiss() {
        var tokens = new TextTokens("el es apasionada", new String[] { "el", "es", "apasionada" }, new Locale("es"));
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestBiasDetectionWithoutLocale() {
        var tokens = new TextTokens("she is good", new String[] { "she", "is", "good" }, null);
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestBiasDetectionWithMismatchedLocale() {
        var tokens = new TextTokens("she is good", new String[] { "she", "is", "good" }, new Locale("es"));
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestBiasDetectionWithFullySpecifiedLocale() {
        var tokens = new TextTokens("she is vain", new String[] { "she", "is", "vain" }, new Locale("en", "US"));
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    private static LanguageAwareBiasDetector CreateDetector() {
        return new LanguageAwareBiasDetector(
                new EnglishFemalePronounBiasDetector(),
                new SpanishFemalePronounBiasDetector());
    }
}
