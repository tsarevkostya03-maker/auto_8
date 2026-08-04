package tests;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    @BeforeAll
    static void setUpAll() {
<<<<<<< HEAD
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 15000;
        Configuration.pollingInterval = 500;
        Configuration.browserSize = "1920x1080";
        Configuration.browserPosition = "0x0";
        System.out.println("Chrome driver configured");
=======
>>>>>>> 25babdc7b21623a26ac669c572b54946864b74ba
    }
}
