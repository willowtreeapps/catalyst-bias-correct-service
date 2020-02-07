package util;

import org.javatuples.Pair;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Utility {
    public static Map<String, Set<String>> collectIntoMapOfSuggestionsByTriggerWord(List<Correction> corrections) {
        return corrections.stream()
                .collect( groupingBy( Correction::getTrigger, mapping(Correction::getSuggestion, toSet()) ) );
    }

    public static Optional<Pair<Integer, Integer>> findMatch(String needle, TextTokens haystack, TextTokenizer tokenizer, Locale locale) {
        Optional<Pair<Integer, Integer>> result = Optional.empty();

        if (needle == null || haystack == null || haystack.getTokens() == null || tokenizer == null) {
            return result;
        }

        var needleTokens = tokenizer.tokenize(needle).getTokens();
        var haystackTokens = haystack.getTokens();

        if (null == needleTokens || needleTokens.length == 0) {
            return result;
        }

        var firstNeedle = needleTokens[0];
        int maxCount = haystackTokens.length >= needleTokens.length ? haystackTokens.length : 0;
        var foundPrefix = false;

        for (var position = 0; position < maxCount && !foundPrefix; position++) {
            var haystackValue = haystackTokens[position];

            if (!RegexMatcher.matchesWithAnyPrefixSuffix(firstNeedle, haystackValue)) {
                continue;
            }

            int haystackOffset = position;
            foundPrefix = IntStream
                    .range(0, needleTokens.length)
                    .allMatch( index -> matchesPattern(index, needleTokens.length, needleTokens[index], haystackTokens[haystackOffset + index]));

            result = foundPrefix ? Optional.of(Pair.with(position, position + needleTokens.length - 1)) : Optional.empty();
        }

        return result;
    }

    private static boolean matchesPattern(int index, int tokenLength, String needle, String haystack) {

        if (tokenLength > 1 && index == 0) {
            return RegexMatcher.matchesWithAnyPrefix(needle, haystack);
        } else if (tokenLength > 1 && index == tokenLength - 1) {
            return RegexMatcher.matchesWithAnySuffix(needle, haystack);
        }

        return RegexMatcher.matchesWithAnyPrefixSuffix(needle, haystack);
    }

    private Utility() {}
}
