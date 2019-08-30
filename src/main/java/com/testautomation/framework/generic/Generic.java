package com.testautomation.framework.generic;

import com.testautomation.framework.base.ConfigTestData;
import com.testautomation.framework.utils.JsonUtils;
import com.testautomation.framework.utils.Log;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.openqa.selenium.Platform;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Generic {

    static ResourceBundle rbConfig =null;

    private static Platform platform = null;
    ConfigTestData configTestData=null;
    JsonUtils jsonUtils=null;

    public Generic(ConfigTestData configTestData){
        this.configTestData=configTestData;
        jsonUtils = new JsonUtils();
    }


    /**
     * Method Description :: Verify Text actual and expected
     * @param sActualText
     * @param sExpectedText
     * @param verifyTextOptions
     * @return
     */
    public static boolean verifyText(String sActualText, String sExpectedText, String verifyTextOptions){
        boolean result=true;
        try{
            switch (verifyTextOptions) {
                case "EXACTMATCH":
                    result = sActualText.equals(sExpectedText);
                    break;
                case "EXACTMATCHIGNORECASE":
                    sActualText=sActualText.trim();
                    sExpectedText=sExpectedText.trim();
                    result=sActualText.equalsIgnoreCase(sExpectedText);
                    break;
                case "PARTIAL":
                    sActualText=sActualText.trim().toLowerCase();
                    sExpectedText=sExpectedText.trim().toLowerCase();
                    result = sActualText.contains(sExpectedText);
                    break;
            }

        }catch (Exception e) {
            result=false;
            Log.error("Exception in verifyText :"+ e.getMessage());
        }
        return result;
    }

    /**
     * Method Description :: Trim the String leading zeros
     * @param stext
     * @return
     */
    public String trimLeadingZeros(String stext) {
        String sreturn_text = null;
        try {
            Double value = Double.parseDouble(stext);
            sreturn_text = value.toString();

        } catch (Exception e) {
            Log.error("Exception in generic_TrimLeadingZeros :"+ e.getMessage());
        }
        return sreturn_text;
    }


    /*	To get the host OS name */
    public static Platform getCurretnPlatform(){
        if(platform == null){
            String osname = System.getProperty("os.name").toLowerCase();
            if(osname.contains("win")){
                platform = Platform.WINDOWS;
            } else if(osname.contains("nix") || osname.contains("nux") || osname.contains("aix")){
                platform=Platform.LINUX;
            } else if(osname.contains("mac")){
                platform=Platform.MAC;
            }
        }
        return platform;
    }

    /*	To get the ComputerName */
    public static String getComputerName() throws Exception{
        String hostname = "Unknown";
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex) {
            ex.fillInStackTrace();
            System.out.println("Hostname can not be resolved");
        }
        return (hostname);
    }

    public boolean testExecute(){
        String strExcute;
        strExcute = jsonUtils.getValue(jsonUtils.parseStringToJsonObject(configTestData.TEST_DATA_FILE_PATH),configTestData.testMethodName,"execute");
        return getBoolean(strExcute);
    }

    public boolean getBoolean(String str){

        if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("true") || str.equalsIgnoreCase("on")) {
             return true;
         } else {
             return false;
         }

    }

    public static Object resizeArray(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

    public String readFile(String filepath) throws IOException{
        File file = new File(filepath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while(line!=null){
                stringBuilder.append(line);
                stringBuilder.append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();

        } finally {
            bufferedReader.close();
        }
    }

    public String getSuiteXmlGroupName(String[] groupnames){
        String groupName=null;
        for(int i=0;i<=groupnames.length-1;i++){
            groupName = groupnames[0];
        }
        return groupName;

    }









//    public String[] readALMargs(){
//        JsonUtils jsonUtils=new JsonUtils();
//        String[] args=null;
//        try{
//            JSONObject jsonObject = jsonUtils.parseStringToJsonObject(ConfigTestData.workDir+readConfigProp("alm.config.file"));
//            args[0]=jsonUtils.getValue(jsonObject,"ALM","almURL");
//            args[1]=jsonUtils.getValue(jsonObject,"ALM","almUserName");
//            args[2]=jsonUtils.getValue(jsonObject,"ALM","almPassword");
//            args[3]=jsonUtils.getValue(jsonObject,"ALM","almDomain");
//            args[4]=jsonUtils.getValue(jsonObject,"ALM","almProject");
//            args[5]=jsonUtils.getValue(jsonObject,"ALM","almTestSetPath");
//            args[6]=jsonUtils.getValue(jsonObject,"ALM","almTestSetName");
//            args[7]=jsonUtils.getValue(jsonObject,"ALM","almTestSetID");
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return args;
//    }
    public String[] readALMargs(){

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
        args[0] = rbTestdata.getString("almURL");
        args[1]= rbTestdata.getString("almUserName");
        args[2] = rbTestdata.getString("almPassword");
        args[3] = rbTestdata.getString("almDomain");
        args[4] = rbTestdata.getString("almProject");
        args[5] = rbTestdata.getString("almTestSetPath");
        args[6] = rbTestdata.getString("almTestSetName");
        args[7] = rbTestdata.getString("almTestSetID");
        return args;
    }


    public static String getTestDataPath(String module){
       return MessageFormat.format(readConfigProp("testdata.path"),System.getProperty("user.dir"),module);
    }

    // Load the Config properties file from test source
    public static void loadConfigProp() throws Exception{
        File file = new File(ConfigTestData.CONFIG_PROP_FILE_PATH);
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        rbConfig = ResourceBundle.getBundle("config", Locale.getDefault(), loader);
    }
    // Read the Config properties file from test source
    public static String readConfigProp(String key){
        try {
            loadConfigProp();
        } catch (Exception e){
            e.printStackTrace();
        }
        return rbConfig.getString(key);
    }
    public void readMobileCapabilities(String mobileName){
        ClassLoader loader=null;
        System.out.println("mobile name:"+mobileName);
        try {
            File file = new File(System.getProperty("user.dir")+ File.separator + "src"+File.separator+"test"+File.separator+"resources"+File.separator+"config"+File.separator+"mobiles");
            URL[] urls = {file.toURI().toURL()};
            loader = new URLClassLoader(urls);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResourceBundle rbTestdata = ResourceBundle.getBundle(mobileName, Locale.getDefault(), loader);
        configTestData.mb_udid = rbTestdata.getString("UDID");
        configTestData.mb_deviceName= rbTestdata.getString("DeviceName");
        configTestData.mb_platformName = rbTestdata.getString("PlatformName");
        configTestData.mb_platformVersion = rbTestdata.getString("PlatformVersion");
        configTestData.mb_manufacturer = rbTestdata.getString("Manufacturer");
        configTestData.mb_appActivity = rbTestdata.getString("AppActivity");
    }


}
