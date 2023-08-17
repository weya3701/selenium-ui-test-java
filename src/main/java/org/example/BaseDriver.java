package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

abstract class BaseDriver {
    HashMap<String, HashMap<String, String>> resultQueue;
    public List<HashMap<String, String>> stepsResult;
    boolean taskStepStatus = true;
    String successful = "執行成功";
    String failed = "執行失敗";

    BaseDriver() {

        stepsResult = new ArrayList<HashMap<String, String>>();
        resultQueue = new HashMap<String, HashMap<String, String>>();
    }
}
