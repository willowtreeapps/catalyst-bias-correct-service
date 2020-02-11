package util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UtilityTests {

    @Test
    public void TestCorrectionCollection() {
        var corrections = List.of(
                new Correction("trigger1", "suggestion1"),
                new Correction("trigger2", "suggestion2"),
                new Correction("trigger1", "suggestion3")
        );

        var mapping = Utility.collectIntoMapOfSuggestionsByTriggerWord(corrections);
        assertNotNull(mapping);

        assertNotNull(mapping.get("trigger1"));
        assertNotNull(mapping.get("trigger2"));

        var suggestions = mapping.get("trigger1");
        assertTrue(suggestions.contains("suggestion1"));
        assertTrue(suggestions.contains("suggestion3"));
        assertEquals(2, suggestions.size());

        var secondSuggestions = mapping.get("trigger2");
        assertEquals(1, secondSuggestions.size());
        assertEquals(secondSuggestions.iterator().next(), "suggestion2");
    }

    @Test
    public void TestMultiWordMatching() {
        var tokens = new TextTokens("a b c d", new String[] { "a", "b", "c", "d", });
        var match = "b c";
        var result = Utility.findMatches(match, tokens, TestUtility.createTextTokenizer());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStartIndex());
        assertEquals(2, result.get(0).getEndIndex());
    }

    @Test
    public void TestRepeatedMultiWordMatching() {
        var tokens = new TextTokens("a b c d b c", new String[] { "a", "b", "c", "d", "b", "c"});
        var match = "b c";
        var result = Utility.findMatches(match, tokens, TestUtility.createTextTokenizer());
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getStartIndex());
        assertEquals(2, result.get(0).getEndIndex());
        assertEquals(4, result.get(1).getStartIndex());
        assertEquals(5, result.get(1).getEndIndex());
    }

    @Test
    public void TestMultiWordMisMatching() {
        var tokens = new TextTokens("a b c d", new String[] { "a", "b", "c", "d", });
        var match = "b d";
        var result = Utility.findMatches(match, tokens, TestUtility.createTextTokenizer());
        assertTrue(result.isEmpty());
    }
}
