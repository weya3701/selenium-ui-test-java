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

import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class DemoWebDriver extends BaseDriver implements WebAutomationTool {


    public void webdriver_demo(Step step) {
        System.out.println("Running webdriver_demo function");
    }
    private void saveResult(String result, String params) {
        this.resultQueue.put("step_result",
                Map.ofEntries(
                        entry("step", params),
                        entry("status", result)
                ));
    }

    private By getElementBy(String elementName, String selector) {
        By by = null;
        switch (selector) {
            case "xpath":
                by = By.xpath(elementName);
                break;
            case "css":
                by = By.cssSelector(elementName);
                break;
            case "link_text":
                by = By.linkText(elementName);
                break;
            case "id":
                by = By.id(elementName);
                break;
            case "tag_name":
                by = By.name(elementName);
                break;
        }

        return by;
    }

    public void switch_tab(Step step) {
        System.out.println("Running switch_tab function.");

        Set<String> wdtabs = new TreeSet<>();

        wdtabs = this.webDriver.getWindowHandles();
        this.webDriver.switchTo().window(
                wdtabs.stream().toList().get(step.tab)
        );
    }

    @Override
    public void open_website(Step step) {
        System.out.println("Running open_website");
        this.webDriver.get(step.url);
        try {
            TimeUnit.SECONDS.sleep(step.interval);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void open_new_tab(Step step) {

        System.out.println("Running open_new_tab function.");
        JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
        executor.executeScript("window.open('');");

        try {
            TimeUnit.SECONDS.sleep(step.interval);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void find_element_and_click(Step step) {
        System.out.println("Running find_element_and_click");
        try {
            By by = getElementBy(step.elementName, step.by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(ExpectedConditions.elementToBeClickable(by));
            element.click();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            TimeUnit.SECONDS.sleep(step.interval);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void find_element_and_click_without_wait(Step step) {
        System.out.println("Running find_element_and_click_without_wait");
        try {
            By by = getElementBy(step.elementName, step.by);
            this.webDriver.findElement(by).click();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            TimeUnit.SECONDS.sleep(step.interval);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void find_element_and_sendkey(Step step) {
        System.out.println("Running find_element_and_sendkey");

        try {
            // Do find_element_and_sendkey
            By by = getElementBy(step.elementName, step.by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(ExpectedConditions.elementToBeClickable(by));
            element.sendKeys(step.key);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            TimeUnit.SECONDS.sleep(step.interval);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void switch_frame(Step step) {
        try {
            if (step.frame == "parent") {
                this.webDriver.switchTo().parentFrame();
            } else {
                this.webDriver.switchTo().frame(step.frame);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void get_value_to_store(Step step) {
        System.out.println(step);
        System.out.println("Running get_value_to_store.");
    }

    public void get_regex_value_to_store(Step step) {
        System.out.println(step);
        System.out.println("Running get_regex_value_to_store.");
    }

    public void find_element_and_sendkey_from_store(Step step) {
        System.out.println(step);
        System.out.println("Running find_element_and_sendkey_from_store.");
    }

    public void validation_count(Step step) {
        System.out.println("Running validation");
        Pattern pattern = Pattern.compile(step.pattern);
        String demo_str = "<td>12345</td><td>88888</td><td>72222</td><td>28346</td>";
        Matcher matcher = pattern.matcher(demo_str);
        while (matcher.find()) {

            System.out.println("matcher.group():\t"+matcher.group(1));

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
}
