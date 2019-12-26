package util;

public class TestBiasDetector implements BiasDetector {

    public void setBiasDetected(boolean value) {
        _isBiasDetected = value;
    }

    @Override
    public boolean isBiasDetected(TextTokens input) {
        return _isBiasDetected;
    }

    private boolean _isBiasDetected;
}
