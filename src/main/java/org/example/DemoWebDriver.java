package org.example;

import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class DemoWebDriver extends BaseDriver implements WebAutomationTool {

    private void saveResult(String result, String params) {
        this.resultQueue.put("step_result",
                Map.ofEntries(
                        entry("step", params),
                        entry("status", result)
                ));
    }
    @Override
    public void openWebsite(String url) {
        String params = "";
        try {
            this.webDriver.get(url);
            this.saveResult("SUCCESSFUL", params);
        } catch (Exception ex) {
            this.saveResult("FAIL", params);
        }
    }

    @Override
    public void findElementAndClick(String selector, int waitSeconds) {
        String params = "";
        By by;
        switch (selector){
            case "xpath":
                 by = By.xpath("");
                break;
            case "css":
                by = By.cssSelector("");
                break;
            case "link_text":
                by = By.linkText("");
                break;
            default:
                by = By.xpath("");
        }
        WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(waitSeconds).toSeconds())
                .until(ExpectedConditions.elementToBeClickable(by));
        element.click();
        this.saveResult("SUCCESSFUL", params);
    }

    public void open_new_tab() {
        // do open new tab
        System.out.println("Running open_new_tab function.");
    }

    public void get_value_to_store() {
        System.out.println("Running get_value_to_store.");
    }

    public void get_regex_value_to_store() {
        System.out.println("Running get_regex_value_to_store.");
    }

    public void find_element_and_sendkey_from_store() {
        System.out.println("Running find_element_and_sendkey_from_store.");
    }

    public void validation() {
        System.out.println("Running validation");
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
