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
                "    \"method\": \"getProjectById\",\n" +
                "    \"id\": " + System.currentTimeMillis() + ",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + projectId + "\n" +
                "    }\n" +
                "}";

        // Виконуємо POST запит для отримання інформації про проект
        Response response = sendPostRequest(requestBody);

        // Валідуємо відповідь
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
        // Виводимо response body в консоль
        System.out.println("Response body: " + response.getBody().asString());

        // Перевірка статусу та результату
        Assert.assertEquals(response.getStatusCode(), 200);
        String projectName = response.jsonPath().getString("result.name");
        Assert.assertNotNull(projectName, "Project not found!");

        // Додаткові перевірки для властивостей проекту
        String isActive = response.jsonPath().getString("result.is_active");
        Assert.assertEquals(isActive, "1", "Project is not active!");

        String description = response.jsonPath().getString("result.description");
        System.out.println("Project description: " + description);
    }
}
