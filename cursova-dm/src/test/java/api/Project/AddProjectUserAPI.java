package api.Project;

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

@Epic("Project Management")
@Feature("Add User to Project")
public class AddProjectUserAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to add a user to an existing project with a specific role")
    @Step("Adding user to project")
    public void addProjectUserSuccess() throws IOException {
        // Initialize properties object
        Properties props = new Properties();

        // Load existing data from config file
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }

        // Get project ID and user ID from the properties file
        String projectId = props.getProperty("projectId");
        String userId = props.getProperty("userId");
        String role = "project-manager";

        // Validate that projectId and userId are present
        Assert.assertNotNull(projectId, "Project ID not found in config file!");
        Assert.assertNotNull(userId, "User ID not found in config file!");

        // Build request body
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"addProjectUser\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": [\n" +
                "        \"" + projectId + "\",\n" +
                "        \"" + userId + "\",\n" +
                "        \"" + role + "\"\n" +
                "    ]\n" +
                "}";

        // Send POST request to grant user access to the project
        Response response = sendPostRequest(requestBody);

        // Validate status code and result
        verifyResponse(response, projectId, userId, role);
    }

    @Step("Sending POST request to add user to project")
    private Response sendPostRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD) // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating response and checking result")
    private void verifyResponse(Response response, String projectId, String userId, String role) {
        // Print response body for debugging purposes
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 status code.");

        // Validate result in the response
        Boolean result = response.jsonPath().getBoolean("result");
        Assert.assertTrue(result, "Failed to add user to the project with ID: " + projectId);

        System.out.println("User with ID " + userId + " added to project with ID: " + projectId + " as " + role);
    }
}
