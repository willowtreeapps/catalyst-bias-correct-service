import com.google.inject.AbstractModule;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import util.*;

public class Module extends AbstractModule {
    protected void configure() {
        // Application
        bind(TextTokenizer.class).to(OpenNLPTextTokenizer.class);
        bind(BiasDetector.class).to(EnglishFemalePronounBiasDetector.class);
        bind(BiasCorrector.class).to(SimpleBiasCorrector.class).asEagerSingleton();

        // Third-party
        bind(Tokenizer.class).to(SimpleTokenizer.class).asEagerSingleton();
    }
}
