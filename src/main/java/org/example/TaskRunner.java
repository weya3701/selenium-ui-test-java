package org.example;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;

public class TaskRunner {
    public TaskRunner(TestJob testJob) {

        Class c = DemoWebDriver.class;
        Constructor con = c.getDeclaredConstructors()[0];
        String webdriver_status = "";
        try {
            Object wb = con.newInstance(testJob.getWebdriverType(), testJob.getWebdriverPath(), testJob.getOptions());
            for (Step step: Arrays.stream(testJob.steps).toList()) {
                Method taskMethod = c.getMethod(step.module, step.getClass());
                webdriver_status = (String) taskMethod.invoke(wb, step);
                Method saveMethod = c.getMethod("saveResult", String.class, String.class);
                saveMethod.invoke(wb, step.desc, webdriver_status);
            }
            Method resultMethod = c.getMethod("getResult");
            Method statusMethod = c.getMethod("getTaskStatus");
            List<HashMap<String, String>> result = (List<HashMap<String, String>>) resultMethod.invoke(wb);
            String status = (String) statusMethod.invoke(wb);
            System.out.println("##"+testJob.job);
            System.out.println("### 測試案例資訊");
            System.out.println("* 測試案例描述："+testJob.description);
            System.out.println("* 測試案例步驟描述："+testJob.testCaseDescription);
            System.out.println("* 瀏覽器類型："+testJob.webdriverType);
            System.out.println("* 是否啟用畫面擷圖："+testJob.screenshot);
            System.out.println("* * * ");
            System.out.println("### 測試步驟結果");
            System.out.println("```");

            for (HashMap<String, String> r: result) {
                System.out.println("* 步驟："+r.get("Step")+" --> 結果："+r.get("Status"));
            }
            
            System.out.println("```");
            System.out.println("* * * ");
            System.out.println("執行結果："+status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}