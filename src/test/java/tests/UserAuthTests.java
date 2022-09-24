package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserAuthTests extends BaseTestCase {

    String cookie;
    String header;
    int userIdFromAuth;

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = getCookie(responseGetAuth, "auth_sid");
        this.header = getHeader(responseGetAuth, "x-csrf-token");
        this.userIdFromAuth = getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    public void userAuthPositiveTest() {
        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        System.out.println("\nResponse:");
        responseCheckAuth.prettyPrint();

        Assertions.assertJsonByName(responseCheckAuth, "user_id", userIdFromAuth);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void userAuthNegativeTest(String condition) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookie);
        } else if (condition.equals("header")) {
            spec.header("x-csrf-token", header);
        } else {
            throw new IllegalArgumentException("Condition value is unknown: " + condition);
        }

        Response responseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    }
}
