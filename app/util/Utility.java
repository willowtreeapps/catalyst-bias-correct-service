package util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class Utility {
    public static Map<String, Set<String>> collectIntoMapOfSuggestionsByTriggerWord(List<Correction> corrections) {
        return corrections.stream()
                .collect( groupingBy( Correction::getTrigger, mapping(Correction::getSuggestion, toSet()) ) );
    }

    private Utility() {}
}
