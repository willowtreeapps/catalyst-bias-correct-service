package util;

import opennlp.tools.tokenize.Tokenizer;

import javax.inject.Inject;

public class OpenNLPTextTokenizer implements TextTokenizer {
    @Inject
    public OpenNLPTextTokenizer(Tokenizer tokenizer) {
        _tokenizer = tokenizer;
    }

    @Override
    public TextTokens tokenize(String input) {
        var tokens = _tokenizer.tokenize(input);
        return new TextTokens(tokens);
    }

    private Tokenizer _tokenizer;
}
