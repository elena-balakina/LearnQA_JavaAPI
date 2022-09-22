package lesson_2;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GetSecretPasswordTests {

    @ParameterizedTest
    @MethodSource("testData")
    public void getSecretPasswordParameterizedTest(String password) {
        Map<String, String> body = new HashMap<>();
        body.put("login", "super_admin");
        body.put("password", password);

        System.out.println("Password = " + password);

        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();
        String authCookie = response.getCookie("auth_cookie");
        System.out.println("\nAuth_cookie: " + authCookie);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("auth_cookie", authCookie);

        Response responseCheckAuthCookie = RestAssured
                .given()
                .queryParams(body)
                .cookies(cookies)
                .when()
                .get("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseCheckAuthCookie.print();
        Assertions.assertTrue(responseCheckAuthCookie.asPrettyString().contains("You are authorized"));
    }

    public static Stream<String> testData() {
        return Stream.of(getPasswordFromWiki().toArray(new String[0]));
    }

    @Test
    public void getSecretPasswordTest() {
        List<String> passwordsLists = getPasswordFromWiki();

        for (String password : passwordsLists)
        {
            Map<String, String> body = new HashMap<>();
            body.put("login", "super_admin");
            body.put("password", password);

            Response response = RestAssured
                    .given()
                    .body(body)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String authCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", authCookie);

            Response responseCheckAuthCookie = RestAssured
                    .given()
                    .queryParams(body)
                    .cookies(cookies)
                    .when()
                    .get("https://playground.learnqa.ru/api/check_auth_cookie")
                    .andReturn();

            if (responseCheckAuthCookie.asPrettyString().contains("You are authorized")) {
                System.out.println("PASSWORD IS FOUND: " + password);
                break;
            }
        }
    }

    public static List<String> getPasswordFromWiki() {
        List<String> passwordsList = new ArrayList<>();

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords");
        driver.manage().window().maximize();

        for (int i = 2; i <= 10; i++) {
            for (int j = 2; j <= 26; j++) {
                passwordsList.add(driver
                        .findElement(By.xpath("//caption[contains(text(), 'Top 25 most common passwords by year according to SplashData')]//..//tbody//tr[" + j + "]//td[" + i + "]"))
                        .getText());
            }
        }
        driver.quit();
        return passwordsList;
    }
}
