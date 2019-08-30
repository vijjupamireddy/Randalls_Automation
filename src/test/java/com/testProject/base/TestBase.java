package com.testProject.base;

import com.aventstack.extentreports.Status;
import com.testProject.model.screens.HomeScreen;
import com.testProject.model.pages.RegistrationPage;
import com.testProject.model.pages.SignInPage;
import com.testautomation.framework.base.ConfigBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.ResourceBundle;

public class TestBase extends ConfigBase {
    ResourceBundle rb =null;

    // Web page object model
    public SignInPage signInPage  = null;
    public RegistrationPage registrationPage  = null;
    // Mobile screen object model
    public HomeScreen homeScreen = null;


    @BeforeMethod(alwaysRun = true)
    public void initTestMethods(){
        initilizeClasses();
    }


    public void initilizeClasses(){
        //Page Initilize
        signInPage = new SignInPage(configTestData);
        registrationPage = new RegistrationPage(configTestData);

        //screen Initilize
        homeScreen = new HomeScreen(configTestData);
    }


}
