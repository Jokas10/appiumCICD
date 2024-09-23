 package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPage extends BaseTest{
	TestUtils utils = new TestUtils();

	@AndroidFindBy (accessibility = "test-Username") private WebElement usernameField;
	@AndroidFindBy (accessibility = "test-Password") private WebElement passwordField;
	@AndroidFindBy (accessibility = "test-LOGIN") private WebElement loginButton;
	@AndroidFindBy (xpath = "//android.widget.TextView[@text=\\\"Username "
			+ "and password do not match any user in this service.\\\"]") private WebElement errorText;
	
	public LoginPage(){
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	public LoginPage enterUsername(String username) {
		sendKeys(usernameField, username);
		return this;
	}
	
	public LoginPage enterPassword(String password) {
		sendKeys(passwordField, password);
		return this;
	}
	
	public String getError() {
		return getAttribute(errorText, "text");
	}
	
	public ProductsPage clickLogin() {
		click(loginButton);
		return new ProductsPage();
	}
	
	public ProductsPage login(String username, String password) {
		enterUsername(username);
		enterPassword(password);
		return clickLogin();
	}
}

