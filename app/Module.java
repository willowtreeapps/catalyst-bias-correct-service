import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import opennlp.tools.tokenize.*;
import util.*;

public class Module extends AbstractModule {
    protected void configure() {
        // Application
        bind(TextTokenizer.class).to(OpenNLPTextTokenizer.class);
        bind(BiasDetector.class).to(LanguageAwareBiasDetector.class);
        bind(Randomizer.class).to(SimpleRandomizer.class);

        bind(BiasCorrector.class).toProvider(GoogleSheetsBackedBiasDetectorProvider.class).asEagerSingleton();
    }

    @Provides
    private static Detokenizer createDetokenizer() {
        String[] tokens = new String[]{".", "!", "(", ")", "\"", "-", "?", "'"};

        DetokenizationDictionary.Operation[] operations = new DetokenizationDictionary.Operation[]{
                DetokenizationDictionary.Operation.MOVE_LEFT,
                DetokenizationDictionary.Operation.MOVE_LEFT,
                DetokenizationDictionary.Operation.MOVE_RIGHT,
                DetokenizationDictionary.Operation.MOVE_LEFT,
                DetokenizationDictionary.Operation.RIGHT_LEFT_MATCHING,
                DetokenizationDictionary.Operation.MOVE_BOTH,
                DetokenizationDictionary.Operation.MOVE_LEFT,
                DetokenizationDictionary.Operation.MOVE_BOTH
        };

        return new DictionaryDetokenizer(new DetokenizationDictionary(tokens, operations));
    }

    @Provides @Singleton
    private static Tokenizer createWhitespaceTokenizer() {
        return WhitespaceTokenizer.INSTANCE;
    }
}
