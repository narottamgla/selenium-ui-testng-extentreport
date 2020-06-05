package com.ui.utilities;


public class DriverFactory {
	
	private static String chromeDriverExePath;
	private static String IEDriverExePath;
	private static String configPropertyFile;
	private static Boolean isRemote;

	public static Boolean getIsRemote() {
		return isRemote;
	}
	public static void setIsRemote(Boolean isRemote) {
		DriverFactory.isRemote = isRemote;
	}
	public static String getChromeDriverExePath() {
		return chromeDriverExePath;
	}
	public static void setChromeDriverExePath(String chromeDriverExePath) {
		DriverFactory.chromeDriverExePath = chromeDriverExePath;
	}
	public static String getIEDriverExePath() {
		return IEDriverExePath;
	}
	public static void setIEDriverExePath(String iEDriverExePath) {
		IEDriverExePath = iEDriverExePath;
	}
	public static String getConfigPropertyFile() {
		return configPropertyFile;
	}
	public static void setConfigPropertyFile(String configPropertyFile) {
		DriverFactory.configPropertyFile = configPropertyFile;
	}

}
