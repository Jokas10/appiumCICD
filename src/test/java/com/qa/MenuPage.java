package com.qa;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class MenuPage extends BaseTest{
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView") private WebElement settingsButton;
	
	public MenuPage() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	public SettingsPage pressSettingsButton() {
		click(settingsButton);
		return new SettingsPage();

	}

}
