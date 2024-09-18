package base;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;
import utils.ApiConfig;

public class BaseAPI {

    @BeforeClass
    public void setup() {
        // General Rest Assured configuration
        RestAssured.baseURI = ApiConfig.BASE_URL;

        // Adding filters for logging requests and responses
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        // If necessary, you can set headers or authentication at the base class level
        RestAssured.authentication = RestAssured.basic(ApiConfig.APP_USERNAME, ApiConfig.APP_PASSWORD);
    }
}
