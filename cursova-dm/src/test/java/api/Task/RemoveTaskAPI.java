package api.Task;

import base.BaseAPI;
import io.qameta.allure.Attachment;
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

public class RemoveTaskAPI extends BaseAPI {

    @Test
    @Description("This test removes a task by task ID")
    public void removeTaskSuccess() throws IOException {
        Properties props = loadProperties();

        // Get task ID from the properties file
        String taskId = props.getProperty("taskId");
        Assert.assertNotNull(taskId, "Task ID not found in config file!");

        // Send request to remove the task
        Response response = removeTaskRequest(taskId);

        // Attach response to Allure
        attachResponse(response);

        // Validate result in the response
        boolean result = response.jsonPath().getBoolean("result");
        Assert.assertTrue(result, "Task removal failed!");

        Allure.step("Task with ID " + taskId + " has been successfully removed.");
    }

    @Step("Send request to remove task with ID: {taskId}")
    private Response removeTaskRequest(String taskId) {
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeTask\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"task_id\": " + taskId + "\n" +
                "    }\n" +
                "}";

        // Send POST request to remove the task with AllureRestAssured logging enabled
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD) // Application credentials
                .filter(new AllureRestAssured()) // Allure filter for logging request/response
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Load properties from config file")
    private Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        return props;
    }

    @Attachment(value = "Response", type = "application/json", fileExtension = "json")
    private String attachResponse(Response response) {
        return response.getBody().asPrettyString();
    }
}
