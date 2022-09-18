import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResponseCodeTests {

    @Test
    public void checkStatusCode200Test() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
    }

    @Test
    public void checkStatusCode500Test() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_500")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        Assertions.assertEquals(500, statusCode);
    }

}
