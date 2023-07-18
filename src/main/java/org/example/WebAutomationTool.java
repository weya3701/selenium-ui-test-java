package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

public interface WebAutomationTool {

    static final String SUCCESSFUL = "執行成功";
    static final String FAILED = "執行失敗";

    public void open_website(String url);
}