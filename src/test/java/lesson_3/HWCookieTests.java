package lesson_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HWCookieTests {

    @Test
    public void hwCookieTests() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        System.out.println("\nCookies:");
        System.out.println(response.getCookies());

        assertEquals("hw_value", response.getCookie("HomeWork"), "Unexpected cookie value");
    }
}
