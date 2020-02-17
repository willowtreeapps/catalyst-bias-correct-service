package util;

import java.util.Locale;
import java.util.Set;

public abstract class BiasDetector {
    public boolean isBiasDetected(TextTokens input, TextTokenizer tokenizer) {
        return getPronouns()
                .parallelStream()
                .anyMatch(pronoun -> !Utility.findMatches(pronoun, input, tokenizer).isEmpty());
    }

    public Locale getBiasDetectedLocale(TextTokens input, TextTokenizer tokenizer) {
        return isBiasDetected(input, tokenizer) ? getLocale() : null;
    }

    abstract Locale getLocale();
    abstract Set<String> getPronouns();
}
