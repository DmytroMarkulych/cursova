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
@Feature("Enable Public Access to Project")
public class EnableProjectPublicAccessAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to enable public access to an existing project")
    @Step("Enabling public access for project")
    public void enablePublicAccessSuccess() throws IOException {
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
                "    \"method\": \"enableProjectPublicAccess\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": [\"" + projectId + "\"]\n" +
                "}";

        // Send POST request to enable public access for the project
        Response response = sendPostRequest(requestBody);

        // Validate response
        validatePublicAccessEnablement(response, projectId);
    }

    @Step("Sending POST request to enable public access for project")
    private Response sendPostRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD) // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating enable public access response")
    private void validatePublicAccessEnablement(Response response, String projectId) {
        // Print response body for debugging purposes
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 status code.");

        // Validate result in the response
        Boolean result = response.jsonPath().getBoolean("result");
        Assert.assertTrue(result, "Failed to enable public access for project with ID: " + projectId);

        System.out.println("Public access enabled for project ID: " + projectId);
    }
}
