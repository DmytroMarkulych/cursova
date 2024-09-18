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
@Feature("Get Project by ID")
public class GetProjectByIdAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to retrieve project details by project ID")
    @Step("Getting project details by project ID")
    public void getProjectByIdSuccess() throws IOException {
        // Load projectId from the configuration file
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        String projectId = props.getProperty("projectId");

        // Ensure projectId is not null
        Assert.assertNotNull(projectId, "Project ID is not set in the config file!");

        // Create request body
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"getProjectById\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + projectId + "\n" +
                "    }\n" +
                "}";

        // Execute POST request to get project details
        Response response = sendPostRequest(requestBody);

        // Validate the response
        validateProjectResponse(response, projectId);
    }

    @Step("Sending POST request to get project by ID")
    private Response sendPostRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating project response")
    private void validateProjectResponse(Response response, String projectId) {
        // Log response body to console
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status and result
        Assert.assertEquals(response.getStatusCode(), 200);
        String projectName = response.jsonPath().getString("result.name");
        Assert.assertNotNull(projectName, "Project not found!");

        // Additional checks for project properties
        String isActive = response.jsonPath().getString("result.is_active");
        Assert.assertEquals(isActive, "1", "Project is not active!");

        String description = response.jsonPath().getString("result.description");
        System.out.println("Project description: " + description);
    }
}
