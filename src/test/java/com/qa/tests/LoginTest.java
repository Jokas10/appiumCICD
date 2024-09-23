package com.qa.tests;

import org.testng.annotations.Test;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
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

public class LoginTest extends BaseTest{
	LoginPage loginPage;
	ProductsPage productsPage;
	JSONObject loginUsers;
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
		  
		  loginPage = new LoginPage();
		  utils.log().info("\n" + "******** starting test" + m.getName() + "********" + "\n");
	  }

	  
	  @AfterMethod
	  public void afterMethod() {
	  } 
	
	  @Test
	  public void invalidUsername() {	
		  loginPage.enterUsername(loginUsers.getJSONObject("invalidUser").getString("username"));
		  loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
		  loginPage.clickLogin();		 
		  
		  String actualErrorText = loginPage.getError();
		  String expectedErrorText = getStrings().get("err_invalid_username_or_password");
			
		  assertEquals(actualErrorText, expectedErrorText);
		}
		
	  @Test
	  public void invalidPassword() {
		  loginPage.enterUsername(loginUsers.getJSONObject("invalidPassword").getString("username"));
		  loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
		  loginPage.clickLogin();	
			
			
		  String actualErrorText = loginPage.getError();
		  String expectedErrorText = getStrings().get("err_invalid_username_or_password");
			
		  assertEquals(actualErrorText, expectedErrorText);
		}
		
	  @Test
	  public void succesfulLogin() {
			loginPage.enterUsername(loginUsers.getJSONObject("validUser").getString("username"));
			loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
			productsPage = loginPage.clickLogin();	
			
			String actualProduct = productsPage.getTitle(); 
			String expectedProduct = getStrings().get("product_title");
			
			assertEquals(actualProduct, expectedProduct);
		}
}
