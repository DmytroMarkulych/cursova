package api.User;

import base.BaseAPI;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

@Epic("User Management")
@Feature("Get User Details")
public class GetUserAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to successfully retrieve user details by user ID")
    @Step("Getting user details by user ID")
    public void getUserSuccess() throws IOException {
        // Load data from the configuration file
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        String userId = props.getProperty("userId");

        // Ensure userId is not null
        Assert.assertNotNull(userId, "User ID is not set in the config file!");

        // Create request body
        String requestBody = buildRequestBody(userId);

        // Execute POST request to get user details
        Response response = sendGetUserRequest(requestBody);

        // Validate the response
        validateGetUserResponse(response, userId);
    }

    @Step("Building request body to get user details")
    private String buildRequestBody(String userId) {
        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"getUser\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"user_id\": " + userId + "\n" +
                "    }\n" +
                "}";
    }

    @Step("Sending POST request to get user details")
    private Response sendGetUserRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating get user response")
    private void validateGetUserResponse(Response response, String userId) {
        // Log response body to console
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status and results
        Assert.assertEquals(response.getStatusCode(), 200);
        String username = response.jsonPath().getString("result.username");
        Assert.assertNotNull(username, "User not found!");
    }
}
