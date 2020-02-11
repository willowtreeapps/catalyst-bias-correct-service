package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Utility {
    public static Map<String, Set<String>> collectIntoMapOfSuggestionsByTriggerWord(List<Correction> corrections) {
        return corrections.stream()
                .collect( groupingBy( Correction::getTrigger, mapping(Correction::getSuggestion, toSet()) ) );
    }

    public static List<Match> findMatches(String needle, TextTokens haystack, TextTokenizer tokenizer) {
        List<Match> result = new ArrayList<>();

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

        for (var position = 0; position < maxCount; position++) {
            var haystackValue = haystackTokens[position];

            if (!RegexMatcher.matchesWithAnyPrefixSuffix(firstNeedle, haystackValue)) {
                continue;
            }

            int haystackOffset = position;
            var foundPrefix = IntStream
                    .range(0, needleTokens.length)
                    .allMatch( index -> matchesPattern(index, needleTokens.length, needleTokens[index], haystackTokens[haystackOffset + index]));

            if (foundPrefix) {
                result.add( new Match
                        .MatchBuilder()
                        .setTrigger(needle)
                        .setStartIndex(position)
                        .setEndIndex(position + needleTokens.length - 1)
                        .build() );
            }
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
