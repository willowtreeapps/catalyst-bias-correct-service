package util;

import org.junit.Test;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MapBackedBiasCorrectorTests {
    @Test
    public void TestSimpleFetch() {
        var locale = Locale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer);

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
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer);

        var suggestion = corrector.correct("miss", locale);
        assertNull(suggestion);
    }

    @Test
    public void TestSuggestionRandomization() {
        var locale = Locale.ENGLISH;
        var corrections = Map.of(
                "trigger", Set.of("suggestion", "suggestion-two")
        );
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var corrector = new MapBackedBiasCorrector(Map.of(locale, corrections), randomizer);

        var suggestion = corrector.correct("trigger", locale);
        assertEquals("suggestion", suggestion);

        var secondSuggestion = corrector.correct("trigger", locale);
        assertEquals("suggestion-two", secondSuggestion);
    }
}
