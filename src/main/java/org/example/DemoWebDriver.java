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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.Function;
import static java.util.Map.entry;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DemoWebDriver extends BaseDriver implements WebAutomationTool {

    private void saveResult(String result, String params) {
        this.resultQueue.put("step_result",
                Map.ofEntries(
                        entry("step", params),
                        entry("status", result)
                ));
    }

    public void getScreenshot(Step step) {

        String filePath = String.format("pic/%s.png", step.getDesc());
        File screenshotFile = ((TakesScreenshot) this.webDriver).getScreenshotAs(OutputType.FILE);
        Path destinationPath = Paths.get(filePath);

        screenshotFile.renameTo(destinationPath.toFile());
    }

    private void setSecondsSleep(int n) {
        try {
            TimeUnit.SECONDS.sleep(n);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            default:
                by = By.xpath(elementName);
                break;
        }
        return by;
    }

    public void switch_tab(Step step) {

        Set<String> wdtabs = new TreeSet<>();

        wdtabs = this.webDriver.getWindowHandles();
        this.webDriver.switchTo().window(
                wdtabs.stream().toList().get(step.tab)
        );
    }

    @Override
    public void open_website(Step step) {
        this.webDriver.get(step.url);
        setSecondsSleep(step.interval);
    }

    public void open_new_tab(Step step) {

        JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
        executor.executeScript("window.open('');");
        setSecondsSleep(step.interval);
    }

    @Override
    public void find_element_and_click(Step step) {
        try {
            By by = getElementBy(step.elementName, step.by);
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(condition);
            element.click();
            setSecondsSleep(step.interval);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void find_element_and_click_without_wait(Step step) {
        try {
            By by = getElementBy(step.elementName, step.by);
            this.webDriver.findElement(by).click();
            setSecondsSleep(step.interval);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void find_element_and_sendkey(Step step) {

        try {
            // Do find_element_and_sendkey
            By by = getElementBy(step.elementName, step.by);
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(condition);
            element.sendKeys(step.key);
            setSecondsSleep(step.interval);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void find_element_and_sendkey_by_js(Step step) {
        String script = String.format(
            """
            ele = document.evaluate(
                '%s',
                document,
                null,
                XPathResult.FIRST_ORDERED_NODE_TYPE,
                null
            ).singleNodeValue;
            ele.value='%s'
            """, step.elementName, step.key
        );
        script.replaceAll("'", "\"");
        script.replaceAll("\"", "'");
        JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
        executor.executeScript(script);
    }

    @Override
    public void scroll_element_intoview(Step step) {
        String script = String.format(
            """
            ele = document.evaluate(
                '%s',
                document,
                null,
                XPathResult.FIRST_ORDERED_NODE_TYPE,
                null
            ).singleNodeValue;
            ele.scrollIntoView();
            """, step.elementName
        );
        script.replaceAll("'", "\"");
        script.replaceAll("\"", "'");
        JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
        executor.executeScript(script);
    }

    @Override
    public void switch_frame(Step step) {
        try {
            boolean isEqual = step.frame.equalsIgnoreCase("parent");
            if (isEqual) {
                this.webDriver.switchTo().parentFrame();
            } else {
                this.webDriver.switchTo().frame(step.frame);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void get_value_to_store(Step step) {

        String store_key = step.storeKey;
        By selectType = getElementBy(step.elementName, step.by);
        String element_value = "";
        HashMap<String, String> storeValue = new HashMap<>();

        try {
            element_value = this.webDriver.findElement(
                    selectType
            ).getText();
            storeValue.put("key", element_value);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        this.resultQueue.put(store_key, storeValue);
    }

    @Override
    public void validation(Step step) {
        String result = step.result;
        By selectType = getElementBy(step.elementName, step.by);
        String element_value = "";

        try {
            element_value = this.webDriver.findElement(
                    selectType
            ).getText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isEqual = element_value.equalsIgnoreCase(result);
        if (isEqual) {
            System.out.println("The value is correct.");
        } else {
            System.out.println("The value is incorrect");
        }
    }

    public void get_regex_value_to_store(Step step) {
        HashMap<String, String> storeValue = new HashMap<>();
        String element_value = "";
        Pattern pattern = Pattern.compile(step.pattern);
        String html = this.webDriver.getPageSource();

        try {
            Matcher matcher = pattern.matcher(html);
            boolean isMatch = matcher.matches();
            if (isMatch) {
                element_value = matcher.group();
                storeValue.put("key", element_value);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void find_element_and_sendkey_from_store(Step step) {

        String store_key = step.storeKey;
        HashMap<String, String> storeValue = new HashMap<>();
        storeValue = (HashMap<String, String>) this.resultQueue.get(store_key);
        try {

            By by = getElementBy(step.elementName, step.by);
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(condition);
            element.sendKeys(storeValue.get("key"));
            setSecondsSleep(step.interval);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validation_count(Step step) {

        Pattern pattern = Pattern.compile(step.pattern);
        Integer total = 0;
        HashMap<String, Integer> resultPattern = new HashMap<>();
        String html = this.webDriver.getPageSource();

        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            resultPattern.put(matcher.group(1), 0);
            total = total + 1;
            System.out.println("matcher.group():\t"+matcher.group(1));

        }

        if (resultPattern.size() == total) {
            System.out.println("Successful.");
        } else {
            System.out.println("Failed.");
        }
    }

    public void find_element_and_hover(Step step) {
        By selectBy = getElementBy(step.elementName, step.by);
        try {
            this.actions.moveToElement(
                    this.webDriver.findElement(
                            selectBy
                    )
            ).perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class WebDriverBuilder {
        public static WebDriver getWebDriver(String webDriverType, String executablePath, String[] browserOptions) {
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

    public DemoWebDriver(String webDriverType, String executablePath, String[] browserOptions) {
        resultQueue = new HashMap<>();
        webDriver = WebDriverBuilder.getWebDriver(webDriverType, executablePath, browserOptions);
        actions = new Actions(webDriver);
    }
}
