package com.qa;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

//@Listeners(ExtentITestListenerAdapter.class)

public class BaseTest {
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	protected static ThreadLocal <Properties> props = new ThreadLocal<Properties>();
	protected static ThreadLocal <HashMap<String, String>> strings = new ThreadLocal <HashMap<String, String>>();
	protected static ThreadLocal <String> dateTime = new ThreadLocal<String>(); 
	protected static ThreadLocal <String> platform = new ThreadLocal<String>(); 
	protected static ThreadLocal <String> deviceName = new ThreadLocal<String>(); 

	private static AppiumDriverLocalService server;
	TestUtils utils = new TestUtils();
	
	public AppiumDriver getDriver() {
		return driver.get();
	}
	
	public void setDriver(AppiumDriver driver2) {
		driver.set(driver2);
	}
	
	public Properties getProps() {
		return props.get();
	}
	
	public void setProps(Properties props2) {
		props.set(props2);
	}
	
	public HashMap<String, String> getStrings() {
		return strings.get();
	}
	
	public void setStrings(HashMap<String, String> strings2) {
		strings.set(strings2);
	}
	
	public String getDateTime() {
		return dateTime.get();
	}
	
	public void setDateTime(String dateTime2) {
		dateTime.set(dateTime2);
	}
	
	public String getPlatform() {
		 return platform.get();
	  }
	  
	public void setPlatform(String platform2) {
		 platform.set(platform2);
	  }
	  
	public String getDeviceName() {
		 return deviceName.get();
	  }
	  
	public void setDeviceName(String deviceName2) {
		 deviceName.set(deviceName2);
	  }
	
	public BaseTest() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	@BeforeMethod
	public void beforeMethod() {
		((CanRecordScreen)getDriver()).startRecordingScreen();
	}
	
	@AfterMethod
	public synchronized void afterMethod(ITestResult result) {
		String media = ((CanRecordScreen)getDriver()).stopRecordingScreen();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params = (HashMap<String, String>) result.getTestContext().getCurrentXmlTest().getAllParameters();
		
		String dir = "Screenshots" + File.separator + params.get("platformName") + 
				"_" + params.get("deviceName") + File.separator + File.separator + 
				result.getTestClass().getRealClass().getSimpleName() + File.separator + 
				result.getName() + ".png";
		
		File videoDir = new File(dir);
		
		synchronized(videoDir){
			if(!videoDir.exists()) {
				videoDir.mkdirs();
			}
		} 
	
		
		try {
			FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
			stream.write(Base64.decode(media));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Base64DecodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Parameters({"platformName", "deviceName"})
	@BeforeTest
	public void beforeTest(String platformName, String deviceName) throws Exception {

		
		utils = new TestUtils();
		setDateTime(utils.DateTime());
		InputStream stringis = null;
		InputStream inputStream = null;
		AppiumDriver driver;
		Properties props = new Properties();
		try {
			props = new Properties();
			String propFileName = "config.properties";
			String xmlFileName = "strings/strings.xml";
			
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
			setProps(props);
			
			
			stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
			utils = new TestUtils();
			setStrings(utils.parseStringXML(stringis));
			
			//URL appURL = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
			String appURL = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
			
			UiAutomator2Options options = new UiAutomator2Options().setDeviceName(deviceName).
					setPlatformName(platformName).
					setAutomationName(props.getProperty("androidAutomationName")).
					setAppPackage(props.getProperty("androidAppPackage")).
					setAppActivity(props.getProperty("androidAppActivity")).
					setApp(appURL);
			
			URI url = new URI("http://0.0.0.0:4723");
			driver = new AndroidDriver(url.toURL(), options);
			setDriver(driver);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(inputStream !=null) {
				inputStream.close();
			} if(stringis != null) {
				stringis.close();
			}
		}
	}
	
	public void waitForVisibility(WebElement e) {
		WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
		wait.until(ExpectedConditions.visibilityOf(e));
	}
	
	public void click(WebElement e) {
		waitForVisibility(e);
		e.click();
		}
	
	public void sendKeys(WebElement e, String txt) {
		waitForVisibility(e);
		e.sendKeys(txt);
	}
	
	public String getAttribute(WebElement e, String attribute) {
		waitForVisibility(e);
		return e.getAttribute(attribute);
	}
	
	public String getText(WebElement e, String msg) {
		String txt = null;
		txt = getAttribute(e, "text");
		
		utils.log().info(msg + txt);
		ExtentReport.getTest().log(Status.INFO, msg + txt);
		return txt;
	  }
	
	public void closeApp() {
		((InteractsWithApps)driver).terminateApp(getProps().getProperty("androidAppPackage"));
	}
	
	public void launchApp() {
		((InteractsWithApps)driver).activateApp(getProps().getProperty("androidAppPackage"));
	}
	
	public WebElement scrollToElement() {
		  return getDriver().findElement(AppiumBy.androidUIAutomator(
				  "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
						  + "new UiSelector().description(\"test-Price\"));"));
	  }
	
	public Logger log() {
		return (Logger) LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
	}
	
	@BeforeSuite
	public void BeforeSuite() {
		ThreadContext.put("ROUTINGKEY", "ServerLogs");
		server = getAppiumService();
		
		if(!server.isRunning()) {
			server.start();
			server.clearOutPutStreams();
			//utils.log().info("Appium server started");
		} else {
			//utils.log().info("Server is running");
		}
		
	}
	
	@AfterSuite
	public void AfterSuite() {
		server.stop();
	}
	
	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}
	
	public AppiumDriverLocalService getAppiumService() {
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH", "");
		environment.put("ANDROID_HOME", "C:\\Users\\jdiezrodriguez\\AppData\\Local\\Android\\Sdk");
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder().
				usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
				.withAppiumJS(new File("path to appium server"))
				.usingPort(4723).withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				.withEnvironment(environment).withLogFile(new File("ServerLogs/server.log")));
	}
		

	@AfterTest
	public void afterTest() {
		if(getDriver() != null) {
			getDriver().quit();		
			}
	}

}
