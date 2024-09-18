package ui.LoginTests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

@Epic("Login Functionality")
@Feature("Valid Login")
public class LoginTestsValid {

    private Login login;

    @BeforeClass
    @Step("Setup test environment and load configuration")
    public void setUp() throws IOException {
        // Load configuration from file
        Properties properties = new Properties();
        FileInputStream fileInput = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties");
        properties.load(fileInput);
        Configuration.baseUrl = properties.getProperty("baseUrl", "http://127.0.0.1"); // Default value

        // Configure the browser
        Configuration.browser = "chrome";
        //Configuration.headless = true;
        Configuration.browserSize = "1920x1080";

        // Initialize the Login class by passing parameters through the constructor
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        login = new Login(username, password);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test to verify successful login with valid credentials")
    @Story("Valid Login Test")
    public void validLoginTest() {
        stepLogin(login.getUsername(), login.getPassword());
        login.performLogin();  // Call the login method

        // Additional verification after successful login
        $("h1").shouldHave(text("Dashboard"));  // Verify that the dashboard is displayed
    }

    @Step("Perform login with username: {0} and password: {1}")
    public void stepLogin(String username, String password) {
        System.out.println("Logging in with username: " + username + " and password: " + password);
    }
}
