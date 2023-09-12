package org.example;

import lombok.Data;

@Data
public class TestJob {
    String job;
    String description;
    String testCaseDescription;
    String testCaseID;
    String webdriverType;
    String webdriverPath;
    String screenshot;
    String picPath;
    String reportFilePath;
    String reportFile;
    String[] options;
    Step[] steps;
}