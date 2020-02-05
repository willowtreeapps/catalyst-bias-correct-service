package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LanguageAwareBiasDetectorTests {
    @Test
    public void TestEnglishBiasDetectionHit() {
        var tokens = getEnglishTokens(false);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestEnglishBiasDetectionHitWithCapitalization() {
        var tokens = getEnglishTokens(true);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestEnglishBiasDetectionMiss() {
        var tokens = new TextTokens("he is good", new String[] { "he", "is", "good" });
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionHit() {
        var tokens = getSpanishTokens(false);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionHitWithCapitalization() {
        var tokens = getSpanishTokens(true);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestSpanishBiasDetectionMiss() {
        var tokens = new TextTokens("el es apasionada", new String[] { "el", "es", "apasionada" });
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestFrenchBiasDetectionHit() {
        var tokens = getFrenchTokens(false);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestFrenchBiasDetectionHitWithCapitalization() {
        var tokens =  getFrenchTokens(true);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestFrenchBiasDetectionMiss() {
        var tokens = new TextTokens("il est bon", new String[] { "il", "est", "bon" });
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestGermanBiasDetectionHit() {
        var tokens = getGermanTokens(false);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestGermanBiasDetectionHitWithCapitalization() {
        var tokens = getGermanTokens(true);
        assertTrue(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestGermanBiasDetectionMiss() {
        var tokens = new TextTokens("er ist gut", new String[] { "er", "ist", "gut" });
        assertFalse(CreateDetector().isBiasDetected(tokens));
    }

    @Test
    public void TestBiasDetectionEnglishLocale() {
        var tokens = getEnglishTokens(false);
        assertEquals(BiasCorrectLocale.ENGLISH, CreateDetector().getBiasDetectedLocale(tokens));
    }

    @Test
    public void TestBiasDetectionSpanishLocale() {
        var tokens = getSpanishTokens(false);
        assertEquals(BiasCorrectLocale.SPANISH, CreateDetector().getBiasDetectedLocale(tokens));
    }

    @Test
    public void TestBiasDetectionFrenchLocale() {
        var tokens = getFrenchTokens(false);
        assertEquals(BiasCorrectLocale.FRENCH, CreateDetector().getBiasDetectedLocale(tokens));
    }

    @Test
    public void TestBiasDetectionGermanLocale() {
        var tokens = getGermanTokens(false);
        assertEquals(BiasCorrectLocale.GERMAN, CreateDetector().getBiasDetectedLocale(tokens));
    }

    private static LanguageAwareBiasDetector CreateDetector() {
        return new LanguageAwareBiasDetector(
                new EnglishFemalePronounBiasDetector(),
                new SpanishFemalePronounBiasDetector(),
                new FrenchFemalePronounBiasDetector(),
                new GermanFemalePronounBiasDetector());
    }

    private static TextTokens getEnglishTokens(boolean capitalized) {
        var pronoun = capitalized ? "She" : "she";
        return new TextTokens(String.format("%s is vain", pronoun), new String[] { pronoun, "is", "vain" });
    }

    private static TextTokens getSpanishTokens(boolean capitalized) {
        var pronoun = capitalized ? "Ella" : "ella";
        return new TextTokens(String.format("%s es sentimental", pronoun), new String[] { pronoun, "es", "sentimental" });
    }

    private static TextTokens getFrenchTokens(boolean capitalized) {
        var pronoun = capitalized ? "Elle" : "elle";
         return new TextTokens(String.format("%s émotive", pronoun), new String[] { pronoun, "émotive" });
    }

    private static TextTokens getGermanTokens(boolean capitalized) {
        var pronoun = capitalized ? "Ihre" : "ihre";
        return new TextTokens(String.format("%s theatralisch", pronoun), new String[] { pronoun, "theatralisch" });
    }
}
