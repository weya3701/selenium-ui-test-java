package org.example;

import lombok.Data;
import lombok.Getter;

@Data
public class TestJob {
    String job;
    String description;
    String testCaseDescription;
    String testCaseID;
    String webdriverType;
    String webdriverPath;
    String screenshot;
    Step[] steps;
}