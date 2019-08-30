package com.testautomation.framework.integration;

import atu.alm.wrapper.ALMServiceWrapper;
import atu.alm.wrapper.ITestCase;
import atu.alm.wrapper.ITestCaseRun;
import atu.alm.wrapper.enums.StatusAs;
import atu.alm.wrapper.exceptions.ALMServiceException;
import com.aventstack.extentreports.Status;
import com.testautomation.framework.base.ConfigTestData;
import com.testautomation.framework.generic.Generic;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class ALMIntegration  {

    private ALMServiceWrapper almWrapper = null;
    private String almURL;
    private String almUserName;
    private String almPassword;
    private String almDomain;
    private String almProject;
    private String almTestSetPath;
    private String almTestSetName;
    private String almTestSetID;

    public static String almTestCaseName=null;
    private String testStepExecutionResults[][] = new String[1][5];

    public boolean initALM = false;
    private ITestCaseRun objRun = null;
    private ITestCase objTestCase = null;

    public ALMIntegration() {
        readsALMArgs();
        almWrapper = new ALMServiceWrapper(almURL);
    }
    /**
     * To connect ALM using jacob and ALM TDConnect
     *
     * @throws ALMServiceException
     */
    public void initializeConnection() throws ALMServiceException {
        boolean isConnected = almWrapper.connect(almUserName, almPassword, almDomain, almProject);
        if (isConnected) {
            initALM=true;
            System.out.println("Connected to ALM!");
        } else {
            System.out.println("Unable to connect to ALM.");
        }
    }

    /**
     * To upadataExtractore Test case Results to ALM each step
     *
     * @param finalstatus
     * @throws ALMServiceException
     */
    public void updateTestCaseResult(ConfigTestData configTestData,StatusAs finalstatus) throws ALMServiceException {
        StatusAs status = null;
        int j;
        if (configTestData.finalTestCaseStatus != null) {
            if (configTestData.finalTestCaseStatus.equals("Passed")) {
                status = StatusAs.PASSED;
            } else if (configTestData.finalTestCaseStatus.equals("N/A")) {
                status = StatusAs.N_A;
            } else {
                status = StatusAs.FAILED;
            }
        }
        if (finalstatus == StatusAs.FAILED) {
            status = finalstatus;
        }
        if (initALM) {
            System.out.println("############################################");
            System.out.println(almTestSetPath);
            System.out.println(almTestSetName);
            System.out.println(almTestSetID);
            System.out.println(almTestCaseName);
            System.out.println(status);
            System.out.println("############################################");

            objTestCase =
                    almWrapper.updateResult(
                            almTestSetPath, almTestSetName, Integer.valueOf(almTestSetID), almTestCaseName, status);
            objRun = almWrapper.createNewRun(objTestCase, LocalDateTime.now().toString(), status);
            for (int i = 0; i < testStepExecutionResults.length; i++) {
                if (testStepExecutionResults[i][1] != null) {
                    if (testStepExecutionResults[i][1].equals("Passed")) {
                        status = StatusAs.PASSED;
                    } else if (testStepExecutionResults[i][1].equals("Failed")) {
                        status = StatusAs.FAILED;
                    } else if (testStepExecutionResults[i][1].equals("N/A")) {
                        status = StatusAs.N_A;
                    }

                    almWrapper.addStep(
                            objRun,
                            "Step " + testStepExecutionResults[i][0],
                            status,
                            testStepExecutionResults[i][2],
                            testStepExecutionResults[i][3],
                            testStepExecutionResults[i][4]);
                    j = i + 1;

                    if (finalstatus == StatusAs.FAILED && j == testStepExecutionResults.length) {
                        almWrapper.addStep(
                                objRun,
                                "Step " + configTestData.stepNo,
                                StatusAs.NOT_COMPLETED,
                                "This test case is aborted",
                                "Failed",
                                "Terminated");
                    }
                }
            }
            terminateConnection();
        } else {
            System.out.println("############################################");
            System.out.println(almTestCaseName + "::" + status);
            System.out.println("############################################");
        }
    }

    /**
     * Add each step results to Array
     *
     * @param testStatus
     * @throws Exception
     */
    public void addExecutionStep(ConfigTestData configTestData, String testStatus)
            throws Exception {
        String actual = null;

        if (configTestData.finalTestCaseStatus == null) {
            configTestData.finalTestCaseStatus = Status.PASS;
        }

        if (configTestData.stepNo > 1) {
            testStepExecutionResults =
                    (String[][]) Generic.resizeArray(testStepExecutionResults, testStepExecutionResults.length + 1);
            if (testStepExecutionResults[testStepExecutionResults.length - 1] == null) {
                testStepExecutionResults[testStepExecutionResults.length - 1] = new String[5];
            }
        }
        if (testStatus.equalsIgnoreCase("Failed")) {
            configTestData.finalTestCaseStatus = Status.FAIL;
            actual = configTestData.stepFail;

        } else {
            actual = configTestData.stepExpected;
        }

        testStepExecutionResults[testStepExecutionResults.length - 1][0] =
                Integer.toString(configTestData.stepNo);
        testStepExecutionResults[testStepExecutionResults.length - 1][1] = testStatus;
        testStepExecutionResults[testStepExecutionResults.length - 1][2] =
                configTestData.stepDescription;
        testStepExecutionResults[testStepExecutionResults.length - 1][3] = configTestData.stepExpected;
        testStepExecutionResults[testStepExecutionResults.length - 1][4] = actual;

    }

    /**
     * To close the ALM Connection
     *
     * @throws ALMServiceException
     */
    public void terminateTestExecution() throws ALMServiceException {
        // updateTestCaseResult();
        terminateConnection();
        //        Assert.fail("Test execution terminated!");
        //        throw new SkipException("Test execution terminated!");
    }

    public void terminateConnection() {
        almWrapper.close();
    }

    public void readsALMArgs(){
        String[] args=null;
        ClassLoader loader=null;
        try {
            File file = new File(System.getProperty("user.dir")+ File.separator + "src"+File.separator+"test"+File.separator+"resources"+File.separator+"config"+File.separator+"alm");
            URL[] urls = {file.toURI().toURL()};
            loader = new URLClassLoader(urls);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResourceBundle rbTestdata = ResourceBundle.getBundle("alm", Locale.getDefault(), loader);
        almURL = rbTestdata.getString("alm.URL");
        almUserName = rbTestdata.getString("alm.UserName");
        almPassword = rbTestdata.getString("alm.Password");
        almDomain = rbTestdata.getString("alm.Domain");
        almProject = rbTestdata.getString("alm.Project");
        almTestSetPath = rbTestdata.getString("alm.TestSetPath");
        almTestSetName = rbTestdata.getString("alm.TestSetName");
        almTestSetID = rbTestdata.getString("alm.TestSetID");
    }
}
