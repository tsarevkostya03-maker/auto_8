package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("input[name='code']");
    private final SelenideElement submitButton = $("[data-test-id='action-verify']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public VerificationPage setVerificationCode(String code) {
        codeField.setValue(code);
        return this;
    }

    public void clickSubmit() {
        submitButton.click();
        Selenide.sleep(2000);
    }

    public String getErrorMessage() {
        if (errorNotification.exists()) {
            return errorNotification.getText();
        }
        return "";
    }
}