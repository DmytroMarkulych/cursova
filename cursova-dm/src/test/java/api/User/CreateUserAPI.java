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
        // Load existing data from the configuration file
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }

        // Dynamically generate username and password
        String username = "user_" + System.currentTimeMillis();
        String password = "123456";

        // Create request body for user creation
        String requestBody = buildRequestBody(username, password);

        // Execute POST request to create the user
        Response response = sendCreateUserRequest(requestBody);

        // Validate the response
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
        // Log response body to console for debugging
        System.out.println("Response body: " + response.getBody().asString());

        // Validate the response status
        Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200!");

        // Get the ID of the created user
        String userId = response.jsonPath().getString("result");

        // Ensure the user was created successfully
        Assert.assertNotNull(userId, "User creation failed!");

        System.out.println("User created with ID: " + userId);

        // Update userId, username, and password in the configuration file
        props.setProperty("userId", userId);
        props.setProperty("username", username);
        props.setProperty("password", password);

        // Save the updated configuration file
        try (FileOutputStream output = new FileOutputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.store(output, null);
        }
    }
}
