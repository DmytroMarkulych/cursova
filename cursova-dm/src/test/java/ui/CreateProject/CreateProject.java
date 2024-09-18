package ui.CreateProject;

import base.BaseUI;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.text;

@Epic("Project Management")
@Feature("Create Project")
public class CreateProject extends BaseUI {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify successful login and dashboard loading")
    @Step("Logging in and verifying dashboard")
    public void validLoginTest() {
        login.performLogin();
        $("h1").shouldHave(text("Dashboard"));  // Verify that the dashboard is displayed
    }

    @Test(dependsOnMethods = "validLoginTest")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to open the Create Project form")
    @Step("Click on 'New personal project' link")
    public void clickOnCreateProject() {
        clickNewProjectLink();  // Use method to click on "New project" link
        $("h2").shouldHave(text("New personal project"));  // Verify that "New project" header is displayed
    }

    @Test(dependsOnMethods = "clickOnCreateProject")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test to create a new project with valid data")
    @Step("Creating a new project with valid data")
    public void createNewProjectWithValidData() {
        $("#form-name").setValue("Test Project");
        $("#form-identifier").setValue("NewProjectID" + System.currentTimeMillis());
        $("#project-creation-form label:nth-child(9) input[type=checkbox]").click();
        $("#form-task_limit").setValue("2");
        $("#project-creation-form .js-submit-buttons-rendered button").click();
        $("h1").shouldHave(text("Test Project"));  // Verify successful project creation with the name
    }

    @Test(dependsOnMethods = "clickOnCreateProject")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that the project identifier must be unique")
    @Step("Creating a project with a non-unique identifier")
    public void createProjectWithUniqueIDName() {
        navigateToDashboard();  // Navigate back to the dashboard
        clickNewProjectLink();
        $("#form-name").setValue("Test Project");
        $("#form-identifier").setValue("NewProjectID");
        $("#project-creation-form .js-submit-buttons-rendered button").click();
        $(".form-errors").shouldHave(text("The identifier must be unique"));
        clickCancelButton();
    }

    @Test(dependsOnMethods = "clickOnCreateProject")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that a project cannot be created without a name")
    @Step("Attempting to create a project without a name")
    public void createProjectWithoutName() {
        navigateToDashboard();
        clickNewProjectLink();
        $("#form-identifier").setValue("NewProjectIDSecond");
        $("#project-creation-form .js-submit-buttons-rendered button").click();
        $(".form-errors").shouldHave(text("The project name is required"));
        clickCancelButton();
    }

    @Test(dependsOnMethods = "createProjectWithoutName")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that the project identifier must be alphanumeric")
    @Step("Attempting to create a project with an invalid identifier")
    public void createProjectWithInvalidIdentifier() {
        clickNewProjectLink();
        $("#form-name").setValue("Project with invalid identifier");
        $("#form-identifier").setValue("New_projectID_Second");
        $("#project-creation-form .js-submit-buttons-rendered button").click();
        $(".form-errors").shouldHave(text("This value must be alphanumeric"));
        clickCancelButton();
    }

    // Helper methods with Allure annotations
    @Step("Clicking on 'New personal project' link")
    private void clickNewProjectLink() {
        $("#main ul li:first-child a").shouldHave(text("New personal project")).click();
    }

    @Step("Navigating back to dashboard")
    private void navigateToDashboard() {
        $(".title-container .logo a").click();
    }

    @Step("Clicking the cancel button")
    private void clickCancelButton() {
        $(".js-submit-buttons-rendered a").click();
    }
}
