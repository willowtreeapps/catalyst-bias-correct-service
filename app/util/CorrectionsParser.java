package util;
import java.net.URL;
import java.util.List;

public interface CorrectionsParser {
    List<Correction> parse(URL file);
}
