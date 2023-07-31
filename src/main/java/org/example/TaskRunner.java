package org.example;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TaskRunner {
    public TaskRunner(TestJob testJob) {

        Class c = DemoWebDriver.class;
        Constructor con = c.getDeclaredConstructors()[0];

        try {
            Object wb = con.newInstance(testJob.getWebdriverType(), testJob.getWebdriverPath(), testJob.getOptions());
            for (Step step: Arrays.stream(testJob.steps).toList()) {
                Method method = c.getMethod(step.module, step.getClass());
                method.invoke(wb, step);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}