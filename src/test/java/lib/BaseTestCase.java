package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {

    protected String getHeader(Response response, String headerName) {
        Headers headers = response.getHeaders();
        assertTrue(headers.hasHeaderWithName(headerName), "Response does not have header with name " + headerName);
        return headers.getValue(headerName);
    }

    protected String getCookie(Response response, String cookieName) {
        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.containsKey(cookieName), "Response does not have cookie with name " + cookieName);
        return cookies.get(cookieName);
    }

    protected int getIntFromJson(Response response, String keyName) {
        response.then().assertThat().body("$", hasKey(keyName));
        return response.jsonPath().getInt(keyName);
    }
}
