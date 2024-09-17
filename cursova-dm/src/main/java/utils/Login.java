package utils;

import io.qameta.allure.Step;
import static com.codeborne.selenide.Selenide.*;

public class Login {

    private String username;
    private String password;

    // Конструктор класу Login
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Step("Opening login page")
    private void openLoginPage() {
        open("/login");  // Відкриваємо сторінку логіну
    }

    @Step("Entering username: {0}")
    private void enterUsername(String username) {
        $("#form-username").setValue(username);  // Вводимо ім'я користувача
    }

    @Step("Entering password")
    private void enterPassword(String password) {
        $("#form-password").setValue(password);  // Вводимо пароль
    }

    @Step("Submitting login form")
    private void submitLoginForm() {
        $(".form-actions > button").click();  // Клікаємо на кнопку логіну
    }

    @Step("Performing login for user: {0}")
    public void performLogin() {
        openLoginPage();           // Відкриваємо сторінку логіну
        enterUsername(username);    // Вводимо ім'я користувача
        enterPassword(password);    // Вводимо пароль
        submitLoginForm();          // Надсилаємо форму логіну
    }

    // Геттер для отримання імені користувача
    public String getUsername() {
        return username;
    }

    // Геттер для отримання пароля
    public String getPassword() {
        return password;
    }
}
