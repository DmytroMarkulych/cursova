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
        // Виконуємо логін і перевіряємо, що користувач потрапляє на дашборд
        login.performLogin();
        $("h1").shouldHave(text("Dashboard"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Open Project")
    @Description("Test to open a specific project from the dashboard")
    @Step("Opening project: {0}")
    public void openProject() {
        // Припустимо, що 'projectName' встановлено в BaseUI або передано динамічно
        String projectName = this.projectName;

        // Відкриваємо конкретний проект з дашборду, клікаючи на його посилання
        $x("//a[text()='" + projectName + "']").shouldBe(visible).click();

        // Перевіряємо, що проект відкрито і відображається правильно
        $("h1").shouldHave(text(projectName));
    }

    @Test(dependsOnMethods = "openProject")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Add New Task")
    @Description("Test to create a new task in the project")
    @Step("Adding a new task to the project: {0}")
    public void addNewTask() {
        // Очікуємо на видимість елемента та клікаємо для створення нового завдання
        $x("//*[@id='board']/tbody/tr[1]/th[1]/div[2]/div/a/i").shouldBe(visible).click();

        // Перевіряємо, що назва проекту коректно відображена в заголовку модального вікна
        $("h2").shouldHave(text(projectName));

        // Заповнюємо всі необхідні поля форми завдання
        $("#form-title").shouldBe(visible).setValue(taskName);
        $("#modal-content .text-editor-write-mode textarea").shouldBe(visible).setValue("Task Description");

        // Вибір додаткових параметрів
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

        // Зберігаємо завдання
        $("#modal-content .task-form-bottom button").shouldBe(visible).click();
      //  $("#modal-content .task-form-bottom a").shouldBe(visible);
    }
}
