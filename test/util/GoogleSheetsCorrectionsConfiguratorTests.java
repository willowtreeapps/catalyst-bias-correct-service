package util;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static util.TestUtility.createTextTokenizer;

public class GoogleSheetsCorrectionsConfiguratorTests {
    @Test
    public void TestFetchingGoogleSheetsCorrectionsConfigurator() throws MalformedURLException {
        var parser = new CSVCorrectionsParser();
        var url = new File("test/util/GoogleSheetsCorrectionsTest.txt").toURI().toURL();
        var randomizer = new MonotonicallyIncrementingRandomizer();
        var configurator = new GoogleSheetsCorrectionsConfigurator(url, parser, new TestBiasDetector(true), createTextTokenizer());

        var corrector = configurator.createCorrector(randomizer);
        assertNotNull(corrector);

        var suggestion = corrector.correct("keyone");
        assertEquals("value1", suggestion);

        var suggestionTwo = corrector.correct("keyone");
        assertEquals("value3", suggestionTwo);
    }
}
