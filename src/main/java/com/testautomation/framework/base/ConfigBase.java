package com.testautomation.framework.base;

import com.aventstack.extentreports.Status;
import com.testautomation.framework.generator.ScreenshotGenarator;
import com.testautomation.framework.generic.Generic;
import com.testautomation.framework.integration.ALMIntegration;
import com.testautomation.framework.utils.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigBase {

    public ConfigTestData configTestData=new ConfigTestData();
    public ConfigDriver configDriver = null;
    public ScreenshotGenarator screenshotGenarator = null;
    public ExtentManager extentManager = new ExtentManager(configTestData);
    public Generic generic=null;


    static ResourceBundle rbTestdata =null;
    HashMap<String, String> enviornmentHashmap = null;


    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        try {
        if(Generic.readConfigProp("alm.Intigration").equalsIgnoreCase("ON")){
            almIntialization();
        }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeTest(alwaysRun = true)
    public void extentConfig(ITestContext context) {
        String reportName = null;
        String groupName = null;
        configTestData.suiteXmlName = context.getSuite().getName();
       // groupName=generic.getSuiteXmlGroupName(context.getIncludedGroups());
        reportName = configTestData.suiteXmlName + "_[" + DateAndTime.getTime() +"]_"+DateAndTime.getDate()+".html";
        extentManager.createReportFile(reportName);
    }

    @Parameters({"environment","platform","service"})
    @BeforeClass(alwaysRun = true)
    public void initSetup(String environment, String platformType, String service){
        if(System.getProperty("environment")==null) {
            configTestData.testEnvironment = environment.toLowerCase();
            configTestData.testservice = service.toLowerCase();
            configTestData.testPlatform = platformType.toLowerCase();

        }else {
            configTestData.testEnvironment = System.getProperty("environment").toLowerCase();
            configTestData.testservice = System.getProperty("service").toLowerCase();
            configTestData.testPlatform = System.getProperty("platform").toLowerCase();
        }
    }

    @Parameters({"banner","browser","network","mobileDevice","appName","udid","deviceName","platformName","platformVersion", "manufacturer"})
    @BeforeMethod(alwaysRun = true)
    public void initWebBrowserSetup(Method testMethod,@Optional("optional") String banner,@Optional("optional") String browser,@Optional("optional")String network,@Optional("optional")String mobileDevice,@Optional("optional")String appName,@Optional("optional")String udid,@Optional("optional") String deviceName,@Optional("optional")String platformName,@Optional("optional")String platformVersion,@Optional("optional")String manufacturer) throws Exception {
        initClass();
        if(System.getProperty("browser")!=null) {
            configTestData.testBrowser = System.getProperty("browser").toLowerCase();
            configTestData.banner = System.getProperty("banner").toLowerCase();
        }else {
            configTestData.testBrowser = browser.toLowerCase();
            configTestData.banner = banner.toLowerCase();
        }
        if(System.getProperty("mobileDevice")!=null) {
            configTestData.testNetowk = System.getProperty("network").toLowerCase();
            configTestData.testDeviceName = System.getProperty("mobileDevice").toLowerCase();
            configTestData.testAppName = System.getProperty("appName").toLowerCase();
            if(System.getProperty("udid")!=null){
                configTestData.mb_udid = System.getProperty("udid").toLowerCase();
                configTestData.mb_deviceName = System.getProperty("deviceName").toLowerCase();
                configTestData.mb_platformName = System.getProperty("platformName").toLowerCase();
                configTestData.mb_platformVersion = System.getProperty("platformVersion").toLowerCase();
                configTestData.mb_manufacturer = System.getProperty("manufacturer").toLowerCase();
            } else {
                if(configTestData.testPlatform.equalsIgnoreCase("mobile")){
                    generic.readMobileCapabilities(mobileDevice);
                }
            }

        } else {
            configTestData.testNetowk = network.toLowerCase();
            configTestData.testDeviceName = mobileDevice.toLowerCase();
            configTestData.testAppName = appName.toLowerCase();
            if(udid.equalsIgnoreCase("optional")) {
                if(configTestData.testPlatform.equalsIgnoreCase("mobile")) {
                    generic.readMobileCapabilities(mobileDevice);
                }
            } else{
                configTestData.mb_udid = udid.toLowerCase();
                configTestData.mb_deviceName = deviceName.toLowerCase();
                configTestData.mb_platformName = platformName.toLowerCase();
                configTestData.mb_platformVersion = platformVersion.toLowerCase();
                configTestData.mb_manufacturer = manufacturer.toLowerCase();
            }
        }
        try {
            configTestData.testMethodName = testMethod.getName();
//            testExecute = generic.testExecute();
           // if(testExecute) {
                initDriver();
           // }
            extentManager.createTest(configTestData.testMethodName);
        } catch (Exception e){
            e.printStackTrace();
        }
        Test testClass = testMethod.getAnnotation(Test.class);
        configTestData.groupName = testClass.groups()[0];
        loadTestData(testClass.groups()[0],banner,configTestData.testEnvironment);
        Log.info(configTestData.testMethodName + " :: TestScript is Start");
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod(ITestResult result) {

        for(String group:result.getMethod().getGroups()){
            extentManager.assignGroup(group);
        }
        try{
            configTestData.driver.quit();
            extentManager.addfinalStatus(configTestData.finalTestCaseStatus);
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.info(configTestData.testMethodName + " :: TestScript is End");
        Log.info("===================================================================");
    }

    public void initClass(){
        configDriver = new ConfigDriver(configTestData);
        screenshotGenarator = new ScreenshotGenarator(configTestData);
        generic = new Generic(configTestData);
    }

    public void initDriver(){
        try {
//            configDriver = new ConfigDriver(configTestData);
            configDriver.initDriver();
            //  Set Objet Defination file path
          //  generic.getObjectDefFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {

        try{
            enviornmentHashmap = new HashMap<String, String>();
            if(configTestData.testPlatform.equalsIgnoreCase("desktop")){
                enviornmentHashmap.put("OS",Generic.getCurretnPlatform().toString());
                enviornmentHashmap.put("UserName",System.getProperty("user.name"));
                enviornmentHashmap.put("Environment",configTestData.testEnvironment.toUpperCase());
                enviornmentHashmap.put("Platform",configTestData.testPlatform.toUpperCase());
                enviornmentHashmap.put("Browser",configTestData.testBrowser.toUpperCase());
                extentManager.setSystemInfo(enviornmentHashmap);
            } else {
                if(configTestData.testAppName.equalsIgnoreCase("optional")){
                    if(configTestData.mb_platformName.equalsIgnoreCase("android")){
                        configTestData.testBrowser = "CHROME";
                    } if(configTestData.mb_platformName.equalsIgnoreCase("ios")){
                        configTestData.testBrowser = "SAFARI";
                    }
                    enviornmentHashmap.put("OS",configTestData.mb_platformName.toUpperCase());
                    enviornmentHashmap.put("UserName",System.getProperty("user.name"));
                    enviornmentHashmap.put("Environment",configTestData.testEnvironment.toUpperCase());
                    enviornmentHashmap.put("Platform",configTestData.testPlatform.toUpperCase());
                    enviornmentHashmap.put("Mobile Device",configTestData.mb_deviceName.toUpperCase());
                    enviornmentHashmap.put("Browser",configTestData.testBrowser);
                    extentManager.setSystemInfo(enviornmentHashmap);
                } else{
                    enviornmentHashmap.put("OS",configTestData.mb_platformName.toUpperCase());
                    enviornmentHashmap.put("UserName",System.getProperty("user.name"));
                    enviornmentHashmap.put("Environment",configTestData.testEnvironment.toUpperCase());
                    enviornmentHashmap.put("Platform",configTestData.testPlatform.toUpperCase());
                    enviornmentHashmap.put("Mobile Device",configTestData.mb_deviceName.toUpperCase());
                    enviornmentHashmap.put("App",configTestData.testAppName);
                    extentManager.setSystemInfo(enviornmentHashmap);
                }
            }


            extentManager.assignLog(generic.readFile(ConfigTestData.workDir+ Generic.readConfigProp("log.file")));
            extentManager.setHtmlConfig(configTestData.suiteXmlName);
            extentManager.flush();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ResourceBundle loadTestData(String groupName,String banner,String env) throws Exception{

        File file = new File(Generic.getTestDataPath(groupName));
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        return rbTestdata = ResourceBundle.getBundle(banner+"_"+env, Locale.getDefault(), loader);
    }

    public void almIntialization(){
        try{
            ALMIntegration almIntegration = new ALMIntegration();
            almIntegration.initializeConnection();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void verify(boolean condition){
        configTestData.stepNo = configTestData.stepNo+1;
        if(condition){
            extentManager.addExecutionStep(Status.PASS);
        } else{
            if(configTestData.stepExpected.contains(" is not ")){
                configTestData.stepExpected = configTestData.stepExpected.replaceAll(" is not ", " is ");
            } else {
                configTestData.stepExpected = configTestData.stepExpected.replaceAll(" is ", " is not ");
            }
            extentManager.addExecutionStep(Status.FAIL);
            configTestData.finalTestCaseStatus = Status.FAIL;
        }
    }
    public void verify(String condition, String trueMsg,String falseMsg){
        configTestData.stepNo = configTestData.stepNo+1;
        if(condition.toLowerCase() == "true"){
            extentManager.addExecutionStep(Status.PASS);
        } else{
            extentManager.addExecutionStep(Status.FAIL);
            configTestData.finalTestCaseStatus = Status.FAIL;
        }
    }
    public String verify(boolean condition,String message){
        configTestData.stepNo = configTestData.stepNo+1;
        if(condition){
            return "true";
        } else{
            return "false";
        }
    }

    public void Assert(String condition,String trueMsg,String falseMsg){
        configTestData.stepNo = configTestData.stepNo+1;
        boolean con;
        if(condition.toLowerCase() == "true"){
            extentManager.addExecutionStep(Status.PASS);
            con=true;
        } else{
            con=false;
            extentManager.addExecutionStep(Status.FAIL);
            configTestData.finalTestCaseStatus = Status.FAIL;
        }

        Assert.assertTrue(con,falseMsg);
    }


    /**
     *
     *     */
    public void report(String status){
        if(status.equalsIgnoreCase("Pass")||status.equalsIgnoreCase("true")){
            extentManager.addExecutionStep(Status.PASS);
        }else {
            extentManager.addExecutionStep(Status.FAIL);
        }

    }
    public void assertTrueAndReport(boolean condition, String passMessage, String failMessage){
        if(condition){
            report("pass");
        }else{
            report("fail");
        }
        assertTrue(failMessage, condition);
    }

    public void assertFalseAndReport(boolean condition, String passMessage, String failMessage){
        if(!condition){
            report("pass");
        }else{
            report("fail");
        }
        assertFalse(failMessage, condition);
    }

}
