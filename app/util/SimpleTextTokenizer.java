package util;

public class SimpleTextTokenizer implements TextTokenizer {
    @Override
    public TextTokens tokenize(String input) {
        return new TextTokens(input.split("\\s"));
    }
}
