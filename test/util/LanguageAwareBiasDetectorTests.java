package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LanguageAwareBiasDetectorTests {
    @Test
    public void TestEnglishBiasDetectionHit() {
        var tokens = new TextTokens("she is vain", new String[] { "she", "is", "vain" });
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestEnglishBiasDetectionHitWithCapitalization() {
        var tokens = new TextTokens("She is vain", new String[] { "She", "is", "vain" });
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestEnglishBiasDetectionMiss() {
        var tokens = new TextTokens("he is good", new String[] { "he", "is", "good" });
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionHit() {
        var tokens = new TextTokens("ella es sentimental", new String[] { "ella", "es", "sentimental" });
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionHitWithCapitalization() {
        var tokens = new TextTokens("Ella es sentimental", new String[] { "Ella", "es", "sentimental" });
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionMiss() {
        var tokens = new TextTokens("el es apasionada", new String[] { "el", "es", "apasionada" });
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestBiasDetectionEnglishLocale() {
        var tokens = new TextTokens("she is vain", new String[] { "she", "is", "vain" });
        assertEquals(BiasCorrectLocale.ENGLISH, CreateDetector().getBiasDetectedLocale(tokens));
    }

    @Test
    public void TestBiasDetectionSpanishLocale() {
        var tokens = new TextTokens("Ella es sentimental", new String[] { "Ella", "es", "sentimental" });
        assertEquals(BiasCorrectLocale.SPANISH, CreateDetector().getBiasDetectedLocale(tokens));
    }

    private static LanguageAwareBiasDetector CreateDetector() {
        return new LanguageAwareBiasDetector(
                new EnglishFemalePronounBiasDetector(),
                new SpanishFemalePronounBiasDetector());
    }
}
