package org.example;
import java.lang.reflect.Constructor;
import org.yaml.snakeyaml.Yaml;
// import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import java.io.InputStream;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import java.io.InputStream;
import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) {
        Class c = DemoWebDriver.class;

        Constructor con = c.getDeclaredConstructors()[0];

        System.out.println(args[0]);
        // TaskParser tp = new TaskParser("/Users/mirage/Desktop/yuantaDemo.yaml");
        TaskParser tp = new TaskParser(args[0]);
        TestJob tj = tp.getTestJob();
        new TaskRunner(tj.steps);
    }
}
