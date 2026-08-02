package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");
    private final SelenideElement submitButton = $("[data-test-id='action-login']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public LoginPage() {
        Selenide.open("http://localhost:9999");
        loginField.shouldBe(Condition.visible);
    }

    public LoginPage setLogin(String login) {
        loginField.setValue(login);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    public VerificationPage clickSubmit() {
        submitButton.click();
        return new VerificationPage();
    }

    public String getErrorMessage() {
        if (errorNotification.exists()) {
            return errorNotification.getText();
        }
        return "";
    }

    public String getTitle() {
        return Selenide.title();
    }
}
