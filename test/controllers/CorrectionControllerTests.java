package controllers;

import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import util.MonotonicallyIncrementingRandomizer;
import util.Randomizer;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.inject.Bindings.bind;
import static play.test.Helpers.*;

public class CorrectionControllerTests extends WithApplication {
    private static final String CORRECT_URI = "/corrector/correct";
    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .configure("google_sheets_configuration_url", "test/util/GoogleSheetsCorrectionsTest.txt")
                .overrides(bind(Randomizer.class).to(MonotonicallyIncrementingRandomizer.class))
                .build();
    }

    @Test
    public void testSimpleCorrection() {
        var json = Json.toJson(Map.of(
                "context", "context-value",
                "text", "she is keyone"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("she is value1", response.correction);
        assertEquals("she is keyone", response.input);
        assertEquals("context-value", response.context);
    }

    @Test
    public void testCorrectionWithoutContext() {
        var json = Json.toJson(Map.of(
                "text", "she is keyone"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("she is value1", response.correction);
        assertEquals("she is keyone", response.input);
        assertEquals("", response.context);
    }

    @Test
    public void testCorrectionWithEmptyBody() {
        var json = Json.toJson(Map.of());
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("", response.correction);
        assertEquals("", response.input);
        assertEquals("", response.context);
    }

    @Test
    public void testCorrectionWithNoBody() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("", response.correction);
        assertEquals("", response.input);
        assertEquals("", response.context);
    }

    @Test@Ignore
    public void testMultiWordTrigger() {
        var json = Json.toJson(Map.of(
                "context", "context-value",
                "text", "she is key four"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("she is value4", response.correction);
        assertEquals("she is key four", response.input);
        assertEquals("context-value", response.context);
    }
}
