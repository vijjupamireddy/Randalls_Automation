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



}
