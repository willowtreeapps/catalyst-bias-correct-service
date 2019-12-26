package util;

public class TestBiasDetector implements BiasDetector {
    public TestBiasDetector() {
        this(false);
    }

    public TestBiasDetector(boolean value) {
        _isBiasDetected = value;
    }

    public void setBiasDetected(boolean value) {
        _isBiasDetected = value;
    }

    @Override
    public boolean isBiasDetected(TextTokens input) {
        return _isBiasDetected;
    }

    private boolean _isBiasDetected;
}
