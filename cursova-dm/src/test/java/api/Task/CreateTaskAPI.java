package api.Task;

import base.BaseAPI;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.qameta.allure.restassured.AllureRestAssured.*;

public class CreateTaskAPI extends BaseAPI {

    @Test
    public void createTaskWithAllAttributes() throws IOException {
        // Load properties from config file
        Properties props = loadProperties();

        // Get necessary values from properties
        String projectId = props.getProperty("projectId");
        String ownerId = props.getProperty("userId");
        String columnId = props.getProperty("columns");
        Assert.assertNotNull(projectId, "Project ID not found in config file!");
        Assert.assertNotNull(ownerId, "Owner ID not found in config file!");

        // Define additional task details
        String title = "Test Task";
        String colorId = "green"; // Optional
        String description = "This is a test task created via API.";
        String dueDate = "2024-07-01 14:25"; // Optional
        String categoryId = "1";  // Optional Category ID
        String priority = "3";    // Optional Priority
        String dateStarted = "2024-06-01 10:00";  // Optional start date
        String recurrenceStatus = "1";
        String recurrenceTrigger = "0";
        String recurrenceFactor = "0";
        String recurrenceTimeframe = "0";
        String recurrenceBasedate = "0";
        String score = "5";  // Optional Score

        // Send request to create the task
        Response response = createTaskRequest(
                ownerId, projectId, title, description, columnId, colorId,
                dueDate, categoryId, priority, dateStarted, recurrenceStatus,
                recurrenceTrigger, recurrenceFactor, recurrenceTimeframe,
                recurrenceBasedate, score);

        // Attach response body to Allure report
        attachResponse(response);

        // Validate result and print task ID
        validateAndStoreTaskId(response, props);
    }

    @Step("Load properties from config file")
    private Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        return props;
    }

    @Step("Create task with provided details")
    private Response createTaskRequest(String ownerId, String projectId, String title, String description,
                                       String columnId, String colorId, String dueDate, String categoryId,
                                       String priority, String dateStarted, String recurrenceStatus,
                                       String recurrenceTrigger, String recurrenceFactor, String recurrenceTimeframe,
                                       String recurrenceBasedate, String score) {
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createTask\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"owner_id\": " + ownerId + ",\n" +
                "        \"creator_id\": "+ ownerId + ",\n" +
                "        \"date_due\": \"" + dueDate + "\",\n" +
                "        \"description\": \"" + description + "\",\n" +
                "        \"category_id\": " + categoryId + ",\n" +
                "        \"score\": " + score + ",\n" +
                "        \"title\": \"" + title + "\",\n" +
                "        \"project_id\": " + projectId + ",\n" +
                "        \"color_id\": \"" + colorId + "\",\n" +
                "        \"column_id\": " + columnId + ",\n" +
                "        \"recurrence_status\": " + recurrenceStatus + ",\n" +
                "        \"recurrence_trigger\": " + recurrenceTrigger + ",\n" +
                "        \"recurrence_factor\": " + recurrenceFactor + ",\n" +
                "        \"recurrence_timeframe\": " + recurrenceTimeframe + ",\n" +
                "        \"recurrence_basedate\": " + recurrenceBasedate + ",\n" +
                "        \"date_started\": \"" + dateStarted + "\",\n" +
                "        \"priority\": " + priority + "\n" +
                "    }\n" +
                "}";

        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD) // Application credentials
                .filter(new AllureRestAssured())  // Allure request/response logging
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validate and store task ID in properties")
    private void validateAndStoreTaskId(Response response, Properties props) throws IOException {
        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200 status code.");

        // Extract and validate task ID from response
        Integer taskId = response.jsonPath().getInt("result");
        Assert.assertNotNull(taskId, "Failed to create task.");
        System.out.println("Task created with ID: " + taskId);

        Allure.step("Task created with ID: " + taskId);

        // Update taskId in properties file for future use
        props.setProperty("taskId", taskId.toString());
        try (FileOutputStream output = new FileOutputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.store(output, null);
        }
    }

    @Step("Attach response body to Allure")
    @io.qameta.allure.Attachment(value = "Response", type = "application/json")
    private String attachResponse(Response response) {
        return response.getBody().asPrettyString();
    }
}
