package org.example;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) {
        try {
            Representer represent = new Representer();
            represent.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(new Constructor(TestJob.class), represent);
            InputStream inputStream =new FileInputStream("/Users/arthuryueh/Downloads/validation_demo.yaml");
            TestJob testJob = yaml.load(inputStream);
            System.out.println(testJob);

//            new DemoWebDriver("Chrome", "/Users/arthuryueh/Downloads/chromedriver_mac_arm64/chromedriver").openWebsite("https://www.google.com/");
//            Thread.sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}