package util;

import java.util.Locale;

public class TestBiasDetector implements BiasDetector {
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

    private boolean _isBiasDetected;
}
