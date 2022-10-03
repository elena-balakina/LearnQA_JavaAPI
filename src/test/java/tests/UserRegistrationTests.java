package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationTests extends BaseTestCase {

    @Test
    public void createUserWithExistingEmailTest() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnQA");
        userData.put("firstName", "learnQA");
        userData.put("lastName", "learnQA");

        Response responseCreateUser = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println("\nResponse: " + responseCreateUser.asString());
        System.out.println("\nStatus code: " + responseCreateUser.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Users with email '" + email + "' already exists");
    }

    @Test
    public void createUserSuccessfullyTest() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnQA");
        userData.put("firstName", "learnQA");
        userData.put("lastName", "learnQA");

        Response responseCreateUser = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseCreateUser.asString());

        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        Assertions.assertJsonHasKey(responseCreateUser, "id");
    }
}
