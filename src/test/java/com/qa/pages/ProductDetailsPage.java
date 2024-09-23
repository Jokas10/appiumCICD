package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ProductDetailsPage extends MenuPage{
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]") private WebElement SLBTitle;
	@AndroidFindBy (xpath = "//android.widget.TextView[@text=\"carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.\"]") 
	private WebElement SLBText;
	@AndroidFindBy (xpath= "//android.widget.TextView[@text=\"BACK TO PRODUCTS\"]") private WebElement backToProduct;
	
	public ProductDetailsPage(){
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	public String getSLBTitle() {
		String title = getText(SLBTitle, "title is - ");
		return title;
	}
	
	public String getSLBText() {
		String txt = getText(SLBText, "txt is - ");
		return txt;
	}
	
	public String scrollToSLBPriceAndGetSLBPrice() {
		return getText(scrollToElement(), "");
	}

	
	public ProductsPage pressBacktoProduct() {
		click(backToProduct);
		return new ProductsPage();
	}

}
