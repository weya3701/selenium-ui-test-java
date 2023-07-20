package org.example;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.io.FileInputStream;

public class TaskParser {

    private TestJob testJob;

    public TaskParser(String yamlFile) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Class<?> TestJob;
        try {
            Yaml yaml = new Yaml(new Constructor(TestJob.class), representer);
            InputStream inputStream = new FileInputStream(yamlFile);
            TestJob testJob = yaml.load(inputStream);
            this.testJob = testJob;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public TestJob getTestJob() {
        return this.testJob;
    }
}
