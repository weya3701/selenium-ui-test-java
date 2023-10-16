package org.example;

import java.io.IOException;
import java.util.Map;
import org.example.plansType.*;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) throws IOException {

        Dotenv dotenv = Dotenv.configure().filename("env").load();

        TestPlansAutomation tsa = new TestPlansAutomation(dotenv.get("AzureApisDomain"), args[0], args[1]);
        tsa.GetTestPlans("TestPlansUrl");
        Map<String, Map<String, PlansTypeImp>> response = tsa.GetPlansObjectMap();
        List<TestCasesThread> tct = new ArrayList<>();
        for (String id: tsa.GetAllPlanIds()) {
            if (id.equalsIgnoreCase(args[2])) {
                Map<String, PlansTypeImp> r = response.get(id);
                PlansTypeString planid = (PlansTypeString) r.get("TestPlanId");
                PlansTypeString suiteid = (PlansTypeString) r.get("testSuiteId");
                tct.add(new TestCasesThread(tsa, "TestCasesUrl", id, suiteid.getValue()));
            }
        }
        for (TestCasesThread thread: tct) {
            thread.run();
        }

        Map<String, Map<String, PlansTypeImp>> r = tsa.GetPlansObjectMap();
        for (String planId: tsa.GetAllPlanIds()) {
            if (planId.equalsIgnoreCase(args[2])) {
                PlansTypeObjectList testCases = (PlansTypeObjectList) r.get(planId.toString()).get("TestCases"); // FIXME. Need to Fixme.
                List<Map<String, PlansTypeImp>> rr = testCases.getValue();
                for (Map<String, PlansTypeImp> testCase : testCases.getValue()) {
                    PlansTypeObjectList parameterList = (PlansTypeObjectList) testCase.get("StepParameter");
                    TestJob tj = new TestJob();
                    List<Step> steps = new ArrayList<>();
                    PlansTypeString testCaseId = (PlansTypeString) testCase.get("TestCaseId");
                    PlansTypeString pointId = (PlansTypeString) testCase.get("PointId");
                    tj.job = dotenv.get("JOB_NAME");
                    tj.description = dotenv.get("DESCRIPTION");
                    tj.testCaseID = testCaseId.getValue();
                    tj.testCaseDescription = dotenv.get("TEST_CASE_DESCRIPTION");
                    tj.picPath = dotenv.get("PIC_PATH");
                    tj.reportFile = dotenv.get("REPORT_FILE");
                    tj.reportFilePath = dotenv.get("REPORT_FILE_PATH");
                    tj.options = new String[]{"--disable-gpu", "--window-size=19200,10800"};
                    tj.webdriverType = dotenv.get("WEBDRIVER_TYPE");
                    tj.webdriverPath = dotenv.get("WEBDRIVER_PATH");
                    for (Map<String, PlansTypeImp> parameter : parameterList.getValue()) {
                        Step step = new Step();
                        PlansTypeString interval = (PlansTypeString) parameter.getOrDefault("interval", new PlansTypeString("1"));
                        PlansTypeString elementName = (PlansTypeString) parameter.getOrDefault("elementName", new PlansTypeString(""));
                        PlansTypeString desc = (PlansTypeString) parameter.getOrDefault("desc", new PlansTypeString(""));
                        PlansTypeString module = (PlansTypeString) parameter.getOrDefault("module", new PlansTypeString(""));
                        PlansTypeString url = (PlansTypeString) parameter.getOrDefault("url", new PlansTypeString(""));
                        PlansTypeString by = (PlansTypeString) parameter.getOrDefault("by", new PlansTypeString(""));
                        PlansTypeString key = (PlansTypeString) parameter.getOrDefault("key", new PlansTypeString(""));
                        PlansTypeString tab = (PlansTypeString) parameter.getOrDefault("tab", new PlansTypeString("0"));
                        step.interval = Integer.valueOf((String) interval.getValue());
                        step.elementName = elementName.getValue();
                        step.desc = desc.getValue();
                        step.module = module.getValue();
                        step.url = url.getValue();
                        step.by = by.getValue();
                        step.key = key.getValue();
                        step.tab = Integer.valueOf(((PlansTypeString) tab).getValue());
                        steps.add(step);
                    }
                    Step[] tpsteps = new Step[steps.size()];
                    for (int i = 0; i < steps.size(); i++) {
                        tpsteps[i] = steps.get(i);
                    }
                    tj.steps = tpsteps;
                    long timestamp = System.currentTimeMillis();
                    String timestampStr = Long.toString(timestamp);
                    int pointIdValue = Integer.parseInt(pointId.getValue());
                    PlansTypeString caseName = (PlansTypeString) testCase.get("TestCaseName");
                    String caseNameStr = caseName.getValue();
                    String runsIdStr = tsa.CreateCaseRuns(caseNameStr + "_" + timestampStr, planId, pointIdValue);

                    new TaskRunner(
                            tj,
                            tsa,
                            "AutomationCaseRuns_" + timestampStr,
                            planId,
                            runsIdStr,
                            100000
                    );
                }
            }
        }
    }
}