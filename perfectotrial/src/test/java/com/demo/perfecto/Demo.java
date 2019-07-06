package com.demo.perfecto;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class Demo {

	AppiumDriver driver;
	WebDriverWait wait;
	String os;
	String appName;
	final int interval = 30000; // The interval of time to watch the trailer. *currently 30 seconds.

	@BeforeTest
	@Parameters({ "host", "securityToken", "deviceModel", "platformName", "appName" })
	public void beforeTest(String host, String securityToken, String deviceModel, String platformName, String appName)
			throws MalformedURLException, UnsupportedEncodingException {

		this.os = platformName;
		this.appName = appName;
		
		System.out.println("Run started");
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("securityToken", securityToken);
			capabilities.setCapability("platformName", platformName);
			capabilities.setCapability("model", deviceModel);

			if (os.equals("iOS")) {
				driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
			} else {
				driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"),
						capabilities);
			}

			wait = new WebDriverWait(driver, 10);
			driver.context("NATIVE_APP");
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void runTest() throws InterruptedException {

		try {
			openApp();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Issue opening app");
		}
		performCalculation();
	}

	@AfterClass
	public void afterClass() throws InterruptedException {

		closeApp();
		driver.close();
		System.out.println("Closed Device:" + os);

	}

	private void openApp() throws InterruptedException {

		Map<String, Object> script = new HashMap<String, Object>();
		script.put("name", appName);
		driver.executeScript("mobile:application:open", script);
		System.out.println("Opened " + appName + " on " + this.os);
		Thread.sleep(this.interval);
	}

	private void performCalculation() throws InterruptedException {
		
		if(os.equalsIgnoreCase("iOS")) {
			
			returnElement(Xpaths.IOS_BTN_CLEAR).click();
			returnElement(Xpaths.IOS_BTN_7).click();
			returnElement(Xpaths.IOS_BTN_MULTIPLY).click();
			returnElement(Xpaths.IOS_BTN_5).click();
			returnElement(Xpaths.IOS_BTN_EQUALS).click();
			Assert.assertEquals(returnElement(Xpaths.IOS_FIELD_RESULT).getText(), "35");
		}
		else {
			
			returnElement(Xpaths.ANDROID_BTN_CLEAR).click();
			returnElement(Xpaths.ANDROID_BTN_7).click();
			returnElement(Xpaths.ANDROID_BTN_MULTIPLY).click();
			returnElement(Xpaths.ANDROID_BTN_5).click();
			returnElement(Xpaths.ANDROID_BTN_EQUALS).click();
			Assert.assertEquals(returnElement(Xpaths.ANDROID_FIELD_RESULT).getText(), "35");
		}

		
		

	}

	private void closeApp() throws InterruptedException {

		Map<String, Object> script = new HashMap<String, Object>();
		script.put("name", appName);
		driver.executeScript("mobile:application:close", script);
		System.out.println("Closed " + appName + " on " + this.os);
		// Thread.sleep(this.interval);
	}

	private WebElement returnElement(String xpath) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		return driver.findElement(By.xpath(xpath));
	}

}
