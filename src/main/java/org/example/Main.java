package org.example;

import java.io.IOException;
import java.util.Map;
import org.example.plansType.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        TestPlansAutomation tsa = new TestPlansAutomation(args[0], args[1]);
        tsa.GetTestPlans("TestPlansUrl");
        Map<String, Map<String, PlansTypeImp>> response = tsa.GetPlansObjectMap();
        List<TestCasesThread> tct = new ArrayList<>();
        for (String id: tsa.GetAllPlanIds()) {
            Map<String, PlansTypeImp> r = response.get(id);
            PlansTypeString planid = (PlansTypeString) r.get("TestPlanId");
            PlansTypeString suiteid = (PlansTypeString) r.get("testSuiteId");

            tct.add(new TestCasesThread(tsa, "TestCasesUrl", planid.getValue(), suiteid.getValue()));
        }
        for (TestCasesThread thread: tct) {
            thread.run();
        }

        Map<String, Map<String, PlansTypeImp>> r = tsa.GetPlansObjectMap();
        for (String planId: tsa.GetAllPlanIds()) {
            PlansTypeObjectList testCases = (PlansTypeObjectList) r.get(planId.toString()).get("TestCases"); // FIXME. Need to Fixme.
            List<Map<String, PlansTypeImp>> rr = testCases.getValue();
            for (Map<String, PlansTypeImp> testCase : testCases.getValue()) {
                PlansTypeObjectList parameterList = (PlansTypeObjectList) testCase.get("StepParameter");
                TestJob tj = new TestJob();
                List<Step> steps = new ArrayList<>();
                PlansTypeString testCaseId = (PlansTypeString) testCase.get("TestCaseId");
                PlansTypeString pointId = (PlansTypeString) testCase.get("PointId");
                tj.job = "UITest";
                tj.description = "Azure DevOps TestPlans自動化測試平台";
                tj.testCaseID = testCaseId.getValue();
                tj.testCaseDescription = "Azure DevOps TestPlans自動化測試";
                tj.picPath = "pic/";
                tj.reportFile = "AzrueTestPlansReport.md";
                tj.reportFilePath = "report/";
                tj.options = new String[]{"--disable-gpu", "--window-size=19200,10800"};
                tj.webdriverType = "Chrome";
                tj.webdriverPath = "/Users/mirage/Documents/workspace/packages/chromedriver";
                for (Map<String, PlansTypeImp> parameter : parameterList.getValue()) {
                    Step step = new Step();
                    PlansTypeString interval = (PlansTypeString) parameter.getOrDefault("interval", new PlansTypeString(""));
                    PlansTypeString elementName = (PlansTypeString) parameter.getOrDefault("elementName", new PlansTypeString(""));
                    PlansTypeString desc = (PlansTypeString) parameter.getOrDefault("desc", new PlansTypeString(""));
                    PlansTypeString module = (PlansTypeString) parameter.getOrDefault("module", new PlansTypeString(""));
                    PlansTypeString url = (PlansTypeString) parameter.getOrDefault("url", new PlansTypeString(""));
                    PlansTypeString by = (PlansTypeString) parameter.getOrDefault("by", new PlansTypeString(""));
                    PlansTypeString key = (PlansTypeString) parameter.getOrDefault("key", new PlansTypeString(""));
                    step.interval = Integer.valueOf((String) interval.getValue());
                    step.elementName = elementName.getValue();
                    step.desc = desc.getValue();
                    step.module = module.getValue();
                    step.url = url.getValue();
                    step.by = by.getValue();
                    step.key = key.getValue();
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
                String runsIdStr = tsa.CreateCaseRuns(caseNameStr+"_"+timestampStr, planId, pointIdValue);

                new TaskRunner(
                        tj,
                        tsa,
                        "AutomationCaseRuns_"+timestampStr,
                        planId,
                        runsIdStr,
                        100000
                );
            }
        }
    }
}