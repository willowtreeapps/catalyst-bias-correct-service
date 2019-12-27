package util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LanguageDetectionTests {
    @Test
    public void TestEnglishDetection() throws IOException {
        var statement = "this is the english text detection test";
        var detector = new TikaLanguageDetector();
        var result = detector.detect(statement);
        assertNotNull(result);
        assertEquals(LanguageDetectionConfidence.HIGH, result.getConfidence());
        assertEquals("en", result.getLanguage());
    }

    @Test
    public void TestSpanishDetection() throws IOException {
        var statement = "esta es la prueba de detección de texto en español";
        var detector = new TikaLanguageDetector();
        var result = detector.detect(statement);
        assertNotNull(result);
        assertEquals(LanguageDetectionConfidence.HIGH, result.getConfidence());
        assertEquals("es", result.getLanguage());
    }
}
