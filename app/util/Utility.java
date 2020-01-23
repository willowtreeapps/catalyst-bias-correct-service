package util;

import org.javatuples.Pair;

import java.text.Collator;
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
        int maxCount = haystackTokens.length > needleTokens.length ? haystackTokens.length : 0;
        var foundPrefix = false;

        var collator = Collator.getInstance(locale);
        collator.setStrength(Collator.FULL_DECOMPOSITION);

        for (var position = 0; position < maxCount && !foundPrefix; position++) {
            var haystackValue = haystackTokens[position];

            if (0 != collator.compare(firstNeedle, haystackValue)) {
                continue;
            }

            int haystackOffset = position;
            foundPrefix = IntStream
                    .range(0, needleTokens.length)
                    .allMatch( index -> 0 == collator.compare(needleTokens[index], haystackTokens[haystackOffset + index]));

            result = foundPrefix ? Optional.of(Pair.with(position, position + needleTokens.length - 1)) : Optional.empty();
        }


        return result;
    }

    private Utility() {}
}
