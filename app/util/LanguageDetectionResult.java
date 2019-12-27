package util;

public class LanguageDetectionResult {
    public LanguageDetectionResult(LanguageDetectionConfidence confidence, String language) {
        _confidence = confidence;
        _language = language;
    }

    public LanguageDetectionConfidence getConfidence() {
        return _confidence;
    }

    public String getLanguage() {
        return _language;
    }

    private LanguageDetectionConfidence _confidence;
    private String _language;
}
