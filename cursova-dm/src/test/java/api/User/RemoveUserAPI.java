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
@Feature("Remove User")
public class RemoveUserAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to successfully remove an existing user by user ID")
    @Step("Removing user by user ID")
    public void removeUserSuccess() throws IOException {
        // Load userId from the configuration file
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        String userId = props.getProperty("userId");

        // Ensure userId is not null
        Assert.assertNotNull(userId, "User ID is not set in the config file!");

        // Create request body
        String requestBody = buildRequestBody(userId);

        // Execute POST request to remove the user
        Response response = sendRemoveUserRequest(requestBody);

        // Validate the response
        validateRemoveUserResponse(response, userId);
    }

    @Step("Building request body for removing user")
    private String buildRequestBody(String userId) {
        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeUser\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"user_id\": " + userId + "\n" +
                "    }\n" +
                "}";
    }

    @Step("Sending POST request to remove user")
    private Response sendRemoveUserRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating remove user response")
    private void validateRemoveUserResponse(Response response, String userId) {
        // Log response body to console
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status and result
        Assert.assertEquals(response.getStatusCode(), 200);
        Boolean result = response.jsonPath().getBoolean("result");
        Assert.assertTrue(result, "Expected user to be removed but got failure!");

        System.out.println("User with ID " + userId + " was successfully removed.");
    }
}
