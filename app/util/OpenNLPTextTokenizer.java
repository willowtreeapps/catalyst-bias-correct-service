package util;

import opennlp.tools.tokenize.Detokenizer;
import opennlp.tools.tokenize.Tokenizer;

import javax.inject.Inject;

public class OpenNLPTextTokenizer implements TextTokenizer {
    @Inject
    public OpenNLPTextTokenizer(Tokenizer tokenizer, Detokenizer detokenizer) {
        _tokenizer = tokenizer;
        _detokenizer = detokenizer;
    }

    @Override
    public TextTokens tokenize(String input) {
        var tokens = _tokenizer.tokenize(input);
        return new TextTokens(input, tokens);
    }

    @Override
    public String detokenize(TextTokens tokens) {
        return _detokenizer.detokenize(tokens.getTokens(), null);
    }

    private Tokenizer _tokenizer;
    private Detokenizer _detokenizer;
}
