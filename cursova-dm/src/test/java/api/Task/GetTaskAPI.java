package api.Task;

import base.BaseAPI;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.qameta.allure.restassured.AllureRestAssured.*;

public class GetTaskAPI extends BaseAPI {

    @Test
    @Description("Test to retrieve a task by its ID and validate its properties.")
    public void getTaskSuccess() throws IOException {
        // Load task ID from properties file
        String taskId = loadTaskIdFromConfig();
        Assert.assertNotNull(taskId, "Task ID is not set in the config file!");

        // Send request and get task information
        Response response = getTaskRequest(taskId);

        // Attach response body to Allure report
        attachResponse(response);

        // Validate the status code and task properties
        validateTaskResponse(response, taskId);
    }

    @Step("Load task ID from config file")
    private String loadTaskIdFromConfig() throws IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        return props.getProperty("taskId");
    }

    @Step("Send request to retrieve task with ID: {taskId}")
    private Response getTaskRequest(String taskId) {
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"getTask\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"task_id\": " + taskId + "\n" +
                "    }\n" +
                "}";

        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .filter(new AllureRestAssured())  // Allure filter for request/response logging
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validate the response for task ID: {taskId}")
    private void validateTaskResponse(Response response, String taskId) {
        // Print response body for debugging
        System.out.println("Response body: " + response.getBody().asString());

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status code 200.");

        // Validate task title
        String taskTitle = response.jsonPath().getString("result.title");
        Assert.assertNotNull(taskTitle, "Task not found!");

        // Validate other task properties
        String isActive = response.jsonPath().getString("result.is_active");
        Assert.assertEquals(isActive, "1", "Task is not active!");

        String projectId = response.jsonPath().getString("result.project_id");
        Assert.assertNotNull(projectId, "Project ID for the task is missing!");

        Allure.step("Task with ID " + taskId + " was retrieved successfully with title: " + taskTitle);
    }

    @Step("Attach response body to Allure")
    @io.qameta.allure.Attachment(value = "Response", type = "application/json")
    private String attachResponse(Response response) {
        return response.getBody().asPrettyString();
    }
}
