package lesson_3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterizedHelloTests {

    @ParameterizedTest
    @ValueSource(strings = {"", "Elena", "John"})
    public void helloWithoutNameTest(String name) {
        Map<String, String> params = new HashMap<>();
        if (name.length() != 0){
            params.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        response.prettyPrint();

        String actualAnswer = response.get("answer");
        String expectedAnswer = (name.length() != 0) ? "Hello, " + name : "Hello, someone";

        assertEquals(expectedAnswer, actualAnswer, "Unexpected message");
    }
}
