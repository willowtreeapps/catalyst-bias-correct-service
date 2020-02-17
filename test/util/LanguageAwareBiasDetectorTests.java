package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static util.TestUtility.createTextTokenizer;

public class LanguageAwareBiasDetectorTests {
    private BiasDetector biasDetector;
    private TextTokenizer textTokenizer;

    @Before
    public void setup() {
        biasDetector = createDetector();
        textTokenizer = createTextTokenizer();
    }

    @After
    public void tearDown() {
        biasDetector = null;
        textTokenizer = null;
    } 
        
    @Test
    public void TestEnglishBiasDetectionHit() {
        var tokens = getEnglishTokens(false);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestEnglishBiasDetectionHitWithCapitalization() {
        var tokens = getEnglishTokens(true);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestEnglishBiasDetectionMiss() {
        var tokens = new TextTokens("he is good", new String[] { "he", "is", "good" });
        assertFalse(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestSpanishBiasDetectionHit() {
        var tokens = getSpanishTokens(false);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestSpanishBiasDetectionHitWithCapitalization() {
        var tokens = getSpanishTokens(true);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestSpanishBiasDetectionMiss() {
        var tokens = new TextTokens("el es apasionada", new String[] { "el", "es", "apasionada" });
        assertFalse(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestFrenchBiasDetectionHit() {
        var tokens = getFrenchTokens(false);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestFrenchBiasDetectionHitWithCapitalization() {
        var tokens =  getFrenchTokens(true);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestFrenchBiasDetectionMiss() {
        var tokens = new TextTokens("il est bon", new String[] { "il", "est", "bon" });
        assertFalse(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestGermanBiasDetectionHit() {
        var tokens = getGermanTokens(false);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestGermanBiasDetectionHitWithCapitalization() {
        var tokens = getGermanTokens(true);
        assertTrue(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestGermanBiasDetectionMiss() {
        var tokens = new TextTokens("er ist gut", new String[] { "er", "ist", "gut" });
        assertFalse(biasDetector.isBiasDetected(tokens, textTokenizer));
    }

    @Test
    public void TestBiasDetectionEnglishLocale() {
        var tokens = getEnglishTokens(false);
        assertEquals(BiasCorrectLocale.ENGLISH, biasDetector.getBiasDetectedLocale(tokens, textTokenizer));
    }

    @Test
    public void TestBiasDetectionSpanishLocale() {
        var tokens = getSpanishTokens(false);
        assertEquals(BiasCorrectLocale.SPANISH, biasDetector.getBiasDetectedLocale(tokens, textTokenizer));
    }

    @Test
    public void TestBiasDetectionFrenchLocale() {
        var tokens = getFrenchTokens(false);
        assertEquals(BiasCorrectLocale.FRENCH, biasDetector.getBiasDetectedLocale(tokens, textTokenizer));
    }

    @Test
    public void TestBiasDetectionGermanLocale() {
        var tokens = getGermanTokens(false);
        assertEquals(BiasCorrectLocale.GERMAN, biasDetector.getBiasDetectedLocale(tokens, textTokenizer));
    }

    private static LanguageAwareBiasDetector createDetector() {
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
        var pronoun = capitalized ? "Sie ist" : "sie ist";
        return new TextTokens(String.format("%s theatralisch", pronoun), new String[] { capitalized ? "Sie":"sie", "ist", "theatralisch" });
    }
}
