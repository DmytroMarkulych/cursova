package ui.LoginTests;

import base.BaseUI;
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
@Feature("Invalid Login")
public class LoginTestsInvalid extends BaseUI  {

    private Login login;

    @BeforeClass
    @Step("Setup test environment and load configuration")
    public void setUp() throws IOException {
        // Завантажуємо конфігурацію з файлу
        Properties properties = new Properties();
        FileInputStream fileInput = new FileInputStream("/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties");
        properties.load(fileInput);
        Configuration.baseUrl = properties.getProperty("baseUrl", "http://127.0.0.1"); // Значення за замовчуванням

        // Налаштовуємо браузер
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.browserSize = "1920x1080";
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify login failure with an invalid username")
    @Story("Invalid Username Test")
    public void invalidUsernameTest() {
        // Ініціалізуємо екземпляр класу Login з неправильним Username
        Login invalidLogin = new Login("invalid_user", "admin");

        stepLogin("invalid_user", "admin");
        invalidLogin.performLogin();  // Викликаємо метод логіну

        // Перевіряємо, що відображається повідомлення про помилку
        $(".alert").shouldHave(text("Bad username or password"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify login failure with an invalid password")
    @Story("Invalid Password Test")
    public void invalidPasswordTest() {
        // Ініціалізуємо екземпляр класу Login з неправильним Password
        Login invalidLogin = new Login("admin", "invalid_user");

        stepLogin("admin", "invalid_user");
        invalidLogin.performLogin();  // Викликаємо метод логіну

        // Перевіряємо, що відображається повідомлення про помилку
        $(".alert").shouldHave(text("Bad username or password"));
    }

    @Step("Perform login with username: {0} and password: {1}")
    public void stepLogin(String username, String password) {
        System.out.println("Logging in with username: " + username + " and password: " + password);
    }
}
