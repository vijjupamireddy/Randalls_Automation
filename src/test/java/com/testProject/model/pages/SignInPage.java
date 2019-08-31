package com.testProject.model.pages;


import com.testProject.constants.dataKey;
import com.testautomation.framework.base.ConfigTestData;
import com.testautomation.framework.generic.DriverBase;
import com.testautomation.framework.utils.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage extends DriverBase {

    private String signIn_Url;

    public SignInPage(ConfigTestData configTestData) {
        super(configTestData);
        PageFactory.initElements(this.driver,this);
    }

    @CacheLookup
    @FindBy(xpath = "//img[@class='uca-header-logo-randalls']")
    public WebElement imgRandalls;

    @CacheLookup
    @FindBy(xpath = "//h2[text()='Sign In']")
    public WebElement textsignIn;

    @CacheLookup
    @FindBy(xpath = "//label[text()='Email Address']")
    public WebElement textEmailAddress;

    @CacheLookup
    @FindBy(xpath = "//input[@id='label-email']")
    public WebElement enterEmailAddress;

    @CacheLookup
    @FindBy(xpath = "//label[text()='Password']")
    public WebElement textPassword;

    @CacheLookup
    @FindBy(xpath = "//input[@id='label-password']")
    public WebElement enterPassword;

    @CacheLookup
    @FindBy(xpath = "//div[@id='cShow']")
    public WebElement linkShow;


    @CacheLookup
    @FindBy(xpath = "//label[text()='Keep Me Signed In']")
    public WebElement textKeepMeSignedIn;

    @CacheLookup
    @FindBy(xpath = "//p[text()='For your security, we do not recommend checking this box if you are using a public device.']")
    public WebElement textSecurityWarning;

    @CacheLookup
    @FindBy(xpath = "//input[@id='btnSignIn']")
    public WebElement btnSignIn;

    @CacheLookup
    @FindBy(xpath = "//li[@role='presentation']//a[text()='Create Account']")
    public WebElement linkcreateAccount;

    @CacheLookup
    @FindBy(xpath = "//a[text()='Forgot Password']")
    public WebElement linkforgotPassword;


}
