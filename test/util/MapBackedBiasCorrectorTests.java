package util;

import org.junit.Test;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static util.TestUtility.createTextTokenizer;

public class MapBackedBiasCorrectorTests {
    @Test
    public void TestSimpleFetch() {
        var locale = BiasCorrectLocale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion")
        );
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(true), createTextTokenizer());

        var suggestion = corrector.correct("trigger");
        assertEquals("suggestion", suggestion);
    }

    @Test
    public void TestSuggestionMiss() {
        var locale = BiasCorrectLocale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion")
        );
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(), createTextTokenizer());

        var suggestion = corrector.correct("miss");
        assertNull(suggestion);
    }

    @Test
    public void TestSuggestionRandomization() {
        var locale = BiasCorrectLocale.ENGLISH;
        var suggestions = Set.of("suggestion", "suggestion-two");
        var corrections = Map.of(
                "trigger", suggestions
        );
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(true), createTextTokenizer());

        var suggestion = corrector.correct("trigger");
        assertTrue(suggestions.contains(suggestion));

        var secondSuggestion = corrector.correct("trigger");
        assertTrue(suggestions.contains(secondSuggestion) && secondSuggestion != suggestion);
    }

    @Test
    public void TestLocaleMiss() {
        var locale = BiasCorrectLocale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(), createTextTokenizer());

        var suggestion = corrector.correct("trigger");
        assertNull(suggestion);
    }

    @Test
    public void TestRequestingAMoreSpecificLocale() {
        var locale = BiasCorrectLocale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(true), createTextTokenizer());

        var suggestion = corrector.correct("trigger");
        assertNotNull(suggestion);
    }

    @Test
    public void TestRequestingALessSpecificLocale() {
        var locale = Locale.US;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer, new TestBiasDetector(), createTextTokenizer());

        var suggestion = corrector.correct("trigger");
        assertNull(suggestion);
    }
}
