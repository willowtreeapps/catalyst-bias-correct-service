package util;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class SimpleBiasCorrector implements BiasCorrector {
    @Inject
    public SimpleBiasCorrector(BiasDetector detector, TextTokenizer tokenizer) {
        _biasDetector = detector;
        _textTokenizer = tokenizer;
        _corrections = Map.of(
                "bossy", "a boss"
        );
    }

    @Override
    public String correct(String input, Locale locale) {
        var tokens = _textTokenizer.tokenize(input);
        if (! _biasDetector.isBiasDetected(tokens)) {
            return input;
        }

        var corrections = Arrays.stream(tokens.getTokens())
                .map( token -> _corrections.getOrDefault(token, token))
                .toArray(String[]::new);
        return _textTokenizer.detokenize(new TextTokens(corrections));
    }

    private BiasDetector _biasDetector;
    private TextTokenizer _textTokenizer;
    private Map<String, String> _corrections;
}
