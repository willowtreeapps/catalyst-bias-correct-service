package util;

import com.google.common.collect.Streams;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;

import static java.util.stream.Collectors.toMap;

public class GoogleSheetsCorrectionsConfigurator<TParser extends CorrectionsParser> implements CorrectionsConfigurator {
    public GoogleSheetsCorrectionsConfigurator(URL startingUrl, TParser parser, BiasDetector detector, TextTokenizer tokenizer) {
        _startingUrl = startingUrl;
        _parser = parser;
        _detector = detector;
        _tokenizer = tokenizer;
    }

    @Override
    public BiasCorrector createCorrector(Randomizer randomizer) {
        try (CSVParser parser = CSVParser.parse(_startingUrl, Charset.defaultCharset(), CSVFormat.DEFAULT)) {

            var mapping = Streams.stream(parser.iterator())
                    .map(GoogleSheetsCorrectionsConfigurator::GetLocaleAndURLFromRecord)
                    .filter( tuple -> tuple != null)
                    .map( x -> Pair.with(x.getValue0(), _parser.parse(x.getValue1())))
                    .collect(toMap(x -> x.getValue0(), x -> Utility.collectIntoMapOfSuggestionsByTriggerWord(x.getValue1())));
            return new MapBackedBiasCorrector(mapping, randomizer, _detector, _tokenizer);

        } catch (IOException e) {
            return null;
        }
    }

    private static Pair<Locale, URL> GetLocaleAndURLFromRecord(CSVRecord record) {
        var languageTag = record.get(0);
        var urlString = record.get(1);

        Locale locale;
        URL url;

        locale = (locale = Locale.forLanguageTag(languageTag)) == null ? new Locale(languageTag) : locale;
        url = GetUrlOrFileUrl(urlString);
        return url == null || locale == null ? null : Pair.with(locale, url);
    }

    private static URL GetUrlOrFileUrl(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            // continue
        }

        try {
            return new File(urlString).toURI().toURL();
        } catch (MalformedURLException e) {
            // continue
        }

        return null;
    }

    private URL _startingUrl;
    private TParser _parser;
    private BiasDetector _detector;
    private TextTokenizer _tokenizer;
}
