package api.Project;

import base.BaseAPI;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.qameta.allure.restassured.AllureRestAssured.*;

public class GetColumnsAPI extends BaseAPI {

    @Test
    public void getColumnsForProject() throws IOException {
        // Load project ID from properties file
        String projectId = loadProjectIdFromConfig();
        Assert.assertNotNull(projectId, "Project ID not found in config file!");

        // Send request to get columns for the project
        Response response = getColumnsRequest(projectId);

        // Attach response body to Allure report
        attachResponse(response);

        // Validate and print the columns
        validateColumnsResponse(response, projectId);
    }

    @Step("Load project ID from config file")
    private String loadProjectIdFromConfig() throws IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        return props.getProperty("projectId");
    }

    @Step("Send request to get columns for project ID: {projectId}")
    private Response getColumnsRequest(String projectId) {
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"getColumns\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": [\n" +
                "        " + projectId + "\n" +
                "    ]\n" +
                "}";

        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .filter(new AllureRestAssured())  // Allure filter for request/response logging
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validate response for project ID: {projectId}")
    private void validateColumnsResponse(Response response, String projectId) {
        // Print response body for debugging
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 status code.");

        // Extract columns from the response
        String columns = response.jsonPath().getString("result");

        // Check if columns are returned
        if (columns == null || columns.isEmpty()) {
            System.out.println("No columns found for the given project.");
        } else {
            System.out.println("Columns for project ID " + projectId + ": " + columns);
            Assert.assertNotEquals(columns, "[]", "Columns list is empty!");

            Allure.step("Columns for project ID " + projectId + ": " + columns);
        }
    }

    @Step("Attach response body to Allure")
    @io.qameta.allure.Attachment(value = "Response", type = "application/json")
    private String attachResponse(Response response) {
        return response.getBody().asPrettyString();
    }
}
