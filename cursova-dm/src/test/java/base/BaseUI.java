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
    @Parameters({"browser", "headless"}) // Added parameters for browser and headless mode
    public void setUp(String browser, String headless) throws IOException {
        // Load configuration
        loadConfiguration();
        // Set up browser
        configureBrowser(browser, headless);
        // Initialize login
        initializeLogin();
    }

    private void loadConfiguration() throws IOException {
        properties = new Properties();
        // Load configuration file
        try (FileInputStream fileInput = new FileInputStream(System.getProperty("config.file", "/home/dmytro/IdeaProjects/cursova/cursova-dm/config.properties"))) {
            properties.load(fileInput);
        }

        // Initialize common variables
        username = properties.getProperty("username", "username");
        password = properties.getProperty("password", "password");
        projectId = properties.getProperty("projectId", "projectId");
        projectName = properties.getProperty("projectName", "projectName");
        taskName = properties.getProperty("taskName", "taskName");
    }

    private void configureBrowser(String browser, String headless) {
        // Set Selenide configuration
        Configuration.baseUrl = properties.getProperty("baseUrl", "http://127.0.0.1");
        Configuration.browser = browser;  // Use parameter for browser
        Configuration.browserSize = properties.getProperty("browserSize", "1920x1080");
        Configuration.headless = Boolean.parseBoolean(headless);  // Use parameter for headless mode
    }

    private void initializeLogin() {
        // Initialize login mechanism
        login = new Login(username, password);
    }

    @AfterClass
    public void tearDown() {
        // Close the browser after tests
        closeWebDriver();
    }
}
