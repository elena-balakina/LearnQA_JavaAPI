package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertJsonByName(Response response, String keyName, int expectedValue) {
        response.then().assertThat().body("$", hasKey(keyName));

        int actualValue = response.jsonPath().getInt(keyName);
        assertEquals(expectedValue, actualValue, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer) {
        assertEquals(expectedAnswer, response.asString(),
                "Response text is not as expected");
    }

    public static void assertResponseCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.statusCode(),
                "Response status code is not as expected");
    }

    public static void assertJsonHasKey(Response response, String expectedKey) {
        response.then().assertThat().body("$", hasKey(expectedKey));
    }

    public static void assertJsonHasKeys(Response response, String[] expectedKeys) {
        for (String expectedKey: expectedKeys) {
            response.then().assertThat().body("$", hasKey(expectedKey));
        }
    }

    public static void assertJsonHasNotKey(Response response, String unexpectedKey) {
        response.then().assertThat().body("$", not(hasKey(unexpectedKey)));
    }
}
