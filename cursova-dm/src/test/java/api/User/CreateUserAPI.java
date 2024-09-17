package api.User;

import base.BaseAPI;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

@Epic("User Management")
@Feature("Create User")
public class CreateUserAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to successfully create a new user")
    @Step("Creating a new user")
    public void createUserSuccess() throws IOException {
        // Завантажуємо існуючі дані з конфігураційного файлу
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }

        // Динамічно створюємо username і password
        String username = "user_" + System.currentTimeMillis();
        String password = "123456";

        // Створюємо тіло запиту для створення користувача
        String requestBody = buildRequestBody(username, password);

        // Виконуємо POST запит для створення користувача
        Response response = sendCreateUserRequest(requestBody);

        // Валідуємо відповідь
        validateCreateUserResponse(response, props, username, password);
    }

    @Step("Building request body for creating user")
    private String buildRequestBody(String username, String password) {
        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createUser\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"username\": \"" + username + "\",\n" +
                "        \"password\": \"" + password + "\"\n" +
                "    }\n" +
                "}";
    }

    @Step("Sending POST request to create user")
    private Response sendCreateUserRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating create user response and updating config file")
    private void validateCreateUserResponse(Response response, Properties props, String username, String password) throws IOException {
        // Виводимо response body в консоль для налагодження
        System.out.println("Response body: " + response.getBody().asString());

        // Перевіряємо статус відповіді
        Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200!");

        // Отримуємо ID створеного користувача
        String userId = response.jsonPath().getString("result");

        // Перевіряємо, що користувача створено успішно
        Assert.assertNotNull(userId, "User creation failed!");

        System.out.println("User created with ID: " + userId);

        // Оновлюємо userId, username, і password у конфігураційному файлі
        props.setProperty("userId", userId);
        props.setProperty("username", username);
        props.setProperty("password", password);

        // Зберігаємо оновлений конфігураційний файл
        try (FileOutputStream output = new FileOutputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.store(output, null);
        }
    }
}
