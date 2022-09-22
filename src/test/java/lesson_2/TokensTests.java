package lesson_2;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TokensTests {

    @Test
    public void tokenTest() throws InterruptedException {

        // Написать тест, который сделал бы следующее: 1) создавал задачу
        int seconds;
        String token;

        JsonPath responseCreateTask = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        token = responseCreateTask.get("token");
        seconds = responseCreateTask.get("seconds");

        responseCreateTask.prettyPrint();

        // 2) делал один запрос с token ДО того, как задача готова, убеждался в правильности поля status
        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        JsonPath responseBeforeTaskIsReady = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        responseBeforeTaskIsReady.prettyPrint();
        Assertions.assertEquals("Job is NOT ready", responseBeforeTaskIsReady.get("status"));

        //3) ждал нужное количество секунд с помощью функции time.sleep() - для этого надо сделать import time
        Thread.sleep(seconds * 1000L);

        //4) делал бы один запрос c token ПОСЛЕ того, как задача готова, убеждался в правильности поля status и наличии поля result
        JsonPath responseAfterTaskIsReady = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        responseAfterTaskIsReady.prettyPrint();
        Assertions.assertEquals("Job is ready", responseAfterTaskIsReady.get("status"));
        Assertions.assertNotEquals(0, responseAfterTaskIsReady.get("result").toString().length());
    }
}
