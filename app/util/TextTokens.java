package util;

import java.util.Locale;

public class TextTokens {
    private final String _originalString;
    private final String[] _tokens;
    private final Locale _locale;

    public TextTokens(String originalString, String[] tokens, Locale locale) {
        _originalString = originalString;
        _tokens = tokens;
        _locale = locale;
    }

    public String[] getTokens() {
        return _tokens;
    }

    public String getOriginalString() {
        return _originalString;
    }

    public Locale getLocale() {
        return _locale;
    }
}
