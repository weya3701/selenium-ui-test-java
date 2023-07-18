package org.example;

import lombok.Getter;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Map.entry;

public class DemoWebDriver extends BaseDriver implements WebAutomationTool {
    @Override
    public void open_website(String url) {
//        this.webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String params = "";
        try {

            this.webDriver.get(url);
            this.resultQueue.put("step_result",
                    Map.ofEntries(
                            entry("step", params),
                            entry("status", "SUCCESSFUL")
                    ));
        } catch (Exception ex) {
            this.resultQueue.put("step_result",
                    Map.ofEntries(
                            entry("step", params),
                            entry("status", "FAIL")
                    ));
        }

    }

    static class WebDriverBuilder {
        public static WebDriver getWebDriver(String webDriverType, String executablePath, String... browserOptions) {
            WebDriver driver;
            switch (webDriverType) {
                case "Chrome":
                    System.setProperty("webdriver.chrome.driver", executablePath);
                    ChromeOptions co = new ChromeOptions();
                    co.addArguments(browserOptions);
                    co.addArguments();
                    driver = new ChromeDriver(co);
                    break;
                case "Firefox":
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.addArguments(browserOptions);
                    fo.addArguments();
                    driver = new FirefoxDriver(fo);
                    break;
                case "Edge":
                    EdgeOptions eo = new EdgeOptions();
                    driver = new EdgeDriver(eo);
                    break;
                case "Safari":
                    SafariOptions so = new SafariOptions();
                    driver = new SafariDriver(so);
                    break;
                default:
                    driver = null;
            }
            return driver;
        }
    }

    @Getter
    private WebDriver webDriver;

    @Getter
    private Actions actions;

    public DemoWebDriver(String webDriverType, String executablePath) {
        resultQueue = new HashMap<>();
        webDriver = WebDriverBuilder.getWebDriver(webDriverType, executablePath);
        actions = new Actions(webDriver);
    }

    public void setCookies(String cookies) {
        String[] cookieList = cookies.split(";");
        for (String cookie : cookieList) {
            String[] keyValue = cookie.split("=");
            this.webDriver.manage().addCookie(new Cookie(keyValue[0], keyValue[1]));
        }
    }

}
