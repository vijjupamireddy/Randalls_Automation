package com.testProject.model.pages;

import com.testProject.constants.dataKey;
import com.testautomation.framework.base.ConfigTestData;
import com.testautomation.framework.generic.DriverBase;
import com.testautomation.framework.utils.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class RegistrationPage extends DriverBase {

    String registrationUrl;

    public RegistrationPage(ConfigTestData configTestData){
        super(configTestData);
        PageFactory.initElements(this.driver,this);
    }

    @CacheLookup
    @FindBy(xpath = "//a/ng-transclude/span[text()=\"Terms of Use\"]")
    public WebElement lnkTermsofUse;

    @CacheLookup
    @FindBy(xpath = "//a/ng-transclude/span[text()=\"Privacy Policy\"]")
    public WebElement lnkPrivacyPolicy;

    @CacheLookup
    @FindBy(xpath = "//button[@aria-label='search for store']")
    public WebElement btnSearchforStore;

    @CacheLookup
    @FindBy(id="create-account-btn")
    public WebElement btnCreateMyAccount;


    @CacheLookup
    @FindBy(id="subscription-opt-in")
    public WebElement chkboxsubscriptionoptin;

    @CacheLookup
    @FindBy(id="show-password-checkbox")
    public WebElement chkboxshowpasswordcheckbox;

    @CacheLookup
    @FindBy(id="userAcceptsTermsAndPolicy")
    public WebElement chkboxuserAcceptsTermsAndPolicy;


    @CacheLookup
    @FindBy(id="input-firstName")
    public WebElement inputFirstName;

    @CacheLookup
    @FindBy(id="input-lastName")
    public WebElement inputLastName;

    @CacheLookup
    @FindBy(id="input-clubCardNumber")
    public WebElement inputclubCardNumber;

    @CacheLookup
    @FindBy(id="input-email")
    public WebElement inputemail;

    @CacheLookup
    @FindBy(id="showPass")
    public WebElement inputpasswordpassword;

    @CacheLookup
    @FindBy(id="value-userEnteredAddress")
    public WebElement inputvalueuserEnteredAddress;


    @CacheLookup
    @FindBy(id="selectAStore")
    public WebElement cmboxselectAStore;


    //Operations
    public void enterFirstName(String firstName){
        inputFirstName.sendKeys(firstName);
    }
    public void enterLastName(String lastName){
        inputLastName.sendKeys(lastName);
    }
    public void enterCCNumberorPhone(String ccOrPhone){
        inputclubCardNumber.sendKeys(ccOrPhone);
    }
    public void enterEmailAddress(String emailAddress){
        inputemail.sendKeys(emailAddress);
    }
    public void enterPassword(String password){
        inputpasswordpassword.sendKeys(password);
    }
    public void enterStore(String store){
        inputvalueuserEnteredAddress.sendKeys(store);
    }
    public void clickStoreSerach(){btnSearchforStore.click();}
    public void selectStoreAddress(String storeAddress){
        Select drop = new Select(cmboxselectAStore);
        drop.selectByValue(storeAddress);
    }
    public void clickCreateAccount(){btnCreateMyAccount.click();}

    public void loadURL(){
        switch (configTestData.testEnvironment.toUpperCase()){
            case "QA1":
                registrationUrl="https://www-qa1.randalls.com/account/short-registration.html";
                break;
            case "QA2":
                registrationUrl="https://www-qa2.randalls.com/account/short-registration.html";
                break;
        }


    }




    public boolean gotoRegistraionPage(){
        configTestData.stepDescription = "Navigate to Registration page.";
        loadURL();
        Log.startLog("RegistrationPage:::'gotoRegistraionPage'");
        navigateToUrl(registrationUrl);
        Log.info("Navigated URL is :"+registrationUrl);

        if (!verifyPageUrl(registrationUrl)) {
            return false;
        }
        Log.endLog("RegistrationPage:::'gotoRegistraionPage'");
        return true;
    }

    public boolean userRegister(String[] args){
        configTestData.stepDescription = "Enter Valid data in registration form";
        Log.startLog("RegistrationPage:::'userRegister'");
        enterFirstName(args[0]);
        enterLastName(args[1]);
        enterCCNumberorPhone(args[2]);
        enterEmailAddress(args[3]);
        enterPassword(args[4]);
        enterStore(args[5]);
        clickStoreSerach();
        clickCreateAccount();
        Log.endLog("RegistrationPage:::'userRegister'");
        return true;
    }
}
