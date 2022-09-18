import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HeadersTests {

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
