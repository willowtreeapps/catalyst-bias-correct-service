package util;

public interface TextTokenizer {
    TextTokens tokenize(String input);
    String detokenize(TextTokens tokens);
}
