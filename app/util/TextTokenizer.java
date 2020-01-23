package util;

import java.util.Locale;

public interface TextTokenizer {
    TextTokens tokenize(String input, Locale locale);
    String detokenize(TextTokens tokens);
}
