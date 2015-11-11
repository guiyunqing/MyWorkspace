package com.android.uitest.AndroidTestDemo;

import com.android.uiautomator.core.UiObjectNotFoundException;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;
 
public class AndroidTestDemo extends UiAutomatorTestCase {
 

    public void testDemo() throws UiObjectNotFoundException {

        getUiDevice().pressHome();
 
       
    }
}