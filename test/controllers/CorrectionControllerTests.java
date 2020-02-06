package controllers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import util.MonotonicallyIncrementingRandomizer;
import util.Randomizer;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.inject.Bindings.bind;
import static play.test.Helpers.*;

public class CorrectionControllerTests extends WithApplication {
    private static final String CORRECT_URI = "/corrector/correct";
    private static SoftAssertions soft = new SoftAssertions();

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .configure("google_sheets_configuration_url", "test/files/lang_redirect.txt")
                .overrides(bind(Randomizer.class).to(MonotonicallyIncrementingRandomizer.class))
                .build();
    }

    @Test
    public void testSimpleCorrection() {
        var json = Json.toJson(Map.of(
                "context", "context-value",
                "text", "she is pushy"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("she is persuasive", response.correction);
        assertEquals("she is pushy", response.input);
        assertEquals("context-value", response.context);
    }

    @Test
    public void testCorrectionWithoutContext() {
        var json = Json.toJson(Map.of(
                "text", "she is pushy"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("she is persuasive", response.correction);
        assertEquals("she is pushy", response.input);
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

    @Test
    public void testMultiWordTrigger() {
        var json = Json.toJson(Map.of(
                "context", "context-value",
                "text", "she is a bitch"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("she is a boss", response.correction);
        assertEquals("she is a bitch", response.input);
        assertEquals("context-value", response.context);
    }

    @Test
    public void testCapitalizedPronoun() {
        var json = Json.toJson(Map.of(
                "context", "context-value",
                "text", "She is cold"
        ));
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CORRECT_URI)
                .bodyJson(json);

        Result result = route(app, request);
        var body = contentAsBytes(result).toArray();
        var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
        assertEquals(OK, result.status());
        assertEquals("She is focused", response.correction);
        assertEquals("She is cold", response.input);
        assertEquals("context-value", response.context);
    }

    @Test
    public void testSimpleEnglishCorrections() {
        try (CSVReader reader = new CSVReader(new FileReader("test/files/en.csv"))) {

            String[] nextLine;
            int rowNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                rowNumber++;
                String userInput = nextLine[0];
                String biasCorrection = nextLine[1];

                var json = Json.toJson(Map.of(
                        "context", "context-value",
                        "text", "she is " + userInput
                ));
                Http.RequestBuilder request = new Http.RequestBuilder()
                        .method(POST)
                        .uri(CORRECT_URI)
                        .bodyJson(json);

                Result result = route(app, request);
                var body = contentAsBytes(result).toArray();
                var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
                assertEquals(OK, result.status());
                soft.assertThat(response.correction).isEqualTo("she is " + biasCorrection);
                assertEquals("she is " + userInput, response.input);
                assertEquals("context-value", response.context);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        soft.assertAll();
    }

    @Test
    public void testSimpleSpanishCorrections() {
        try (CSVReader reader = new CSVReader(new FileReader("test/files/es.csv"))) {

            String[] nextLine;
            int rowNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                rowNumber++;
                String userInput = nextLine[0];
                String biasCorrection = nextLine[1];

                var json = Json.toJson(Map.of(
                        "context", "context-value",
                        "text", "ella es " + userInput
                ));
                Http.RequestBuilder request = new Http.RequestBuilder()
                        .method(POST)
                        .uri(CORRECT_URI)
                        .bodyJson(json);

                Result result = route(app, request);
                var body = contentAsBytes(result).toArray();
                var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
                assertEquals(OK, result.status());
                soft.assertThat(response.correction).isEqualTo("ella es " + biasCorrection);
                assertEquals("ella es " + userInput, response.input);
                assertEquals("context-value", response.context);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        soft.assertAll();
    }

    @Test
    public void testSimpleFrenchCorrections() {
        try (CSVReader reader = new CSVReader(new FileReader("test/files/fr.csv"))) {

            String[] nextLine;
            int rowNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                rowNumber++;
                String userInput = nextLine[0];
                String biasCorrection = nextLine[1];

                var json = Json.toJson(Map.of(
                        "context", "context-value",
                        "text", "elle est " + userInput
                ));
                Http.RequestBuilder request = new Http.RequestBuilder()
                        .method(POST)
                        .uri(CORRECT_URI)
                        .bodyJson(json);

                Result result = route(app, request);
                var body = contentAsBytes(result).toArray();
                var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
                assertEquals(OK, result.status());
                soft.assertThat(response.correction).isEqualTo("elle est " + biasCorrection);
                assertEquals("elle est " + userInput, response.input);
                assertEquals("context-value", response.context);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        soft.assertAll();
    }

    @Test
    public void testSimpleGermanCorrections() {
        try (CSVReader reader = new CSVReader(new FileReader("test/files/de.csv"))) {

            String[] nextLine;
            int rowNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                rowNumber++;
                String userInput = nextLine[0];
                String biasCorrection = nextLine[1];

                var json = Json.toJson(Map.of(
                        "context", "context-value",
                        "text", "ihre " + userInput
                ));
                Http.RequestBuilder request = new Http.RequestBuilder()
                        .method(POST)
                        .uri(CORRECT_URI)
                        .bodyJson(json);

                Result result = route(app, request);
                var body = contentAsBytes(result).toArray();
                var response = Json.fromJson(Json.parse(body), CorrectionController.CorrectResponse.class);
                assertEquals(OK, result.status());
                soft.assertThat(response.correction).isEqualTo("ihre " + biasCorrection);
                assertEquals("ihre " + userInput, response.input);
                assertEquals("context-value", response.context);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        soft.assertAll();
    }
}
