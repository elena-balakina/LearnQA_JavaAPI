import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RedirectionTests {

    @Test
    public void redirectFalseTest() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        Assertions.assertEquals(303, statusCode);
    }

    @Test
    public void redirectTrueTest() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void longRedirectFalseTest() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        Assertions.assertEquals(301, statusCode);
        System.out.println("Redirect URL: " + response.getHeader("location"));
    }

    @Test
    public void longRedirectTrueTest() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        Assertions.assertEquals(200, statusCode);

        String locationHeader = response.getHeader("Location");
        System.out.println("Redirect URL: " + locationHeader);
        Assertions.assertNull(locationHeader);
    }
}
