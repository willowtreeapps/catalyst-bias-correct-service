package util;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class TestBiasDetector extends BiasDetector {
    public TestBiasDetector() {
        this(false);
    }

    public TestBiasDetector(boolean value) {
        _isBiasDetected = value;
    }

    @Override
    public boolean isBiasDetected(TextTokens input) { return _isBiasDetected; }

    @Override
    public Locale getBiasDetectedLocale(TextTokens input) {
        return _isBiasDetected ? BiasCorrectLocale.ENGLISH : null;
    }

    @Override
    Locale getLocale() {
        return BiasCorrectLocale.ENGLISH;
    }

    @Override
    Set<String> getPronouns() {
        return Collections.emptySet();
    }

    private boolean _isBiasDetected;
}
