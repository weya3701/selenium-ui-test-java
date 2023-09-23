package org.example;

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
    public TaskRunner(
            TestJob testJob,
            TestPlansAutomation tpa,
            String runsName,
            String planId,
            String runsId,
            int resultId
            ) {
        Class c = DemoWebDriver.class;
        Constructor con = c.getDeclaredConstructors()[0];
        String webdriver_status = "";
        String report = "";
        try {
            Object wb = con.newInstance(testJob.getWebdriverType(), testJob.getWebdriverPath(), testJob.getOptions());
            for (Step step: Arrays.stream(testJob.steps).toList()) {
                step.replaceSymbol();
                Method taskMethod = c.getMethod(step.module, step.getClass());
                webdriver_status = (String) taskMethod.invoke(wb, step);
                Method saveMethod = c.getMethod("saveResult", String.class, String.class);
                saveMethod.invoke(wb, step.desc, webdriver_status);
                Method screenshotMethod = c.getMethod("getScreenshot", String.class, step.getClass());
                screenshotMethod.invoke(wb, testJob.picPath, step);
            }
            Method resultMethod = c.getMethod("getResult");
            Method statusMethod = c.getMethod("getTaskStatus");
            Method closeMethod = c.getMethod("closeWebdriver");
            Method quitMethod = c.getMethod("quitWebdriver");
            closeMethod.invoke(wb);
            quitMethod.invoke(wb);
            List<HashMap<String, String>> result = (List<HashMap<String, String>>) resultMethod.invoke(wb);
            String status = (String) statusMethod.invoke(wb);
            MarkdownDocument markdown = new MarkdownDocument();
            markdown.setContent("##", testJob.job);
            markdown.setContent("### 測試案例資訊");
            markdown.setContent("* 測試案例描述:", testJob.description);
            markdown.setContent("* 測試案例步驟描述:", testJob.testCaseDescription);
            markdown.setContent("* 瀏覽器類型:", testJob.webdriverType);
            markdown.setContent("\n");
            markdown.setContent("* * *");
            markdown.setContent("### 測試步驟結果");
            markdown.setContent("```");

            for (HashMap<String, String> r: result) {
                markdown.setContent("* 步驟:", r.get("Step"), " ---> ", "結果:", r.get("Status"));
            }
            markdown.setContent("```");
            markdown.setContent("* * *");
            markdown.setContent("結果:", status);
            ReportWriter(
                    testJob.reportFilePath+testJob.reportFile,
                    markdown.getMarkdownContent());
            tpa.UpdateRunsResult(runsId, 100000, "Completed", status, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}