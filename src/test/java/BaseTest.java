import com.codeborne.selenide.Configuration;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;

public class BaseTest {

    @BeforeSuite
    public void setup(){
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
    }

    @AfterSuite
    public void tearDown(){
        closeWebDriver();
    }
}
