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
@Feature("Remove Project")
public class RemoveProjectAPI extends BaseAPI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to successfully remove an existing project")
    @Step("Removing project by ID")
    public void removeProjectSuccess() throws IOException {
        // Завантажуємо projectId з конфігураційного файлу
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties")) {
            props.load(input);
        }
        String projectId = props.getProperty("projectId");

        // Перевіряємо, що projectId не порожній
        Assert.assertNotNull(projectId, "Project ID is not set in the config file!");

        // Створюємо тіло запиту
        String requestBody = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": \"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        // Виконуємо POST запит для видалення проекту
        Response response = sendPostRequest(requestBody);

        // Валідуємо відповідь
        validateProjectRemoval(response, projectId);
    }

    @Step("Sending POST request to remove project")
    private Response sendPostRequest(String requestBody) {
        return given()
                .auth()
                .basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD)  // Application credentials
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
    }

    @Step("Validating project removal response")
    private void validateProjectRemoval(Response response, String projectId) {
        // Виводимо response body в консоль
        System.out.println("Response body: " + response.getBody().asString());

        // Перевірка статусу та результату
        Assert.assertEquals(response.getStatusCode(), 200);
        Boolean result = response.jsonPath().getBoolean("result");
        Assert.assertTrue(result, "Expected project to be removed but got failure!");

        System.out.println("Project with ID " + projectId + " was successfully removed.");
    }
}
