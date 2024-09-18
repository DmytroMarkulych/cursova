package ui.CloseTask;

import base.BaseUI;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Epic("Task Management")
@Feature("Close Task")
public class CloseTask extends BaseUI {

    @BeforeClass
    @Step("Setup: Logging in and verifying dashboard")
    public void setup() {
        // Perform login and verify the user lands on the dashboard
        login.performLogin();
        $("h1").shouldHave(text("Dashboard"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Open task from the project board")
    @Step("Opening task: {0}")
    public void openTask() {
        // Assume 'taskName' is set in BaseUI or passed dynamically
        String nameTask = this.taskName;

        // Open a specific task from the dashboard by clicking on its link
        $x("//a[text()='" + nameTask + "']").shouldBe(visible).click();

        // Verify that the project board is displayed
        $("#task-summary > h2").shouldHave(text(taskName));
    }

    @Test(dependsOnMethods = "openTask")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Click to close the task")
    @Step("Clicking on 'Close this task'")
    public void clickOnCloseThisTask(){
        $x("//*[@id=\"task-view\"]/div[1]/ul[2]/li[14]/a").click();
    }

    @Test(dependsOnMethods = "clickOnCloseThisTask")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify and confirm closing task popup elements")
    @Step("Verifying and confirming pop-up elements to close the task")
    public void popUpElements(){
        $("#modal-content > div.page-header").shouldBe(visible);
        $("#modal-content > div.confirm > p").shouldBe(visible);
        $("#modal-content .confirm a").shouldBe(visible);
        $("#modal-confirm-button").shouldBe(visible).click();
    }
}
