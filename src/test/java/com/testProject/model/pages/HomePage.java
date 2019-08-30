package com.testProject.model.pages;

import com.testProject.constants.dataKey;
import com.testautomation.framework.base.ConfigTestData;
import com.testautomation.framework.generic.DriverBase;
import com.testautomation.framework.utils.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends DriverBase {
    private String homePage_Url;

    public HomePage(ConfigTestData configTestData){
        super(configTestData);
        PageFactory.initElements(this.driver,this);
        loadUrl();
    }

    //li[@class='header-right-menu-list-item']/a[@id='storeLocator-btn-area']
    @CacheLookup
    @FindBy(xpath = "//a[text()=\"Forgot your email address?\"]")
    public WebElement lnkForgotyouremailaddress;

    public void loadUrl(){
        switch (configTestData.testEnvironment.toUpperCase()){
            case "QA1":
                homePage_Url="https://www-qa1.shaws.com/home.html";
                break;
            case "QA2":
                homePage_Url="https://www-qa2.shaws.com/home.html";
                break;
        }
    }

    public boolean gotoHomePage(){
        configTestData.stepDescription = "Navigate to Home page.";

        Log.startLog("HomePage:::'gotoHomePage'");
        navigateToUrl(homePage_Url);
        Log.info("Navigated URL is :"+homePage_Url);
        Log.endLog("HomePage:::'gotoHomePage'");

        if (!verifyPageUrl(homePage_Url))
            return false;

        return true;
    }
}
