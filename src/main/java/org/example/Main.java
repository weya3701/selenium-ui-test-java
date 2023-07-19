package org.example;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
// import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args) {
        Class c = DemoWebDriver.class;

        Constructor con = c.getDeclaredConstructors()[0];

        TaskRunner tr = new TaskRunner(
                "open_new_tab",
                "get_value_to_store",
                "get_regex_value_to_store",
                "find_element_and_sendkey_from_store",
                "validation",
                "get_value_to_store"
        );

        // try {
        //     // System.out.println("Hello world!");
        //     // DemoWebDriver wb = new DemoWebDriver("Chrome", "/Users/mirage/Desktop/chromedriver");
        //     Object wb = con.newInstance("Chrome", "/Users/mirage/Desktop/chromedriver");
        //     Method method = c.getMethod("open_website", String.class);
        //     method.invoke(wb, "https://www.google.com/");
        //     method = c.getMethod("validation");
        //     method.invoke(wb);
            // wb.open_website("https://www.google.com/");
        //     Thread.sleep(5000);
        // } catch (Exception ex) {
        //     System.out.println(ex.toString());
        // }
    }
}
