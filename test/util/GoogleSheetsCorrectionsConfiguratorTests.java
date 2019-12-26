package util;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GoogleSheetsCorrectionsConfiguratorTests {
    @Test
    public void TestFetchingGoogleSheetsCorrectionsConfigurator() throws MalformedURLException {
        var parser = new CSVCorrectionsParser();
        var url = new File("test/util/GoogleSheetsCorrectionsTest.txt").toURI().toURL();
        var randomizer = new MonoatomicallyIncrementingRandomizer();
        var configurator = new GoogleSheetsCorrectionsConfigurator(url, parser);

        var corrector = configurator.createCorrector(randomizer);
        assertNotNull(corrector);

        var locale = new Locale("en");
        var suggestion = corrector.correct("key1", locale);
        assertEquals("value1", suggestion);

        var suggestionTwo = corrector.correct("key1", locale);
        assertEquals("value3", suggestionTwo);
    }
}
