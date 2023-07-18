package org.example;

public interface WebAutomationTool {

    static final String SUCCESSFUL = "執行成功";
    static final String FAILED = "執行失敗";

    public void openWebsite(String url);

    public void findElementAndClick(String selector, int waitSeconds);
}