package util;

import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageConfidence;

import java.io.IOException;

public class TikaLanguageDetector implements LanguageDetector {
    public TikaLanguageDetector() throws IOException {
        _detector = new OptimaizeLangDetector();
        _detector.loadModels();
    }

    @Override
    public LanguageDetectionResult detect(String input) {
        var result = _detector.detect(input);
        if (result == null) {
            return null;
        }

        return new LanguageDetectionResult(Map(result.getConfidence()), result.getLanguage());
    }

    private static LanguageDetectionConfidence Map(LanguageConfidence confidence) {
        switch (confidence) {
            case HIGH:
                return LanguageDetectionConfidence.HIGH;
            case LOW:
                return LanguageDetectionConfidence.LOW;
            case MEDIUM:
                return LanguageDetectionConfidence.MEDIUM;
            case NONE:
                return LanguageDetectionConfidence.NONE;
        }
        return LanguageDetectionConfidence.NONE;
    }

    private org.apache.tika.language.detect.LanguageDetector _detector;
}
