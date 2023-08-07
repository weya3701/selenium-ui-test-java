package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

abstract class BaseDriver {
    HashMap<String, HashMap<String, String>> resultQueue;
    List<HashMap<String, String>> stepsResult = new ArrayList<HashMap<String, String>>();
    boolean taskStepStatus = true;
    String successful = "執行成功";
    String failed = "執行失敗";

}
