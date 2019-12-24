import com.google.inject.AbstractModule;
import util.*;

public class Module extends AbstractModule {
    protected void configure() {
        bind(TextTokenizer.class).to(SimpleTextTokenizer.class);
        bind(BiasDetector.class).to(EnglishFemalePronounBiasDetector.class);
        bind(BiasCorrector.class).to(SimpleBiasCorrector.class).asEagerSingleton();
    }
}
