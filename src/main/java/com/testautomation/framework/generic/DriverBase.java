package com.testautomation.framework.generic;

import com.testautomation.framework.base.ConfigBase;
import com.testautomation.framework.base.ConfigTestData;
import com.testautomation.framework.constants.Constants;
import com.testautomation.framework.utils.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.TestException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DriverBase {

    public RemoteWebDriver driver;
    private static WebDriver webDriver=null;
    public ConfigTestData configTestData=null;
    public JavascriptExecutor jsexecutor;

    //private WebDriver driver=null;
    private String browserName;
    private final int timeout = 30;
    public int DEFAULT_TIMEOUT=500;
    private WebDriverWait wait=null;
    private static WebDriverWait webDriverWait=null;
    public static Robot robot;

    public DriverBase(ConfigTestData configTestData) {
        this.browserName=configTestData.testBrowser;
        this.driver = configTestData.driver;
        this.webDriver = configTestData.driver;
        this.configTestData = configTestData;
        jsexecutor = (JavascriptExecutor) this.driver;

    }


    /** ##################################################################
     *
     *                      BROWSER
     *
     * ##################################################################
     */


    /**
     * Method Description :: To get testing browser name (Ex: chrome,firefox)
     * @return
     */
    public String getBrowserName() {
        return browserName;
    }

    /**
     * Method Description :: It returns Web driver
     * @return
     */
    public WebDriver driver() {
        return driver;
    }

    /**
     * Method Description :: It Close the driver browser
     */
    public void close() {
        driver().close();
    }

    /**
     * Method Description :: It returns the element which is find in current page
     * @param locator
     * @return
     */
    public WebElement findElement(By locator) {
        return driver().findElement(locator);
    }

    /**
     * Method Description :: This method launches a new browser and opens
     * the specified URL in the browser instance
     * @param arg0
     */
    public void get(String arg0) {
        driver().get(arg0);
    }

    /**
     *  Method Description :: This method is used to retrieve the URL of the webpage the user is currently accessing
     * @return
     */
    public String getCurrentUrl() {
        return driver().getCurrentUrl();
    }

    /**
     * Method Description :: This method  is used to retrieve the page source
     * of the webpage the user is currently accessing
     * @return
     */
    public String getPageSource() {
        return driver().getPageSource();
    }

    /**
     * Method Description :: This method is used to retrieve the title of the webpage the user is currently working on.
     * @return
     */
    public String getTitle() {
        return driver().getTitle();
    }

    /**
     * Method Description :: This method is used to tackle with the situation when we have more than one window to deal with.
     * @return
     */
    public String getWindowHandle() {
        return driver().getWindowHandle();
    }

    /**
     * Method Description :: This method  is similar to that of getWindowHandle() with the subtle difference that it helps to deal with multiple windows i.e. when we have to deal with more than 2 windows.
     * @return
     */
    public Set getWindowHandles() {
        return driver().getWindowHandles();
    }

    /**
     * Method Description :: This method is Quits this driver instance, closing every associated window which is opened
     */
    public void quit() {
        driver().quit();
    }

    /**
     * Method Description :: This method is Load a new pages page in the current browser window.
     * @param URL
     */
    public void navigateToUrl(String URL){
        Log.info("Navigating to: " + URL);
        try{
            this.driver.navigate().to(URL);
        }catch (Exception e) {
            Log.error("Exception in navigateToUrl :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: To move back a single "item" in the pages browser's history.
     */
    public void navigateBack(){
        driver.navigate().back();
    }

    /**
     * Method Description :: To move a single "item" forward in the pages browser's history.
     */
    public void navigateForward(){
        driver.navigate().forward();
    }

    /**
     * Method Description :: It refreshes the current pages page
     */
    public void pageRefresh(){
        driver.navigate().refresh();
    }

    /**
     * Method Description :: This method is used for get current Page title
     * @return
     */
    public String getPageTitle() {
        try {
            Log.info("The title of the page is:" + driver.getTitle());
            return driver.getTitle();
        } catch (Exception e) {
            Log.error("Exception in getPageTitle :"+ e.getMessage());
            throw new TestException("URL did not load");
        }
    }
    /**
     * Method Description :: This method is handle the popup or alert messages with Accept or Decline actions
     * @param sAcceptOrDecline
     */
    public void handleAlert(String sAcceptOrDecline){
        try {

            wait = new WebDriverWait(driver, 30);
            if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
                Log.info("alert is not present, Not performing any operation");
            } else {
                Log.info("alert is present, Performing" + sAcceptOrDecline);
                // Handle the message window
                Alert alert = driver.switchTo().alert();

                if (sAcceptOrDecline.equals(Constants.C_ALERT_ACCEPT)) {
                    alert.accept();
                } else if (sAcceptOrDecline.equals(Constants.C_ALERT_DECLINE)) {
                    alert.dismiss();
                }
            }
        } catch (Exception e) {
            Log.error("Exception in generic_HandleAlert :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: This method  is used to move the window down
     * @param numberoftimes
     */
    public void scrollDown(Integer numberoftimes){
        try {
            for (int i = 0; i <= numberoftimes; i++) {
                driver.findElement(By.tagName("body"))
                        .sendKeys(Keys.ARROW_DOWN);
            }
            Thread.sleep(650);

        }catch (Exception e) {
            Log.error("Exception in generic_ScrollDown :"+ e.getMessage());
        }
    }/**
     * Method Description :: This method  is used to move the window up
     * @param numberoftimes
     */
    public void scrollUp(Integer numberoftimes){
        try {
            for (int i = 0; i <= numberoftimes; i++) {
                driver.findElement(By.tagName("body"))
                        .sendKeys(Keys.ARROW_UP);
            }
            Thread.sleep(650);

        }catch (Exception e) {
            Log.error("Exception in generic_ScrollUp :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: This method is wait upto page load
     */
    public synchronized void pageWaitLoad() {
        String str = null;
        try {
            str = (String) ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState");

        } catch (Exception e) {
            // it's need when JS isn't worked
            pageWaitLoad();
            return;
        }
        while (!str.equals("complete")) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            str = (String) ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState");
        }
    }

    /**
     * Method Description :: This method is switching controls from one window to the other.
     * @param iframeId
     * @return
     */
    public WebElement switchToNewWindow(String iframeId) {
        driver.switchTo().frame(iframeId);
        WebElement window = driver.switchTo().activeElement();
        return window;
    }

    /**
     * Method Description :: This method is get current page url and verifies with given url
     * @param Url
     * @return
     */
    public boolean verifyPageUrl(String Url){
        boolean returnStatus = true;
        String getpageUrl=driver.getCurrentUrl();
        if(Url.contains("https")){
            Url = Url.replaceAll("https://","");
        } else {
            Url = Url.replaceAll("http://","");
        }
        getpageUrl=getpageUrl.toLowerCase().trim();
        Url=Url.toLowerCase().trim();
        if(!getpageUrl.contains(Url)){
            returnStatus = false;
        }


        return returnStatus;
    }

    /**
     * Method Description :: This method is used get webpage responce code
     * @param url
     * @return
     */
    public int getStatusCode(String url){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getStatusLine().getStatusCode();
    }

    /**
     * Method Description :: This method is get the url of Link from href attribute and validate with status code : 200
     * @param link
     * @return
     */
    public boolean verifyLink(WebElement link){
        String href = link.getAttribute("href");
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(href);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.getStatusLine().getStatusCode()!=200){
            return false;
        } else{
            return true;
        }

    }

    /**
     * Method Description :: This method is switch tab within the browser
     */
    public void switchTab() {
        String parentHandle = driver.getWindowHandle(); // get the current window handle
        //Clicking on this window
        for (String winHandle : driver.getWindowHandles()) { //Gets the new window handle
            driver.switchTo().window(winHandle);        // switch focus of WebDriver to the next found window handle (that's your newly opened window)
        }
    }

    /**
     * Method Description :: This method is close tab within the browser
     */
    public void closeTab() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.close()");
    }

    /**
     * Method Description :: This method is close given tab within the browser
     * @param orignalTab
     */
    public void closeTab(String orignalTab) {
        for(String handle : driver.getWindowHandles()) {
            if (!handle.equals(orignalTab)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }
        driver.switchTo().window(orignalTab);
    }


    /** ##################################################################
     *
     *                      ELEMENT
     *
     * ##################################################################
     */
    /**
     * Method Description :: This Method will Click on provided WebElement
     * @param webElement : WebElement reference
     * @return
     */
    public void click(WebElement webElement){
        try{
            webElement.click();
        }catch (Exception e) {
            Log.error("Exception in click method :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: It can be used inside forms on elements, or form tags to submit that form.
     * @param webElement
     */
    public void submit(WebElement webElement){
        try{
            webElement.submit();
        }catch (Exception e) {
            Log.error("Exception in submit method :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: To enter text into the Text Fields and Password Fields
     * @param webElement
     * @param str
     */
    public void sendKeys(WebElement webElement,String str){
        try{
            webElement.sendKeys(str);
        }catch (Exception e) {
            Log.error("Exception in sendKeys method :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: This method is used to delete the text in an input box.
     * @param webElement
     */
    public void clear(WebElement webElement){
        try{
            webElement.clear();
        }catch (Exception e) {
            Log.error("Exception in clear method :"+ e.getMessage());
        }
    }

    /**
     *  Method Description :: It gets the tag name of the current element.
     * @param webElement
     * @return
     */
    public String getTagName(WebElement webElement){
        try{
            return webElement.getTagName();
        }catch (Exception e) {
            Log.error("Exception in getTagName method :"+ e.getMessage());
            throw new TestException("webElement is not exist"+webElement);
        }
    }

    /**
     *  Method Description :: This method is used to retrieve the value of the specified attribute
     * @param webElement
     * @param arg
     * @return
     */
    public String getAttribute(WebElement webElement,String arg){
        try{
            return webElement.getAttribute(arg);
        }catch (Exception e) {
            Log.error("Exception in getTagName method :"+ e.getMessage());
            throw new TestException("webElement is not exist"+webElement);
        }
    }

    /**
     * Method Description :: This method is used to verify if the pages element is selected or not.
     * @param webElement
     * @return
     */
    public boolean isSelected(WebElement webElement){
        boolean elmSelected=false;
        try{
            return webElement.isSelected();
        }catch (Exception e) {
            Log.error("Exception in isSelected method :"+ e.getMessage());
        }
        return elmSelected;
    }

    /**
     *  Method Description :: This method is used to verify if the pages element is enabled or disabled within the webpage.
     * @param webElement
     * @return
     */
    public boolean isEnabled(WebElement webElement){
        boolean elmEnabled=false;
        try{
            elmEnabled =webElement.isEnabled();
        }catch (Exception e) {
            elmEnabled=false;
            Log.error(webElement+"::: Exception in isEnabled method :"+ e.getMessage());
        }
        return elmEnabled;
    }

    /**
     *  Method Description :: This method is used to verify a presence of a pages element within the webpage
     * @param webElement
     * @return
     */
    public boolean isDisplayed(WebElement webElement){
        boolean elmDisplayed=false;
        try{
            elmDisplayed= webElement.isDisplayed();
        }catch (Exception e) {
            elmDisplayed=false;
            Log.error("Exception in isDisplayed method :"+ e.getMessage());
        }
        return elmDisplayed;
    }

    /**
     *  Method Description :: This method is used to verify a presence of a pages element within the webpage
     *
     * @return
     */
    public boolean isExist(WebElement webElement){
        boolean elmExist=false;
        WebElement element=null;
        try{
            element = waitForVisibility(webElement,Constants.MEDIUM_TIMEOUT);
            if(element!=null){
                elmExist=true;
            }
        }catch (Exception e) {
            elmExist=false;
            Log.error("Exception in isDisplayed method :"+ e.getMessage());
        }
        return elmExist;
    }

    /**
     * Method Description :: This method is used to retrieve the inner text of the specified pages element
     * @param webElement
     * @return
     */
    public String getText(WebElement webElement){
        String elementText = null;
        try{
            webDriverWait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
            if (webDriverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
                elementText= String.valueOf(webElement.getText());
            }
        }catch (Exception e) {
            Log.error("Exception in getText method :"+ e.getMessage());
        }
        return elementText;
    }

    /**
     *  Method Description :: This Method will selct on provided WebElement and then Key Enter
     * @param webElement : WebElement reference
     * @return
     */
    public void enter(WebElement webElement){
        try{
            webDriverWait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
            if (webDriverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
                webElement.sendKeys(Keys.ENTER);
            }
        }catch (Exception e) {
            Log.error("Exception in generic_Click_Enter :"+ e.getMessage());
        }
    }
    /**
     * Method Description ::  This Method will Click on corresponding element using Action Builder
     * @param webElement : WebElement reference
     * @return
     */
    public void action(WebElement webElement){
        try {
            webDriverWait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
            if (webDriverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
                Actions builder = new Actions(driver);
                builder.click(webElement).build().perform();
            }
        } catch (Exception e) {
            Log.error("Exception in generic_Click_Action :"+ e.getMessage());
        }
    }
    /**
     * Method Description :: This Method will enter value in Textbox
     * @param wTextBoxName : Textbox WebElement reference
     * @param sText : String type text which will be set in text box
     * @return
     */
    public boolean setValueToinputBox(WebElement wTextBoxName,String sText){
        boolean result = false;
        try {
            wTextBoxName.click();
            wTextBoxName.clear();
            wTextBoxName.sendKeys(sText);
            // Verify that value has been entered
            result=verifyInputBoxText(wTextBoxName,sText,Constants.VERIFY_TEXT_PARTIAL);
        } catch (Exception e) {

            Log.error("Exception in generic_SetTextTextBox :"+ e.getMessage());
        }
        return result;
    }

    /**
     * Method Description :: This method is used for select Item from combobox or List
     * @param wComboBoxName
     * @param sItemText
     * @return
     */
    public boolean selectItemInCombobox(WebElement wComboBoxName,String sItemText){
        boolean result = true;
        Log.info("Select " + sItemText + " in Combobox");
        try{
            Select datatype = new Select(wComboBoxName);
            datatype.selectByVisibleText(sItemText);
            // datatype.selectByValue(sItemText);
            Thread.sleep(5000);
            result = verifyItemInCombobox(wComboBoxName, sItemText);
        }catch (Exception e) {
            result=false;
            Log.error("Exception in generic_SelectItemInCombobox :"+ e.getMessage());
        }
        return result;
    }
    /**
     * Method Description :: This method is used for verify selected Item in combobox with given value
     * @param wComboBoxName
     * @param sExpectedItemText
     * @return
     */
    public boolean verifyItemInCombobox(WebElement wComboBoxName,String sExpectedItemText){
        String actualText;
        boolean result = true;
        try {
            // Get the selected value from specified combo box
            Select selectedValue = new Select(wComboBoxName);
            actualText = selectedValue.getFirstSelectedOption().getText();

            Log.info("commonVerifyItemInCombobox,actualText : " + actualText
                    + " , sExpectedItemText : " + sExpectedItemText);

            // Verify that selected item in combo box is same as expected
            // item
            if (actualText.equalsIgnoreCase(sExpectedItemText)) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            Log.error("Exception in generic_VerifyItemInCombobox :"+ e.getMessage());
            result = false;
        }
        return result;
    }
    /**
     * Method Description :: This method is used for check or uncheck the checkbox
     * @param wCheckBox
     * @param checkstatus
     * @return
     */
    public boolean checkUncheckCheckBox(WebElement wCheckBox,String checkstatus){
        boolean result = false;
        try {
            if (wCheckBox.isSelected() && checkstatus.equalsIgnoreCase(Constants.C_CHECK)){
                result=true;
            } else if(wCheckBox.isSelected()==false && checkstatus.equalsIgnoreCase(Constants.C_CHECK)){
                // Check check box
                wCheckBox.click();
                result = true;
            } else if(wCheckBox.isSelected()==false && checkstatus.equalsIgnoreCase(Constants.C_UNCHECK)){
                result = true;
            } else if (wCheckBox.isSelected() && checkstatus.equalsIgnoreCase(Constants.C_UNCHECK)) {
                // UnCheck check box
                wCheckBox.click();
                result = true;
            }
            Log.info(
                    "checkstatus:" + checkstatus + ",Result:" + result);

        }catch (Exception e) {
            Log.error("Exception in generic_CheckUncheckCheckBox :"+ e.getMessage());
            result = false;
        }
        return result;
    }
    /**
     * Method Description :: This Method is used to Verify Label Text. 1. EXACTMATCH: Compares this string to the specified object.2.EXACTMATCHIGNORECASE:Compares this String to another String ignoring case 3.PARTIAL:Returns true if and only if this string contains the specified
     * @param wLabelName
     * @param sExpectedText
     * @return
     */
    public boolean verifyLabelText(WebElement wLabelName,String sExpectedText,String verifyTextOptions){
        boolean result=true;
        String sTemp = null;
        try{
            sTemp = wLabelName.getText();
            result= Generic.verifyText(sTemp,sExpectedText,verifyTextOptions);
        }catch (Exception e) {
            result=false;
            Log.error("Exception in verifyText :"+ e.getMessage());
        }
        return result;
    }

    /**
     * Method Description :: This Method is used to Verify Inputbox Text or Value. 1. EXACTMATCH: Compares this string to the specified object.2.EXACTMATCHIGNORECASE:Compares this String to another String ignoring case 3.PARTIAL:Returns true if and only if this string contains the specified
     * @param winputBoxName
     * @param sExpectedText
     * @return
     */
    public boolean verifyInputBoxText(WebElement winputBoxName,String sExpectedText,String verifyTextOptions){
        boolean result=true;
        String sTemp = null;
        try{
            sTemp = winputBoxName.getAttribute("value");
            result= Generic.verifyText(sTemp,sExpectedText,verifyTextOptions);
        }catch (Exception e) {
            result=false;
            Log.error("Exception in verifyText :"+ e.getMessage());
        }
        return result;
    }

    /**
     * Method Description :: This method is used for upload a file in current webpage
     * @param sourceFile
     * @param fileName
     */
    public void chooseFiletoUpload(WebElement sourceFile, String fileName){
        String path = fileName;
        try {
            sourceFile.sendKeys(path);
            sourceFile.click();
            Thread.sleep(650);
        }catch (Exception e) {
            Log.error("Exception in generic_ChooseFiletoUpload :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: This method is used for get all Items from combobox
     * @param wComboboxName
     * @return
     */
    public List getAllElementInCombobox(WebElement wComboboxName){
        List comboboxElements = null;
        try {
            Select datatype = new Select(wComboboxName);
            comboboxElements = datatype.getOptions();
        } catch (Exception e) {
            Log.error("Exception in generic_TrimLeadingZeros :"+ e.getMessage());
        }
        return comboboxElements;
    }
    /**
     * Method Description ::  This Method will Click menuItem on provided WebElement
     * @param menu
     * @param subMenu
     */
    public void click_MenuItem(WebElement menu,WebElement subMenu){
        try {
            action(menu);
            Thread.sleep(3000);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", subMenu);

        } catch (Exception e) {
            Log.error("Exception in generic_Click_MenuItem :"+ e.getMessage());
        }
    }
    /**
     * Method Description :: This Method will Click date button and select the provided date.
     * @param datebutton
     * @param date
     * @return
     */
    public boolean datePicker(WebElement datebutton,String date) {
        boolean result = false, result1 = false, result2 = false;
        WebElement selectMonth, selectYear;
        String[] abc = date.split(",");
        try {
            action(datebutton);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            selectMonth = driver.findElement(By
                    .className("ui-datepicker-month"));
            // select month from the month drop down
            result1 = selectItemInCombobox(selectMonth, abc[0]);

            selectYear = driver.findElement(By.className("ui-datepicker-year"));

            result2 = selectItemInCombobox(selectYear, abc[2]);
            // result = result1 && result2;

            /*
             * DatePicker is a table.So navigate to each cell If a particular
             * cell matches value 13 then select it
             */

//            WebElement dateWidget = driver.findElement(By
//                    .className("ui-datepicker-calendar"));
//            List rows = dateWidget.findElements(By.tagName("tr"));
//            int rowsize = rows.size();
//            for (int i = 0; i < rowsize; i++) {
//                List columns = rows.get(i).findElements(
//                        By.tagName("td"));
//                int columnsize = columns.size();
//                for (int j = 0; j < columnsize; j++) {
//                    String datatext = columns.get(j).getText().toString();
//                    if (datatext.equalsIgnoreCase(abc[1])) {
//                        generic_Click_Action(columns.get(j));
//                    }
//
//                }
//
//            }
            result = true;

        } catch (Exception e) {
            Log.error("Exception in generic_datePicker :"+ e.getMessage());
        }

        return result;
    }

    /**
     * Method Description :: This method is used for click an Element using Java Script
     * @param element
     */
    public void click_JavaScript(WebElement element) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Log.error("Exception in generic_Click_JavaScript :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: Verify page url is Active or not
     * @param pageurl
     */
    public boolean verifyURLActive(String pageurl){
        boolean result=true;
        try {
            URL url = new URL(pageurl);
            HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
            httpURLConnect.setConnectTimeout(3000);
            httpURLConnect.connect();
            if(httpURLConnect.getResponseCode()==200){
                result=true;
                System.out.println(pageurl+" - "+httpURLConnect.getResponseMessage());
            }
            if(httpURLConnect.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND){
                result=false;
                System.out.println(pageurl+" - "+httpURLConnect.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
            }
        }catch (Exception e) {
            Log.error("Exception in verifyURLActive :"+ e.getMessage());
            result=false;
        }
        return result;
    }


    /**
     * Method Description :: This Method is used for click on link and verify the target page
     * @param objectReferenceId
     * @return
     */
    public boolean clickOnLink_VerifyTargetPage(WebElement objectReferenceId){
        boolean result = true;
        String href=objectReferenceId.getAttribute("href");
        //element.getAttribute(objectReferenceId,"href");
        String target=objectReferenceId.getAttribute("target");
        //element.getAttribute(objectReferenceId,"target");
        String originalHandle = driver.getWindowHandle();
        System.out.println("href::"+href);
        System.out.println("target::"+target);
        if (isDisplayed(objectReferenceId)) {
            click_JavaScript(objectReferenceId);
            //  waitFor(6000);
        } else {
            result = false;
        }
        pageWaitLoad();
        if(target.equalsIgnoreCase("_blank")){
            switchTab();
            sleep(5);
            if (!verifyPageUrl(href)) {
                result = false;
            }
            closeTab(originalHandle);
        } else {
            if (!verifyPageUrl(href)) {
                result = false;
            }
            navigateBack();
        }
        return result;
    }
    /**
     * Method Description :: This method is validate the Alert Message
     * @param wLabelName
     * @param sMessageTextToVerify
     * @return
     */
    public boolean verifyAlertTextMessage(WebElement wLabelName,String sMessageTextToVerify){
        boolean result=true;
        try{
            result=verifyLabelText(wLabelName,sMessageTextToVerify,Constants.VERIFY_TEXT_PARTIAL);
        }catch (Exception e) {
            result=false;
            Log.error("Exception in generic_VerifyAlertTextMessage :"+ e.getMessage());
        }
        return result;
    }

    /**
     * Method Description :: This method is used for control move to Element
     * @param element
     */
    public void moveToElement(WebElement element){
        if(configTestData.testBrowser.equalsIgnoreCase("firefox") || configTestData.testBrowser.equalsIgnoreCase("ie")){
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }else{
            Actions dragger = new Actions(driver);
            dragger.moveToElement(element).build().perform();
        }
    }
    /**
     * Method Description :: This method is used for control move to Element and click that Element
     * @param element
     */
    public void moveToElementAndClick(WebElement element){
        Actions dragger = new Actions(driver);
        dragger.moveToElement(element).click().build().perform();;
    }

    /**
     * Method Description :: This method is used for double click on element
     * @param element
     */
    public void doubleClick(WebElement element){
        Actions dragger = new Actions(driver);
        dragger.doubleClick().build().perform();;
    }

    /**
     * Method Description :: This method is used for scroll to Element
     * @param element
     */
    public void scrollToElement(WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);;window.scrollBy(0, -120)", element);
    }

    /**
     * Method Description :: This method is used for scroll to Top of the pages page
     */
    public void scrollToPageTop() {
        if(configTestData.testBrowser.equalsIgnoreCase("firefox") || configTestData.testBrowser.equalsIgnoreCase("edge")){
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
        }else{
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
        }
    }
    /**
     * Method Description :: This method is used for scroll to bottom of the pages page
     */
    public void scrollToPageBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000)");
    }


    /** ##################################################################
     *
     *                      KEYBOARD
     *
     * ##################################################################
     */

    /**
     * Method Description :: Press Key
     * @param keyEvent
     */
    public void keyPress(int keyEvent){
        try {
            Robot robot = new Robot();

            Thread.sleep(1000);
            robot.keyPress(keyEvent);
            robot.keyRelease(keyEvent);
            Thread.sleep(1000);
        }catch (Exception e){
            Log.error("Exception in keyPress :"+ e.getMessage());
        }
    }
    /*  using Robot */

    /**
     * Method Description :: To Press ENTER Key
     */
    public void hitEnter(){
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }catch (Exception e){
            Log.error("Exception in hitEnter :"+ e.getMessage());
        }

    }

    /**
     * Method Description :: To Press BACKSPACE Key
     */
    public void hitBackspace() {
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        } catch (Exception e){
            Log.error("Exception in hitBackspace :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: To Press DELETE Key
     */
    public void hitDelete() {
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_DELETE);
            robot.keyRelease(KeyEvent.VK_DELETE);
        }catch (Exception e){
            Log.error("Exception in hitBackspace :"+ e.getMessage());
        }
    }

    /**
     * Method Description :: To Select all the Text/Web Elements
     */
    public void selectAll() {
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_A);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }
    }
    /**
     * Method Description :: To Copy all the Selected Text/Web Elements
     */
    public void copyAll() {
        try{
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_C);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }

    }

    /**
     * Method Description :: To Paste all the Selected Text/Web Elements
     */
    public void pasteAll(){
        try{
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }

    }
    /**
     * Method Description :: To ZoomOut
     */
    public void robotZoomOut(){
        try{
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }

    }

    /**
     * Method Description :: To ZoomIn
     */
    public static void robotZoomIn(){
        try{
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ADD);
            robot.keyRelease(KeyEvent.VK_ADD);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }

    }
    /**
     * Method Description :: To ScrollUp
     */
    public static void robotScrollUp(){
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_PAGE_UP);
            robot.keyRelease(KeyEvent.VK_PAGE_UP);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }
    }
    /**
     * Method Description :: To ScrollDown
     */
    public void robotScrollDown(){
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
        }catch (Exception e){
            Log.error("Exception in selectAll :"+ e.getMessage());
        }
    }

    /** ##################################################################
     *
     *                      MOUSE
     *
     * ##################################################################
     */

    /**
     * Method Description :: To mouse hover on element
     * @param referenceElement
     */
    public void hover(WebElement referenceElement) {
        try {
            Point point;
            Robot robot = new Robot();

            point = referenceElement.getLocation();
            Thread.sleep(1000);
            robot.mouseMove(point.getX() - 10, point.getY() - 5);
            Thread.sleep(1000);
        }catch (Exception e){

        }
    }

    /**
     * Method Description :: To mouse click on pages element
     * @param referenceElement
     * @param mouseButton
     */
    public void click(WebElement referenceElement, String mouseButton){
        Actions oAction = new Actions(driver);
        switch (mouseButton.toLowerCase()) {
            case "left":
                oAction.build().perform();
                break;
            case "right":
                oAction.contextClick(referenceElement).build().perform();
                break;
        }

    }
    /* To Move cursor to the Desired Location */

    /**
     *  Method Description :: To move mouse Cursor on X and Y position
     * @param X_Position
     * @param Y_Position
     */
    public void moveCursor(int X_Position, int Y_Position)  {
        robot.mouseMove(X_Position, Y_Position);
    }

    /** ##################################################################
     *
     *                      WAIT
     *
     * ##################################################################
     */

    /**
     * Method Description :: Wait To Load pages page
     * @param waitTime
     */
    public void ToLoad(int waitTime) {
        driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
    }

    /**
     * Method Description :: Wait for Element Visibility of given amount of Time
     * @param element
     * @param timeout
     * @return
     */
    public static WebElement waitForVisibility(WebElement element, int timeout) {
        element = new WebDriverWait(webDriver,timeout).until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    /**
     * Method Description :: Wait for Element INVisibility of given amount of Time
     * @param element
     * @param timeout
     * @return
     */
    public static boolean waitForInVisibility(WebElement element, int timeout){
        return new WebDriverWait(webDriver, timeout).until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Method Description :: Wait for Element INVisibility of 20 Seconds
     * @param element
     * @return
     */
    public static boolean waitForInVisibility(WebElement element){
        return new WebDriverWait(webDriver, Constants.MEDIUM_TIMEOUT).until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Method Description :: Wait for Element Visibility of 20 Seconds
     * @param element
     * @return
     */
    public static WebElement waitForVisibility(WebElement element){
        element = new WebDriverWait(webDriver,Constants.MEDIUM_TIMEOUT).until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    /**
     * Method Description :: Wait for List of Elements Visibility of 20 Seconds
     * @param elements
     * @return
     */
    public static List<WebElement> waitForVisibility(List<WebElement> elements){
        elements = new WebDriverWait(webDriver,Constants.MEDIUM_TIMEOUT).until(ExpectedConditions.visibilityOfAllElements(elements));
        return elements;
    }

    /**
     * Method Description :: Wait for List of Elements Visibility of given amount of Time
     * @param driver
     * @param elements
     * @param timeout
     * @return
     */
    public static List<WebElement> waitForVisibility(WebDriver driver, List<WebElement> elements,int timeout) {
        elements = new WebDriverWait(driver,timeout).until(ExpectedConditions.visibilityOfAllElements(elements));
        return elements;
    }

    /**
     * Method Description :: Wait for clickable of Element Visibility of given amount of Time
     * @param element
     * @param timeout
     * @return
     */
    public static WebElement waitForClickability(WebElement element, int timeout){
        waitUntilElementIsDisplayed(element);
        element = new WebDriverWait(webDriver,timeout).until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    /**
     * Method Description :: Wait for clickable of Element Visibility of 20 seconds
     * @param driver
     * @param element
     * @return
     */
    public static WebElement waitForClickability(WebDriver driver, WebElement element){
        waitUntilElementIsDisplayed(element);
        element = new WebDriverWait(driver,Constants.MEDIUM_TIMEOUT).until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    /**
     * Method Description :: Wait until element is displayed
     * @param element
     * @return
     */
    public static boolean waitUntilElementIsDisplayed(WebElement element){
        int counter = 0;
        boolean result = false;

        while(counter <= 2){
            try{
                if(element.isDisplayed()){
                    result = true;
                    break;
                }
            }catch(NoSuchElementException | StaleElementReferenceException e){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            counter++;
        }

        return result;
    }
    /**
     * Method Description :: Sleep given amount of time
     * @param timeInSeconds
     */
    public static void sleep(int timeInSeconds) {
        try {
            Thread.sleep(timeInSeconds * 1000);
        } catch (InterruptedException e) {
            Log.error("TimeoutException in generic_Wait for :"+ e.getMessage());
        }
    }
}
