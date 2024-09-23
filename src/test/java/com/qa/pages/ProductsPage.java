 package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.qa.BaseTest;
import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ProductsPage extends MenuPage{
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (xpath = "//android.widget.TextView[@text=\\\"PRODUCTS\\\"]") private WebElement productText;
	@AndroidFindBy (xpath = "//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]") private WebElement SLBTitle;
	@AndroidFindBy (xpath = "//android.widget.TextView[@content-desc=\"test-Price\" and @text=\"$29.99\"]") private WebElement SLBPrice;

	public ProductsPage(){
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	public String getTitle() {
		return getAttribute(productText, "text");
	}
	
	public String getSLBTitle() {
		return getText(SLBTitle, "title is - ");
	}
	
	public String getSLBPrice() {
		return getText(SLBPrice, "price is - ");
	}
	
	public ProductDetailsPage pressSLBTitle() {
		click(SLBTitle);
		return new ProductDetailsPage();
	}
}

