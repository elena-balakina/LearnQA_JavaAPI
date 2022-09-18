import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

public class CookieTests {

    @Test
    public void getAuthCookieTest() {
        Map<String, String> params = new HashMap<>();
        params.put("login", "secret_login");
        params.put("password", "secret_pass");

        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nResponse body:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        System.out.println(response.getHeaders());


        System.out.println("\nCookies:");
        System.out.println(response.getCookies());

        System.out.println("\nAuth_cookie: " + response.getCookie("auth_cookie"));
    }

    @Test
    public void checkAuthCookieTest() {
        Map<String, String> params = new HashMap<>();
        params.put("login", "secret_login");
        params.put("password", "secret_pass");

        Response responseGetAuthCookie = RestAssured
                .given()
                .queryParams(params)
                .when()
                .get("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String authCookie = responseGetAuthCookie.getCookie("auth_cookie");
        System.out.println("\nAuth_cookie: " + authCookie);

        Map<String, String> cookies = new HashMap<>();
        if (authCookie != null) {
            cookies.put("auth_cookie", authCookie);
        }

        Response responseCheckAuthCookie = RestAssured
                .given()
                .queryParams(params)
                .cookies(cookies)
                .when()
                .get("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseCheckAuthCookie.print();

        if (authCookie != null) {
            Assertions.assertTrue(responseCheckAuthCookie.asPrettyString().contains("You are authorized"));
        } else {
            Assertions.assertTrue(responseCheckAuthCookie.asPrettyString().contains("You are NOT authorized"));
        }
    }

    @ParameterizedTest
    @CsvSource({"secret_login,secret_pass", "secret_login2,secret_pass2"})
    public void checkAuthCookieParameterizedTest(String login, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        Response responseGetAuthCookie = RestAssured
                .given()
                .queryParams(params)
                .when()
                .get("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String authCookie = responseGetAuthCookie.getCookie("auth_cookie");
        System.out.println("\nAuth_cookie: " + authCookie);

        Map<String, String> cookies = new HashMap<>();
        if (authCookie != null) {
            cookies.put("auth_cookie", authCookie);
        }

        Response responseCheckAuthCookie = RestAssured
                .given()
                .queryParams(params)
                .cookies(cookies)
                .when()
                .get("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseCheckAuthCookie.print();

        if (authCookie != null) {
            Assertions.assertTrue(responseCheckAuthCookie.asPrettyString().contains("You are authorized"));
        } else {
            Assertions.assertTrue(responseCheckAuthCookie.asPrettyString().contains("You are NOT authorized"));
        }
    }
}
