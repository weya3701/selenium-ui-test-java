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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.Function;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DemoWebDriver extends BaseDriver implements WebAutomationTool {

    public void saveResult(String step, String status) {
        HashMap<String, String> stepStatus = new HashMap<String, String>();
        stepStatus.put("Step", step);
        stepStatus.put("Status", status);
        this.stepsResult.add(stepStatus);
    }

    public List<HashMap<String, String>> getResult() {
        return this.stepsResult;
    }

    public String getTaskStatus() {

        String rsp = this.failed;

        if (this.taskStepStatus) {
            rsp = this.successful;
        }
        return rsp;
    }

    private void setTaskStepStatus(boolean status) {
        this.taskStepStatus = this.taskStepStatus && status;
    }

    public void getScreenshot(String fp, Step step) {

        String filePath = String.format(fp+"%s.png", step.getDesc());
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

    public String switch_tab(Step step) {

        Set<String> wdtabs = new TreeSet<>();
        wdtabs = this.webDriver.getWindowHandles();
        this.webDriver.switchTo().window(
                wdtabs.stream().toList().get(step.tab)
        );
        setTaskStepStatus(true);
        return this.successful;
    }

    @Override
    public String open_website(Step step) {

        this.webDriver.get(step.url);
        setSecondsSleep(step.interval);
        setTaskStepStatus(true);
        return this.successful;
    }

    public String open_new_tab(Step step) {

        JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
        executor.executeScript("window.open('');");
        setSecondsSleep(step.interval);
        setTaskStepStatus(true);
        return this.successful;
    }

    @Override
    public String find_element_and_click(Step step) {

        String rsp = this.successful;
        try {
            By by = getElementBy(step.elementName, step.by);
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(condition);
            element.click();
            setSecondsSleep(step.interval);
            setTaskStepStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        return rsp;
    }

    @Override
    public String find_element_and_click_without_wait(Step step) {
        String rsp = this.successful;
        try {
            By by = getElementBy(step.elementName, step.by);
            this.webDriver.findElement(by).click();
            setSecondsSleep(step.interval);
            setTaskStepStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        return rsp;
    }

    @Override
    public String find_element_and_sendkey(Step step) {
        String rsp = this.successful;
        try {
            // Do find_element_and_sendkey
            By by = getElementBy(step.elementName, step.by);
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(by);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(condition);
            element.sendKeys(step.key);
            setSecondsSleep(step.interval);
            setTaskStepStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        return rsp;
    }

    @Override
    public String find_element_and_sendkey_by_js(Step step) {
        String rsp = this.successful;
        try {
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
            setTaskStepStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }

        return rsp;
    }

    @Override
    public String scroll_element_intoview(Step step) {
        String rsp = this.successful;
        try {
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
            setTaskStepStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }

        return rsp;
    }

    @Override
    public String switch_frame(Step step) {
        String rsp = this.successful;
        try {
            boolean isEqual = step.frame.equalsIgnoreCase("parent");
            if (isEqual) {
                this.webDriver.switchTo().parentFrame();
            } else {
                this.webDriver.switchTo().frame(step.frame);
            }
            setTaskStepStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        return rsp;
    }

    @Override
    public String get_value_to_store(Step step) {
        String rsp = this.successful;
        String store_key = step.storeKey;
        By selectType = getElementBy(step.elementName, step.by);
        String element_value = "";
        HashMap<String, String> storeValue = new HashMap<>();

        try {
            element_value = this.webDriver.findElement(
                    selectType
            ).getText();
            storeValue.put("key", element_value);
            setTaskStepStatus(true);
        } catch(Exception ex) {
            rsp = this.failed;
            ex.printStackTrace();
            setTaskStepStatus(false);
        }
        this.resultQueue.put(store_key, storeValue);
        return rsp;
    }

    @Override
    public String validation(Step step) {
        String rsp = this.successful;
        String result = step.result;
        By selectType = getElementBy(step.elementName, step.by);
        String element_value = "";

        try {
            element_value = this.webDriver.findElement(
                    selectType
            ).getText();
            setTaskStepStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        } finally {

            boolean isEqual = element_value.equalsIgnoreCase(result);
            if (isEqual) {
                rsp = this.successful;
                System.out.println(this.successful);
            } else {
                rsp = this.failed;
                System.out.println(this.failed);
            }
        }
        return rsp;
    }

    @Override
    public String get_regex_value_to_store(Step step) {
        String rsp = this.successful;
        String store_key = step.storeKey;
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
            setTaskStepStatus(true);
        } catch(Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        this.resultQueue.put(store_key, storeValue);
        return rsp;
    }

    @Override
    public String find_element_and_sendkey_from_store(Step step) {
        String rsp = this.successful;
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
            setTaskStepStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }

        return rsp;
    }

    @Override
    public String validation_count(Step step) {

        String rsp = this.successful;
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
            System.out.println(this.successful);
            setTaskStepStatus(true);
        } else {
            rsp = this.failed;
            System.out.println(this.failed);
            setTaskStepStatus(false);
        }

        return rsp;
    }

    @Override
    public String find_element_and_hover(Step step) {
        String rsp = this.successful;
        By selectBy = getElementBy(step.elementName, step.by);
        try {
            this.actions.moveToElement(
                    this.webDriver.findElement(
                            selectBy
                    )
            ).perform();
            setTaskStepStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }

        return rsp;
    }

    public void closeWebdriver() {
        try {
            this.webDriver.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quitWebdriver() {
        try {
            this.webDriver.quit();
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
                    System.setProperty("webdriver.firefox.dirver", executablePath);
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.addArguments(browserOptions);
                    fo.addArguments();
                    driver = new FirefoxDriver(fo);
                    break;
                case "Edge":
                    System.setProperty("webdriver.edge.dirver", executablePath);
                    EdgeOptions eo = new EdgeOptions();
                    driver = new EdgeDriver(eo);
                    break;
                case "Safari":
                    System.setProperty("webdriver.safari.dirver", executablePath);
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
        webDriver = WebDriverBuilder.getWebDriver(webDriverType, executablePath, browserOptions);
        actions = new Actions(webDriver);
    }
}
