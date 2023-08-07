package org.example;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

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
            System.out.println(resultMethod.invoke(wb));
            Method statusMethod = c.getMethod("getTaskStatus");
            System.out.println(statusMethod.invoke(wb));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}