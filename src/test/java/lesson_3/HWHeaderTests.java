package lesson_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HWHeaderTests {

    @Test
    public void hwHeaderTest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        System.out.println("\nHeaders:");
        System.out.println(response.getHeaders());

        String actualSecretHeaderValue = response.getHeader("x-secret-homework-header");
        assertEquals("Some secret value", actualSecretHeaderValue, "Unexpected header value");
    }
}
