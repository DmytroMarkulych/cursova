package ui.AddCommentForTask;

import base.BaseUI;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Epic("Task Management")
@Feature("Add Comment to Task")
public class AddCommentForTask extends BaseUI {

    @BeforeClass
    @Step("Setup: Logging in and verifying dashboard")
    public void setup() {
        // Perform login and verify the user lands on the dashboard
        login.performLogin();
        $("h1").shouldHave(text("Dashboard"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Open task from the dashboard")
    @Step("Opening task: {0}")
    public void openTask() {
        // Assume 'taskName' is set in BaseUI or passed dynamically
        String taskName = this.taskName;

        // Open a specific task from the dashboard by clicking on its link
        $x("//a[text()='" + taskName + "']").shouldBe(visible).click();

        // Verify that the task board is displayed
        $("#task-summary > h2").shouldHave(text(taskName));
    }

    @Test(dependsOnMethods = "openTask")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add a comment to the task")
    @Step("Adding a comment to the task")
    public void popUpCommentElements() {
        String comment = "New comment " + System.currentTimeMillis();

        // Open the comment section in the task view
        $x("//*[@id=\"task-view\"]/div[2]/details[6]").shouldBe(visible).click();

        // Enter the comment in the text area
        $x("//*[@id=\"comments\"]/form/div[1]/div/div[2]/textarea").shouldBe(visible).setValue(comment);

        // Click the button to publish the comment
        $x("//*[@id=\"comments\"]/form/div[2]/div/button").shouldBe(visible).click();

        // Verify that the comment has been added
        $("#comments").shouldHave(text(comment));
    }
}
