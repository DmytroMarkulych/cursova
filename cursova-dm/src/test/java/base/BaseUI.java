package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Parameters;
import utils.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseUI {

    protected Properties properties;
    protected String username;
    protected String password;
    protected String projectId;
    protected String projectName;
    protected String taskName;
    protected Login login;

    @BeforeClass
    @Parameters({"browser", "headless"}) // Додав параметри браузера та headless-режиму
    public void setUp(String browser, String headless) throws IOException {
        // Завантаження конфігурації
        loadConfiguration();
        // Налаштування браузера
        configureBrowser(browser, headless);
        // Ініціалізація логіну
        initializeLogin();
    }

    private void loadConfiguration() throws IOException {
        properties = new Properties();
        // Завантаження конфігураційного файлу
        try (FileInputStream fileInput = new FileInputStream(System.getProperty("config.file", "/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties"))) {
            properties.load(fileInput);
        }

        // Ініціалізуємо загальні змінні
        username = properties.getProperty("username", "username");
        password = properties.getProperty("password", "password");
        projectId = properties.getProperty("projectId", "projectId");
        projectName = properties.getProperty("projectName", "projectName");
        taskName = properties.getProperty("taskName", "taskName");
    }

    private void configureBrowser(String browser, String headless) {
        // Налаштування конфігурації Selenide
        Configuration.baseUrl = properties.getProperty("baseUrl", "http://127.0.0.1");
        Configuration.browser = browser;  // Використовуємо параметр для браузера
        Configuration.browserSize = properties.getProperty("browserSize", "1920x1080");
        Configuration.headless = Boolean.parseBoolean(headless);  // Використовуємо параметр для headless-режиму
    }

    private void initializeLogin() {
        // Ініціалізація логін-механізму
        login = new Login(username, password);
    }

    @AfterClass
    public void tearDown() {
        // Закриття браузера після завершення тестів
        closeWebDriver();
    }
}
