package base;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;
import utils.ApiConfig;

public class BaseAPI {

    @BeforeClass
    public void setup() {
        // Загальна конфігурація Rest Assured
        RestAssured.baseURI = ApiConfig.BASE_URL;

        // Додавання фільтрів для логування запитів і відповідей
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        // Якщо потрібно, можна налаштувати заголовки або аутентифікацію на рівні базового класу
        RestAssured.authentication = RestAssured.basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD);
    }
}
