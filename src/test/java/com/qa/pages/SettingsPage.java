package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SettingsPage extends BaseTest{
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (accessibility = "test-LOGOUT") private WebElement logoutButton;
	
	public SettingsPage(){
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	public LoginPage pressLogoutButton() {
		click(logoutButton);
		return new LoginPage();
	}

}
