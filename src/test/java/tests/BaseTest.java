package tests;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));
        Configuration.timeout = 10000; // 10 секунд на ожидание элементов
        Configuration.pollingInterval = 100; // Проверка каждые 100 мс
        Configuration.browserSize = "1920x1080";
        System.out.println("Chrome driver configured, headless: " + Configuration.headless);
    }
}
