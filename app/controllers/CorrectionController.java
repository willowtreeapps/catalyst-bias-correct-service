package controllers;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.BiasCorrector;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CorrectionController extends Controller {
    public static class CorrectResponse {
        public String input;
        public String context;
        public String correction;
    }

    @Inject
    public CorrectionController(HttpExecutionContext ec, BiasCorrector corrector) {
        _biasCorrector = corrector;
        _ec = ec;
    }

    public CompletionStage<Result> correct(Http.Request request) {
        var textToCorrect = getString(request, TextFieldName);
        var context = getString(request, ContextFieldName);

        var correctionResult = CompletableFuture.supplyAsync(() ->_biasCorrector.correct(textToCorrect), _ec.current());

        return correctionResult.thenApplyAsync(correction -> {
            var response = new CorrectResponse();
            response.context = context;
            response.correction = correction == null ? "" : correction;
            response.input = textToCorrect;
            return ok(Json.toJson(response));
        }, _ec.current());
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
    private HttpExecutionContext _ec;

    private static String TextFieldName = "text";
    private static String ContextFieldName = "context";
}
