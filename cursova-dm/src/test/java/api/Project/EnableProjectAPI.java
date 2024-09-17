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
@Feature("Enable Project")
public class EnableProjectAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to enable an existing project")
    @Step("Enabling project with valid data")
    public void enableProjectSuccess() throws IOException {
        // Initialize properties object
        Properties props = new Properties();

        // Load existing data from config file
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }

        // Get project ID from the properties file
        String projectId = props.getProperty("projectId");
        Assert.assertNotNull(projectId, "Project ID not found in config file!");

        // Build request body
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"enableProject\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": [\"" + projectId + "\"]\n" +
                "}";

        // Send POST request to enable the project
        Response response = sendPostRequest(requestBody);

        // Validate the response
        validateEnableProjectResponse(response, projectId);
    }

    @Step("Sending POST request to enable project")
    private Response sendPostRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD) // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating enable project response")
    private void validateEnableProjectResponse(Response response, String projectId) {
        // Print response body for debugging purposes
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 status code.");

        // Validate result in the response
        Boolean result = response.jsonPath().getBoolean("result");
        Assert.assertTrue(result, "Failed to enable project with ID: " + projectId);

        System.out.println("Project enabled with ID: " + projectId);
    }
}
