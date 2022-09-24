package lesson_3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloTests {

    @Test
    public void helloWithoutNameTest() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        response.prettyPrint();
        String answer = response.get("answer");
        assertEquals("Hello, someone", answer, "Unexpected message");
    }

    @Test
    public void helloWithNameTest() {
        String name = "Elena";

        JsonPath response = RestAssured
                .given()
                .queryParam("name", name)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        response.prettyPrint();
        String actualAnswer = response.get("answer");
        String expectedAnswer = "Hello, " + name;

        assertEquals(expectedAnswer, actualAnswer, "Unexpected message");
    }

}
