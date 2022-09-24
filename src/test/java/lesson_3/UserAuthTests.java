package lesson_3;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTests {

    @Test
    public void userAuthPositiveTest() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // first request - login
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        int userIdFromAuth = responseGetAuth.jsonPath().getInt("user_id");

        System.out.println("\nCookies:");
        System.out.println(cookies);

        System.out.println("\nHeaders:");
        System.out.println(headers);

        System.out.println("\nUser id::");
        System.out.println(userIdFromAuth);

        assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code");
        assertTrue(cookies.containsKey("auth_sid"), "Response cookies do not contain key 'auth_sid'");
        assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response headers do not have header 'x-csrf-token'");
        assertTrue(userIdFromAuth > 0, "User id should be grater than 0");

        // second request - check login
        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", headers.getValue("x-csrf-token"))
                .cookie("auth_sid", cookies.get("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        System.out.println("\nResponse:");
        responseCheckAuth.prettyPrint();

        int userIdFromCheck = responseCheckAuth.jsonPath().getInt("user_id");
        assertTrue(userIdFromCheck > 0, "Unexpected user id: " + userIdFromCheck);
        assertEquals(userIdFromAuth, userIdFromCheck, "User ids do not match");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void userAuthNegativeTest(String condition) {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // first request - login
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookies.get("auth_sid"));
        } else if (condition.equals("header")) {
            spec.header("x-csrf-token", headers.getValue("x-csrf-token"));
        } else {
            throw new IllegalArgumentException("Condition value is unknown: " + condition);
        }

        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "User_id is expected to be 0");
    }
}
