package util;

import org.junit.Test;
import play.test.WithApplication;

import java.io.File;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVCorrectionsParserTests extends WithApplication {
    @Test
    public void TestParsingCorrections() throws MalformedURLException {
        var parser = new CSVCorrectionsParser();
        var corrections = parser.parse(new File("test/util/CSVParsingTest.txt").toURI().toURL());
        assertNotNull(corrections);
        assertEquals("keyone", corrections.get(0).getTrigger());
        assertEquals("value1", corrections.get(0).getSuggestion());
        assertEquals("keytwo", corrections.get(1).getTrigger());
        assertEquals("value2", corrections.get(1).getSuggestion());
        assertEquals("keyone", corrections.get(2).getTrigger());
        assertEquals("value3", corrections.get(2).getSuggestion());
    }
}
