package org.example;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class TaskRunner {

    private void ReportWriter(String filename, String content) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public TaskRunner(TestJob testJob) {

        Class c = DemoWebDriver.class;
        Constructor con = c.getDeclaredConstructors()[0];
        String webdriver_status = "";
        String report = "";
        try {
            Object wb = con.newInstance(testJob.getWebdriverType(), testJob.getWebdriverPath(), testJob.getOptions());
            for (Step step: Arrays.stream(testJob.steps).toList()) {
                Method taskMethod = c.getMethod(step.module, step.getClass());
                webdriver_status = (String) taskMethod.invoke(wb, step);
                Method saveMethod = c.getMethod("saveResult", String.class, String.class);
                saveMethod.invoke(wb, step.desc, webdriver_status);
                Method screenshotMethod = c.getMethod("getScreenshot", step.getClass());
                screenshotMethod.invoke(wb, step);
            }
            Method resultMethod = c.getMethod("getResult");
            Method statusMethod = c.getMethod("getTaskStatus");
            List<HashMap<String, String>> result = (List<HashMap<String, String>>) resultMethod.invoke(wb);
            String status = (String) statusMethod.invoke(wb);
            report = "##"+testJob.job+"\n";
            report += "### 測試案例資訊"+"\n";
            report += "* 測試案例描述："+testJob.description+"\n";
            report += "* 測試案例步驟描述："+testJob.testCaseDescription+"\n";
            report += "* 瀏覽器類型："+testJob.webdriverType+"\n";
            report += "* 是否啟用畫面擷圖："+testJob.screenshot+"\n";
            report += "\n";
            report += "* * * "+"\n";
            report += "\n";
            report += "### 測試步驟結果"+"\n";
            report += "```"+"\n";

            for (HashMap<String, String> r: result) {
                report += "* 步驟："+r.get("Step")+" --> 結果："+r.get("Status")+"\n";
            }

            report += "```"+"\n";
            report += "* * * "+"\n";
            report += "執行結果："+status+"\n";
            ReportWriter("report/taskReport.md", report);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}