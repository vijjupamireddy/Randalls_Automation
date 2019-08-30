package com.testProject.testsuites.mobile.demo;


import com.testProject.base.TestBase;
import org.testng.annotations.Test;

public class Demo1 extends TestBase {


    @Test(groups={"Demo"})
    public void Demo1() throws Exception {

            //Variable Declarion
            System.out.println("Driver::"+configTestData.driver);


            // Step 1: Go to Just for U Tab
            verify(homeScreen.gotoJustForU(), "Just for u is displayed");
            // Step 2: Go to My Store Tab
            verify(homeScreen.gotoMyStore(), "My Store is displayed");


    }

}
