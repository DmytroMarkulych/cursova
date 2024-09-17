package api.Project;

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

@Epic("Project Management")
@Feature("Create Project")
public class CreateProjectAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to create a new project with valid data")
    @Step("Creating a project with valid data")
    public void createProjectSuccess() throws IOException {
        // Initialize properties object
        Properties props = new Properties();

        // Try-with-resources for automatic resource management
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            // Load existing data from config file
            props.load(input);
        }

        // Dynamically generate project details
        String projectName = "Project_" + System.currentTimeMillis();
        String description = "Description_" + System.currentTimeMillis();
        String owner_id = props.getProperty("userId");

        // Build request body
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createProject\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"name\": \"" + projectName + "\",\n" +
                "        \"description\": \"" + description + "\",\n" +
                "        \"owner_id\": \"" + owner_id + "\"\n" +
                "    }\n" +
                "}";

        // Send POST request to create the project
        Response response = sendPostRequest(requestBody);

        // Validate response
        validateProjectCreation(response, props, projectName);
    }

    @Step("Sending POST request to create a project")
    private Response sendPostRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD) // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating project creation response")
    private void validateProjectCreation(Response response, Properties props, String projectName) throws IOException {
        // Print response body for debugging purposes
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status code and result presence
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 status code.");

        String projectId = response.jsonPath().getString("result");
        Assert.assertNotNull(projectId, "Project creation failed! No project ID in response.");

        System.out.println("Project created with ID: " + projectId);

        // Update config file with projectId and projectName
        props.setProperty("projectId", projectId);
        props.setProperty("projectName", projectName);

        // Save updated config properties
        try (FileOutputStream output = new FileOutputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.store(output, null);
        }
    }
}
