package org.example;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TaskRunner {
    public TaskRunner(String ...steps) {

        Class c = DemoWebDriver.class;

        Constructor con = c.getDeclaredConstructors()[0];

        // 取得Webdriver運行實體
        try {
            Object wb = con.newInstance("Chrome", "/Users/mirage/Desktop/chromedriver");

            for (String step: steps) {
                // 取得物件方法
                Method method = c.getMethod(step);
                // 執行方法
                method.invoke(wb);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

}