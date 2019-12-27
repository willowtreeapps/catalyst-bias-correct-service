package util;

import java.util.Locale;

public interface BiasCorrector {
    String correct(String input, Locale locale);
}
