package lesson_3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthWithBeforeAfterTests {

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

        this.cookie = responseGetAuth.getCookie("auth_sid");
        this.header = responseGetAuth.getHeader("x-csrf-token");
        this.userIdFromAuth = responseGetAuth.jsonPath().getInt("user_id");
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

        int userIdFromCheck = responseCheckAuth.jsonPath().getInt("user_id");
        assertTrue(userIdFromCheck > 0, "Unexpected user id: " + userIdFromCheck);
        assertEquals(userIdFromAuth, userIdFromCheck, "User ids do not match");
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

        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "User_id is expected to be 0");
    }
}
