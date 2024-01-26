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

    private void setSecondsSleep(int n) throws InterruptedException {
        TimeUnit.SECONDS.sleep(n);
    }

    private By getElementBy(String elementName, String selector) {
        return switch (selector) {
            case "css" -> By.cssSelector(elementName);
            case "linkText" -> By.linkText(elementName);
            case "id" -> By.id(elementName);
            case "name" -> By.name(elementName);
            default -> By.xpath(elementName);
        };
    }

    public String switch_tab_by_name(Step step) {
        this.webDriver.switchTo().window(step.tabName);
        try {
            setSecondsSleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.successful;
    }

    @Override
    public String set_shadow_root(Step step) {
        String rsp = this.successful;
        try {
            String script = String.format(
                    """
                    sd = document.getElementsByTagName('%s')[0].shadowRoot;
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

    public String switch_tab(Step step) {
        System.out.println("tab: "+step.tab);
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
        try {
            setSecondsSleep(step.interval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTaskStepStatus(true);
        return this.successful;
    }

    public String open_new_tab(Step step) {

        JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
        executor.executeScript("window.open('');");
        try {
            setSecondsSleep(step.interval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTaskStepStatus(true);
        return this.successful;
    }

    @Override
    public String find_shadow_root_element_and_click(Step step) {

        String rsp = this.successful;
        try {
            String script = String.format(
                    """
                    sd.querySelector('%s').click()
                    """, step.elementName
            );
            script.replaceAll("'", "\"");
            script.replaceAll("\"", "'");
            JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
            executor.executeScript(script);
            setTaskStepStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        return rsp;
    }

    @Override
    public String find_shadow_root_element_and_sendkey(Step step) {
        String rsp = this.successful;
        try {
            String script = String.format(
                    """
                    sd.querySelector('%s').value='%s';
                    var event = new Event('input', {'bubbles': true});
                    sd.querySelector('%s').dispatchEvent(event);
                    """, step.elementName, step.key
            );
            script.replaceAll("'", "\"");
            script.replaceAll("\"", "'");
            JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
            executor.executeScript(script);
            setTaskStepStatus(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            rsp = this.failed;
            setTaskStepStatus(false);
        }
        return rsp;
    }

    @Override
    public String find_element_and_click(Step step) {

        String rsp = this.successful;
        try {
            By by = getElementBy(step.elementName, step.by);
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(by);
            WebElement element = new WebDriverWait(
                    this.webDriver, Duration.ofSeconds(30).toSeconds()
            ).until(condition);
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
            WebElement element = new WebDriverWait(
                    this.webDriver, Duration.ofSeconds(30).toSeconds()
            ).until(condition);
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
    public String webdriver_run_script(Step step) {
        String rsp = this.successful;
        System.out.println(step.elementName);
        try {
            JavascriptExecutor executor = (JavascriptExecutor) this.webDriver;
            executor.executeScript(step.elementName);
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
            } else {
                rsp = this.failed;
            }
        }
        return rsp;
    }

    @Override
    public String set_windows_size(Step step) {
        String rsp = this.successful;
        try {
            String element = step.elementName;
            String x;
            String y;
            x = element.split("x")[0];
            y = element.split("x")[1];
            this.webDriver.manage().window().setSize(new Dimension(Integer.valueOf(x), Integer.valueOf(y)));
        } catch (Exception e) {
            rsp = this.failed;
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

        }

        if (resultPattern.size() == total) {
            setTaskStepStatus(true);
        } else {
            rsp = this.failed;
            setTaskStepStatus(false);
        }

        return rsp;
    }

    @Override
    public String find_element_and_click_with_wait(Step step) {
        String rsp = this.successful;
        By selectBy = getElementBy(step.elementName, step.by);
        try {
            this.actions.moveToElement(
                    this.webDriver.findElement(
                            selectBy
                    )
            ).build().perform();
            Function<WebDriver, WebElement> condition = (WebDriver d) -> d.findElement(selectBy);
            WebElement element = new WebDriverWait(this.webDriver, Duration.ofSeconds(30).toSeconds())
                    .until(condition);
            element.click();
            setTaskStepStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            rsp = this.failed;
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
                case "Chrome" -> {
                    System.setProperty("webdriver.chrome.driver", executablePath);
                    ChromeOptions co = new ChromeOptions();
                    co.addArguments(browserOptions);
                    co.addArguments();
                    driver = new ChromeDriver(co);
                }
                case "Firefox" -> {
                    System.setProperty("webdriver.firefox.driver", executablePath);
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.addArguments(browserOptions);
                    fo.addArguments();
                    driver = new FirefoxDriver(fo);
                }
                case "Edge" -> {
                    System.setProperty("webdriver.edge.driver", executablePath);
                    EdgeOptions eo = new EdgeOptions();
                    driver = new EdgeDriver(eo);
                }
                case "Safari" -> {
                    System.setProperty("webdriver.safari.driver", executablePath);
                    SafariOptions so = new SafariOptions();
                    driver = new SafariDriver(so);
                }
                default -> driver = null;
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
