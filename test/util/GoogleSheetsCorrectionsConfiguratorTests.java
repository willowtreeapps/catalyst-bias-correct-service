package util;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static util.TestUtility.createTextTokenizer;

public class GoogleSheetsCorrectionsConfiguratorTests {
    @Test
    public void TestFetchingGoogleSheetsCorrectionsConfigurator() throws MalformedURLException {
        var parser = new CSVCorrectionsParser();
        var url = new File("test/util/GoogleSheetsCorrectionsTest.txt").toURI().toURL();
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var biasDetector = new TestBiasDetector();
        biasDetector.setBiasDetected(true);
        var configurator = new GoogleSheetsCorrectionsConfigurator(url, parser, biasDetector, createTextTokenizer());

        var corrector = configurator.createCorrector(randomizer);
        assertNotNull(corrector);

        var locale = new Locale("en");
        var suggestion = corrector.correct("keyone", locale);
        assertEquals("value1", suggestion);

        var suggestionTwo = corrector.correct("keyone", locale);
        assertEquals("value3", suggestionTwo);
    }
}
