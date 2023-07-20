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

//        TaskRunner tr = new TaskRunner(
//                "open_new_tab",
//                "get_value_to_store",
//                "get_regex_value_to_store",
//                "find_element_and_sendkey_from_store",
//                "validation",
//                "get_value_to_store"
//        );

        TaskParser tp = new TaskParser("/Users/mirage/Desktop/yuantaDemo.yaml");
        TestJob tj = tp.getTestJob();
//        for (Step st: tj.steps) {
//            System.out.println(st);
//        }
        new TaskRunner(tj.steps);
    }
}
