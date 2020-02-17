package util;

import java.util.List;
import java.util.regex.Pattern;

public class RegexMatcher {
    private static String ANY_LETTER = "[\\p{L}\\p{M}]+";
    private static String PUNCTUATION = "(\\p{P}*)";
    private static String CASE_INSENSITIVE_STRING = "((?i)%s)";
    private static String WITHIN_SPECIAL_CHARS = PUNCTUATION + CASE_INSENSITIVE_STRING + PUNCTUATION;
    private static String PREFIX_SPECIAL_CHARS = PUNCTUATION + CASE_INSENSITIVE_STRING;
    private static String SUFFIX_SPECIAL_CHARS = CASE_INSENSITIVE_STRING + PUNCTUATION;

    public static boolean matchesWithAnyPrefix(String patternPart, String word) {
        return matchesFormattedString(PREFIX_SPECIAL_CHARS, patternPart, word);
    }

    public static boolean matchesWithAnySuffix(String patternPart, String word) {
        return matchesFormattedString(SUFFIX_SPECIAL_CHARS, patternPart, word);
    }

    public static boolean matchesWithAnyPrefixSuffix(String patternPart, String word) {
        return matchesFormattedString(WITHIN_SPECIAL_CHARS, patternPart, word);
    }

    public static String getReplacementWithPrefixSuffix(int startIndex, int endIndex, String replacement, List<String> tokens) {
        StringBuilder sb = new StringBuilder(replacement);

        if (startIndex != endIndex) {
            var prefixPattern = Pattern.compile(String.format(PREFIX_SPECIAL_CHARS, ANY_LETTER))
                    .matcher(tokens.get(startIndex));
            var suffixPattern = Pattern.compile(String.format(SUFFIX_SPECIAL_CHARS, ANY_LETTER))
                    .matcher(tokens.get(endIndex));

            if (prefixPattern.matches()) {
                sb.insert(0, prefixPattern.group(1));
            }
            if (suffixPattern.matches()) {
                sb.append(suffixPattern.group(2));
            }
        } else {
            var pattern = Pattern.compile(String.format(WITHIN_SPECIAL_CHARS, ANY_LETTER))
                    .matcher(tokens.get(startIndex));
            if (pattern.matches()) {
                sb.insert(0, pattern.group(1)).append(pattern.group(3));
            }
        }
        return sb.toString();
    }

    private static boolean matchesFormattedString(String regexPattern, String replaceablePattern, String word) {
        var fullPattern = String.format(regexPattern, replaceablePattern);
        return Pattern.matches(fullPattern, word);
    }
}
