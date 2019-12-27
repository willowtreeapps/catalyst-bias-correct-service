package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.BiasCorrector;
import util.LanguageDetectionConfidence;
import util.LanguageDetector;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class CorrectionController extends Controller {
    @Inject
    public CorrectionController(BiasCorrector corrector, LanguageDetector languageDetector) {
        _biasCorrector = corrector;
        _languageDetector = languageDetector;
    }

    public Result correct(Http.Request request) {
        var textToCorrect = getString(request, TextFieldName);
        var context = getString(request, ContextFieldName);
        var languageDetection = _languageDetector.detect(textToCorrect);
        var locale = languageDetection.getConfidence() == LanguageDetectionConfidence.HIGH
                ? new Locale(languageDetection.getLanguage())
                : null;

        var correction = _biasCorrector.correct(textToCorrect, locale);

        var response = Map.of(
            "input", textToCorrect,
            "context", context,
            "correction", correction == null ? "" : correction
        );
        return ok(Json.toJson(response));
    }

    private String getString(Http.Request request, String fieldName) {
        var body = request.body();

        var json = body.asJson();
        if (json != null) {
            return json.has(fieldName) ? json.get(fieldName).asText() : "";
        }

        var form = body.asFormUrlEncoded();
        var array = (form == null || !form.containsKey(fieldName)) ? new String[0] : form.get(fieldName);
        return Arrays.stream(array).findFirst().orElse("");
    }

    private BiasCorrector _biasCorrector;
    private LanguageDetector _languageDetector;

    private static String TextFieldName = "text";
    private static String ContextFieldName = "context";
}
