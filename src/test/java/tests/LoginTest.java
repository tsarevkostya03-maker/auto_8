package tests;

import helpers.DbHelper;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.VerificationPage;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        DbHelper.cleanDatabase();
        loginPage = new LoginPage();
        System.out.println("Page opened. Title: " + loginPage.getTitle());
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Successful login with valid credentials")
    void shouldLoginSuccessfully() {
        System.out.println("Test: Successful login");

        loginPage.setLogin("vasya")
                .setPassword("qwerty123")
                .clickSubmit();

        // Ждем появления страницы верификации
        Selenide.sleep(3000);

        VerificationPage verificationPage = new VerificationPage();

        String code = DbHelper.getLatestAuthCode("vasya");
        System.out.println("Auth code from DB: " + code);
        assertNotNull(code, "Auth code should not be null");

        verificationPage.setVerificationCode(code)
                .clickSubmit();

        // Ждем загрузки дашборда
        Selenide.sleep(3000);

        // Проверяем URL для отладки (безопасно)
        try {
            System.out.println("Current URL: " + WebDriverRunner.url());
            String pageText = Selenide.$("body").text();
            if (pageText.length() > 100) {
                System.out.println("Page text preview: " + pageText.substring(0, Math.min(100, pageText.length())));
            } else {
                System.out.println("Page text: " + pageText);
            }
        } catch (Exception e) {
            System.out.println("Error getting page info: " + e.getMessage());
        }

        assertTrue(verificationPage.isDashboardDisplayed(), "Dashboard should be displayed");
        System.out.println("Test passed");
    }

    @Test
    @DisplayName("Login with invalid password")
    void shouldNotLoginWithInvalidPassword() {
        System.out.println("Test: Login with invalid password");

        loginPage.setLogin("vasya")
                .setPassword("wrongpassword")
                .clickSubmit();

        // Ждем появления сообщения об ошибке
        Selenide.sleep(2000);

        String error = loginPage.getErrorMessage();
        System.out.println("Error message: '" + error + "'");

        // Проверяем, что сообщение об ошибке содержит текст
        assertTrue(error != null && !error.trim().isEmpty(), "Should show error message");
        System.out.println("Test passed");
    }

    @Test
    @DisplayName("Login with non-existent user")
    void shouldNotLoginWithNonExistentUser() {
        System.out.println("Test: Login with non-existent user");

        loginPage.setLogin("nonexistent")
                .setPassword("password")
                .clickSubmit();

        // Ждем появления сообщения об ошибке
        Selenide.sleep(2000);

        String error = loginPage.getErrorMessage();
        System.out.println("Error message: '" + error + "'");

        assertTrue(error != null && !error.trim().isEmpty(), "Should show error message");
        System.out.println("Test passed");
    }
}