package tests;

import com.codeborne.selenide.Selenide;
import helpers.DbHelper;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.VerificationPage;

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

        VerificationPage verificationPage = new VerificationPage();
        
        String code = DbHelper.getLatestAuthCode("vasya");
        System.out.println("Auth code from DB: " + code);
        assertNotNull(code, "Auth code should not be null");

        verificationPage.setVerificationCode(code)
                .clickSubmit();

        verificationPage.isDashboardDisplayed();

        // Проверяем, что нет ошибки
        String error = verificationPage.getErrorMessage();
        System.out.println("Error message: " + error);
        assertTrue(error.isEmpty() || !error.contains("Неверный"),
                "Should not have error: " + error);
        System.out.println("Test passed");
    }

    @Test
    @DisplayName("Login with invalid password")
    void shouldNotLoginWithInvalidPassword() {
        System.out.println("Test: Login with invalid password");

        loginPage.setLogin("vasya")
                .setPassword("wrongpassword")
                .clickSubmit();

        String error = loginPage.getErrorMessage();
        System.out.println("Error message: " + error);
        assertTrue(error.length() > 0, "Should show error message");
        System.out.println("Test passed");
    }

    @Test
    @DisplayName("Login with non-existent user")
    void shouldNotLoginWithNonExistentUser() {
        System.out.println("Test: Login with non-existent user");

        loginPage.setLogin("nonexistent")
                .setPassword("password")
                .clickSubmit();

        String error = loginPage.getErrorMessage();
        System.out.println("Error message: " + error);
        assertTrue(error.length() > 0, "Should show error message");
        System.out.println("Test passed");
    }
}
