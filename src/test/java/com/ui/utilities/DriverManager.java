package com.ui.utilities;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


public class DriverManager {
	public static ThreadLocal<WebDriver> dr = new ThreadLocal<WebDriver>();

	private static Integer loadTimeOut = 30;
	private static Integer elementTimeOut = 10;
	private static String reportDirectory;
	private static Boolean storyMode;
	public enum RunType { LOCAL, SAUCE };
	private static RunType runType;
	private static String slackHook;
	private static String qaEmailAddress;
	private static String gmailAPIProjectName;
	private static String gmailCredentialsPath;
	private static String gmailTokensPath;
	private static String browser;

	public static String getReportDirectory() {
		return reportDirectory;
	}
	public static void setReportDirectory(String reportDirectory) {
		DriverManager.reportDirectory = reportDirectory;
	}
	public static Integer getElementTimeOut() {
		return elementTimeOut;
	}
	public static void setElementTimeOut(Integer elementTimeOut) {
		if (elementTimeOut != null) {
			DriverManager.elementTimeOut = elementTimeOut;
		}
	}
	public static Integer getLoadTimeOut() {
		return loadTimeOut;
	}
	public static void setLoadTimeOut(Integer loadTimeOut) {	
		if (elementTimeOut != null) {
			DriverManager.loadTimeOut = loadTimeOut;
			DriverManager.getDriver().manage().timeouts().implicitlyWait(loadTimeOut, TimeUnit.SECONDS);
		}
	}
	public static WebDriver getDriver() {
		return dr.get();
	}
	public static void setWebDriver(RemoteWebDriver driver) {
		dr.set((WebDriver) driver);
	}
	public static Boolean getStoryMode() {
		return storyMode;
	}
	public static void setStoryMode(Boolean storyMode) {
		DriverManager.storyMode = storyMode;
	}
	public static RunType getRunType() {
		return runType;
	}
	public static void setRunType(RunType runType) {
		DriverManager.runType = runType;
	}
	public static String getCurrentURL() {
		return getDriver().getCurrentUrl();
	}
	public static void setCurrentURL(String currentURL) {
		getDriver().get(currentURL);
	}
	public static String getSlackHook() {
		return slackHook;
	}
	public static void setSlackHook(String slackHook) {
		DriverManager.slackHook = slackHook;
	}
	public static boolean slackEnabled() {
		return DriverManager.slackHook.length() > 0;
	}
	public static void setGmailAccount(String qaEmailAddress){ DriverManager.qaEmailAddress = qaEmailAddress;}
	public static String getGmailAccount(){return qaEmailAddress;}
	public static void setGmailAPIProjectName(String gmailAPIProjectName){ DriverManager.gmailAPIProjectName = gmailAPIProjectName;}
	public static String getGmailAPIProjectName(){ return gmailAPIProjectName;}
	public static void setGmailAPICredentialsPath(String gmailCredentialsPath){DriverManager.gmailCredentialsPath = gmailCredentialsPath;}
	public static String getGmailAPICredentialsPath(){return gmailCredentialsPath;}
	public static void setGmailAPITokensPath(String gmailTokensPath){ DriverManager.gmailTokensPath = gmailTokensPath;}
	public static String getGmailAPITokensPath(){return gmailTokensPath;}
	
	public static String getBrowser() {
		return browser;
	}
	public static void setBrowser(String browser) {
		DriverManager.browser = browser;
	}
}
