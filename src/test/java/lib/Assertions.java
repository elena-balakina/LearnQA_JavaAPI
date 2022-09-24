package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertJsonByName(Response response, String keyName, int expectedValue) {
        response.then().assertThat().body("$", hasKey(keyName));

        int actualValue = response.jsonPath().getInt(keyName);
        assertEquals(expectedValue, actualValue, "JSON value is not equal to expected value");
    }
}
