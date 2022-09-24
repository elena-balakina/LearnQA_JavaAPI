package lesson_3;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

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
        Headers header = responseGetAuth.getHeaders();
        int userIdFromAuth = responseGetAuth.jsonPath().getInt("user_id");

        System.out.println("\nCookies:");
        System.out.println(cookies);

        System.out.println("\nHeaders:");
        System.out.println(header);

        System.out.println("\nUser id::");
        System.out.println(userIdFromAuth);

        assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code");
        assertTrue(cookies.containsKey("auth_sid"), "Response cookies do not contain key 'auth_sid'");
        assertTrue(header.hasHeaderWithName("x-csrf-token"), "Response headers do not have header 'x-csrf-token'");
        assertTrue(userIdFromAuth > 0, "User id should be grater than 0");

        // second request - check login
        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", header.getValue("x-csrf-token"))
                .cookie("auth_sid", cookies.get("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        System.out.println("\nResponse:");
        responseCheckAuth.prettyPrint();

        int userIdFromCheck = responseCheckAuth.jsonPath().getInt("user_id");
        assertTrue(userIdFromCheck > 0, "Unexpected user id: " + userIdFromCheck);
        assertEquals(userIdFromAuth, userIdFromCheck, "User ids do not match");
    }
}
