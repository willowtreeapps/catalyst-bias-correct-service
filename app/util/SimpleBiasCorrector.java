package util;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String correct(String input) {
        var tokens = _textTokenizer.tokenize(input);
        if (! _biasDetector.isBiasDetected(tokens)) {
            return input;
        }

        return Arrays.stream(tokens.getTokens())
                .map( token -> _corrections.getOrDefault(token, token))
                .collect(Collectors.joining(" "));
    }

    private BiasDetector _biasDetector;
    private TextTokenizer _textTokenizer;
    private Map<String, String> _corrections;
}
