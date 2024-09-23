package com.qa.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumBy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class ProductTests extends BaseTest{
	LoginPage loginPage;
	ProductsPage productsPage;
	JSONObject loginUsers;
	SettingsPage settingsPage;
	ProductDetailsPage productDetailsPage;
	TestUtils utils = new TestUtils();
	
	  @BeforeClass
	  public void beforeClass() throws IOException {
			InputStream datais = null;
		  try {
			  String dataFileName = "data/loginUsers.json";
			  datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
			  JSONTokener tokener = new JSONTokener(datais);
			  loginUsers = new JSONObject(tokener);
		  } catch(Exception e) {
			  e.printStackTrace();
			  throw e;
		  } finally {
			  if(datais != null) {
				  datais.close();
			  }
		  }
	}

	  @AfterClass
	  public void afterClass() {
	  }
	  
	  @BeforeMethod
	  public void beforeMethod(Method m) {
		  closeApp();
		  launchApp();
		  
		  utils.log().info("\n" + "******** starting test" + m.getName() + "********" + "\n");
	  }

	  
	  @AfterMethod
	  public void afterMethod() {
	  } 
	
	  @Test
	  public void validateProductOnProductsPage() {	
		  loginPage = new LoginPage();		  
		  productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"), 
				  loginUsers.getJSONObject("validUser").getString("password"));
		  
		  SoftAssert sa = new SoftAssert();

		  String slbTitle = productsPage.getSLBTitle();
		  sa.assertEquals(slbTitle, getStrings().get("product_page_slb_title"));
		  
		  String slbPrice = productsPage.getSLBPrice();
		  sa.assertEquals(slbPrice, getStrings().get("product_page_slb_price"));
		  
		  sa.assertAll();
		}
	  
	  @Test
	  public void validateProductOnProductsDetailsPage() {
		  loginPage = new LoginPage();		  
		  productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
				  loginUsers.getJSONObject("validUser").getString("password"));
		  
		  SoftAssert sa = new SoftAssert();
		  
		  productDetailsPage = productsPage.pressSLBTitle();
		  
		  String slbTitle = productDetailsPage.getSLBTitle();
		  sa.assertEquals(slbTitle, getStrings().get("product_details_slb_title"));
		  
		  String slbPrice = productDetailsPage.getSLBText();
		  sa.assertEquals(slbPrice, getStrings().get("product_details_slb_desc"));
		  
		  sa.assertAll();
		}
}
