package util;

import opennlp.tools.tokenize.DetokenizationDictionary;
import opennlp.tools.tokenize.DictionaryDetokenizer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.junit.Test;
import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static util.TestUtility.createTextTokenizer;

public class MapBackedBiasCorrectorTests {
    @Test
    public void TestSimpleFetch() {
        var locale = Locale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var biasDetector = new TestBiasDetector();
        biasDetector.setBiasDetected(true);
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, biasDetector, createTextTokenizer());

        var suggestion = corrector.correct("trigger", locale);
        assertEquals("suggestion", suggestion);
    }

    @Test
    public void TestSuggestionMiss() {
        var locale = Locale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(), createTextTokenizer());

        var suggestion = corrector.correct("miss", locale);
        assertNull(suggestion);
    }

    @Test
    public void TestSuggestionRandomization() {
        var locale = Locale.ENGLISH;
        var suggestions = Set.of("suggestion", "suggestion-two");
        var corrections = Map.of(
                "trigger", suggestions
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var biasDetector = new TestBiasDetector();
        biasDetector.setBiasDetected(true);
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, biasDetector, createTextTokenizer());

        var suggestion = corrector.correct("trigger", locale);
        assertTrue(suggestions.contains(suggestion));

        var secondSuggestion = corrector.correct("trigger", locale);
        assertTrue(suggestions.contains(secondSuggestion) && secondSuggestion != suggestion);
    }

    @Test
    public void TestLocaleMiss() {
        var locale = Locale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(), createTextTokenizer());

        var suggestion = corrector.correct("trigger", Locale.JAPANESE);
        assertNull(suggestion);
    }

    @Test
    public void TestRequestingAMoreSpecificLocale() {
        var locale = Locale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var biasDetector = new TestBiasDetector();
        biasDetector.setBiasDetected(true);
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, biasDetector, createTextTokenizer());

        var suggestion = corrector.correct("trigger", Locale.US);
        assertNotNull(suggestion);
    }

    @Test
    public void TestRequestingALessSpecificLocale() {
        var locale = Locale.US;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(), createTextTokenizer());

        var suggestion = corrector.correct("trigger", Locale.ENGLISH);
        assertNull(suggestion);
    }
}
