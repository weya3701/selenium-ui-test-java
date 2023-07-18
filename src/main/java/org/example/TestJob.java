package org.example;

import lombok.Data;
import lombok.Getter;

@Data
public class TestJob {
    String job;
    String description;
    String testCaseDescription;
    String testCaseId;
    String webDriverType;
    String webDriverPath;
    Boolean screenshot;
    String[] Options;
    Step[] steps;
}


