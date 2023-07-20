package org.example;

public interface WebAutomationTool {

    static final String SUCCESSFUL = "執行成功";
    static final String FAILED = "執行失敗";

    public void open_website(Step step);

    public void find_element_and_click(Step step);
}