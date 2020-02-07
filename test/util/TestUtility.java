package util;

import opennlp.tools.tokenize.DetokenizationDictionary;
import opennlp.tools.tokenize.DictionaryDetokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class TestUtility {
    public static TextTokenizer createTextTokenizer() {
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

        var detokenizer = new DictionaryDetokenizer(new DetokenizationDictionary(tokens, operations));

        return new OpenNLPTextTokenizer(WhitespaceTokenizer.INSTANCE, detokenizer);
    }

    private TestUtility() {
    }
}
