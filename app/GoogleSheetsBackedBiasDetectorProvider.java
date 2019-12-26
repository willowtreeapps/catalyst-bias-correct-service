import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import util.BiasCorrector;
import util.CSVCorrectionsParser;
import util.GoogleSheetsCorrectionsConfigurator;
import util.Randomizer;

import java.net.MalformedURLException;
import java.net.URL;

public class GoogleSheetsBackedBiasDetectorProvider implements Provider<BiasCorrector> {
    @Inject
    public GoogleSheetsBackedBiasDetectorProvider(Config config, Randomizer randomizer) throws MalformedURLException {
        _randomizer = randomizer;

        var startingUrl = new URL(config.getString("google_sheets_configuration_url"));
        _configurator = new GoogleSheetsCorrectionsConfigurator<>(startingUrl, new CSVCorrectionsParser());
    }

    @Override
    public BiasCorrector get() {
        return _configurator.createCorrector(_randomizer);
    }

    private Randomizer _randomizer;
    private GoogleSheetsCorrectionsConfigurator<CSVCorrectionsParser> _configurator;
}
