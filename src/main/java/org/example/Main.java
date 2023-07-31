package org.example;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Class c = DemoWebDriver.class;

        Constructor con = c.getDeclaredConstructors()[0];

        TaskParser tp = new TaskParser(args[0]);
        TestJob tj = tp.getTestJob();
        new TaskRunner(tj);
    }
}
