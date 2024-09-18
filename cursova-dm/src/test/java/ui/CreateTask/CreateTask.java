package ui.CreateTask;

import base.BaseUI;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Epic("Task Management")
@Feature("Create Task")
public class CreateTask extends BaseUI {

    @BeforeClass
    @Step("Setup: Logging in and verifying dashboard")
    public void setup() {
        // Perform login and verify the user lands on the dashboard
        login.performLogin();
        $("h1").shouldHave(text("Dashboard"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Open Project")
    @Description("Test to open a specific project from the dashboard")
    @Step("Opening project: {0}")
    public void openProject() {
        // Assume 'projectName' is set in BaseUI or passed dynamically
        String projectName = this.projectName;

        // Open a specific project from the dashboard by clicking on its link
        $x("//a[text()='" + projectName + "']").shouldBe(visible).click();

        // Verify the project is opened and displayed correctly
        $("h1").shouldHave(text(projectName));
    }

    @Test(dependsOnMethods = "openProject")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Add New Task")
    @Description("Test to create a new task in the project")
    @Step("Adding a new task to the project: {0}")
    public void addNewTask() {
        // Wait for the element to be visible and click to create a new task
        $x("//*[@id='board']/tbody/tr[1]/th[1]/div[2]/div/a/i").shouldBe(visible).click();

        // Verify that the project name is correctly displayed in the modal window's header
        $("h2").shouldHave(text(projectName));

        // Fill in all the necessary fields for the task form
        $("#form-title").shouldBe(visible).setValue(taskName);
        $("#modal-content .text-editor-write-mode textarea").shouldBe(visible).setValue("Task Description");

        // Select additional parameters
        $("#select2-form-color_id-container div").shouldBe(visible).click();
        $("#form-owner_id").shouldBe(visible).selectOption(1);
        $("#form-column_id").shouldBe(visible).selectOption(1);
        $("#form-priority").shouldBe(visible).selectOption(1);
        $("#form-date_due").shouldBe(visible).setValue("2024-07-01 14:25");
        $("#form-date_started").shouldBe(visible).setValue("2024-06-01 09:00");
        $("#form-time_estimated").shouldBe(visible).setValue("5");
        $("#form-time_spent").shouldBe(visible).setValue("2");
        $("#form-score").shouldBe(visible).setValue("10");
        $("#form-reference").shouldBe(visible).setValue("Task Reference");

        // Save the task
        $("#modal-content .task-form-bottom button").shouldBe(visible).click();
        //  $("#modal-content .task-form-bottom a").shouldBe(visible);
    }
}
