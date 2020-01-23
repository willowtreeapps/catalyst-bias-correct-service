package util;

import opennlp.tools.tokenize.Detokenizer;
import opennlp.tools.tokenize.Tokenizer;

import javax.inject.Inject;
import java.util.Locale;

public class OpenNLPTextTokenizer implements TextTokenizer {
    @Inject
    public OpenNLPTextTokenizer(Tokenizer tokenizer, Detokenizer detokenizer) {
        _tokenizer = tokenizer;
        _detokenizer = detokenizer;
    }

    @Override
    public TextTokens tokenize(String input, Locale locale) {
        var tokens = _tokenizer.tokenize(input);
        return new TextTokens(input, tokens, locale);
    }

    @Override
    public String detokenize(TextTokens tokens) {
        return _detokenizer.detokenize(tokens.getTokens(), null);
    }

    private Tokenizer _tokenizer;
    private Detokenizer _detokenizer;
}
