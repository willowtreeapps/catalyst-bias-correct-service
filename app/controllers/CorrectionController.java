package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Arrays;
import java.util.Map;

public class CorrectionController extends Controller {

    public Result correct(Http.Request request) {
        var textToCorrect = getString(request, TextFieldName);
        var context = getString(request, ContextFieldName);
        var correction = performCorrection(textToCorrect);

        var response = Map.of(
            "input", textToCorrect,
            "context", context,
            "correction", correction
        );
        return ok(Json.toJson(response));
    }

    private String performCorrection(String input) {
        return input;
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

    private static String TextFieldName = "text";
    private static String ContextFieldName = "context";
}
