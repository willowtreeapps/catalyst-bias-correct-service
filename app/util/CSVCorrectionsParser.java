package util;

import com.google.common.collect.Streams;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class CSVCorrectionsParser implements CorrectionsParser {

    @Override
    public List<Correction> parse(URL file) {
        CSVParser parser;
        try {
            parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT);
        } catch (IOException e) {
            return null;
        }

        return Streams.stream(parser.iterator())
            .map( record -> new Correction(record.get(0), record.get(1)) )
            .collect(Collectors.toList());
    }
}
