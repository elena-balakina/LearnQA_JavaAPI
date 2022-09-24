package lesson_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HWUserAgentTests {

    @ParameterizedTest
    @MethodSource("testData")
    public void hwParameterizedUserAgentTest(String userAgent, String expectedPlatform, String expectedBrowser, String expectedDevice) {
        Response response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        System.out.println("\nResponse body:");
        response.prettyPrint();

        String actualPlatform = response.jsonPath().get("platform");
        String actualBrowser = response.jsonPath().get("browser");
        String actualDevice = response.jsonPath().get("device");

        assertEquals(expectedPlatform, actualPlatform, "Unexpected platform for User Agent '" + userAgent + "': expected: " + expectedPlatform + ", actual: " + actualPlatform);
        assertEquals(expectedBrowser, actualBrowser, "Unexpected browser for User Agent '" + userAgent + "': expected: " + expectedBrowser + ", actual: " + actualBrowser);
        assertEquals(expectedDevice, actualDevice, "Unexpected device for User Agent '" + userAgent + "': expected: " + expectedDevice + ", actual: " + actualDevice);
    }


    public static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                        "Mobile",
                        "No",
                        "Android"),
                Arguments.of(
                        "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                        "Mobile",
                        "Chrome",
                        "iOS"),
                Arguments.of(
                        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                        "Googlebot",
                        "Unknown",
                        "Unknown"),
                Arguments.of(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                        "Web",
                        "Chrome",
                        "No"),
                Arguments.of(
                        "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                        "Mobile",
                        "No",
                        "iPhone")
        );
    }
}
