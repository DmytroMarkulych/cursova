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
        // Виконуємо логін і перевіряємо, що користувач потрапляє на дашборд
        login.performLogin();
        $("h1").shouldHave(text("Dashboard"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Open task from the dashboard")
    @Step("Opening task: {0}")
    public void openTask() {
        // Припускаємо, що 'taskName' встановлено в BaseUI або передано динамічно
        String taskName = this.taskName;

        // Відкриваємо конкретне завдання з дашборду, клікаючи на його посилання
        $x("//a[text()='" + taskName + "']").shouldBe(visible).click();

        // Перевіряємо, що відображається дошка завдання
        $("#task-summary > h2").shouldHave(text(taskName));
    }

    @Test(dependsOnMethods = "openTask")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add a comment to the task")
    @Step("Adding a comment to the task")
    public void popUpCommentElements() {
        String comment = "New comment " + System.currentTimeMillis();

        // Відкриваємо секцію коментарів у вигляді завдання
        $x("//*[@id=\"task-view\"]/div[2]/details[6]").shouldBe(visible).click();

        // Вводимо коментар у текстове поле
        $x("//*[@id=\"comments\"]/form/div[1]/div/div[2]/textarea").shouldBe(visible).setValue(comment);

        // Клікаємо на кнопку для публікації коментаря
        $x("//*[@id=\"comments\"]/form/div[2]/div/button").shouldBe(visible).click();

        // Перевіряємо, що коментар було додано
        $("#comments").shouldHave(text(comment));
    }
}
