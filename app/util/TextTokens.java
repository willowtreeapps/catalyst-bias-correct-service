package util;

public class TextTokens {
    private final String _originalString;
    private final String[] _tokens;

    public TextTokens(String originalString, String[] tokens) {
        _originalString = originalString;
        _tokens = tokens;
    }

    public String[] getTokens() {
        return _tokens;
    }

    public String getOriginalString() {
        return _originalString;
    }
}
