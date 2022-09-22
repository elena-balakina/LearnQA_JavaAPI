package lesson_2;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CheckTypeTests {

    @Test
    public void checkTypeGetTest() {
        Response response = RestAssured
                .given()
                .param("param1", "value1")
                .param("param2", "value2")
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }

    @Test
    public void checkTypePostTest() {
        Map<String, String> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");

        Response response = RestAssured
                .given()
//                .body("param1=value1&param2=value2")
//                .body("{\n" +
//                        "\t\"param1\": \"value1\",\n" +
//                        "\t\"param2\": \"value2\"\n" +
//                        "}")
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }

    public static class HeadersTests {

        @Test
        public void showAllHeadersTest() {
            Map<String, String> headers = new HashMap<>();
            headers.put("myHeaderName1", "myHeaderValue1");
            headers.put("myHeaderName2", "myHeaderValue2");

            Response response = RestAssured
                    .given()
                    .headers(headers)
                    .when()
                    .get("https://playground.learnqa.ru/api/show_all_headers")
                    .andReturn();

            response.prettyPrint();
            Headers responseHeaders = response.getHeaders();
            System.out.println("Response headers:");
            System.out.println(responseHeaders);
        }
    }
}
