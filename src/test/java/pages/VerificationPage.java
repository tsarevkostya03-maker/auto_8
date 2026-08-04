package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("input[name='code']");
    private final SelenideElement submitButton = $("[data-test-id='action-verify']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");
    // Пробуем разные селекторы для дашборда
    private final SelenideElement dashboard = $("[data-test-id='dashboard']");
    private final SelenideElement dashboardByClass = $(".App_dashboard, .dashboard, .main-content");

    public VerificationPage() {
        // Ждем появления поля для кода
        codeField.shouldBe(Condition.visible);
        System.out.println("Verification page loaded");
    }

    public VerificationPage setVerificationCode(String code) {
        codeField.setValue(code);
        return this;
    }

    public void clickSubmit() {
        submitButton.click();
        // Ждем загрузки после клика
        Selenide.sleep(2000);
    }

    public boolean isDashboardDisplayed() {
        // Проверяем несколько селекторов
        boolean result = dashboard.exists() || dashboardByClass.exists();
        System.out.println("Dashboard exists: " + result);
        if (!result) {
            // Логируем текущий URL для отладки
            System.out.println("Current URL: " + com.codeborne.selenide.WebDriverRunner.url());
        }
        return result;
    }

    public String getErrorMessage() {
        if (errorNotification.exists()) {
            return errorNotification.text();
        }
        return "";
    }
}