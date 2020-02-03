package util;

import java.util.Locale;

public interface BiasDetector {
    boolean isBiasDetected(TextTokens input);
    Locale getBiasDetectedLocale(TextTokens input);
}
