package com.ui.listeners;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.ui.utilities.DriverManager;


public class ExtentManager {

	private static ExtentReports extent;
	public static String reportFilePath = "";

	    public static ExtentReports createInstance(String fileName) {
	    	System.out.println(System.getProperty("os.name"));

	    	if(System.getProperty("os.name").contains("Mac")) {
	    		reportFilePath = System.getProperty("user.dir") + "/src/test/resources/reports/";
	    	} else {
	    		reportFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\reports\\";	    		
	    	}
	        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath + fileName);
	        System.out.println("Report being written to: " + reportFilePath);
	        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
	        htmlReporter.config().setChartVisibilityOnOpen(true);
	        htmlReporter.config().setTheme(Theme.STANDARD);
	        htmlReporter.config().setDocumentTitle(fileName);
	        htmlReporter.config().setEncoding("utf-8");
	        htmlReporter.config().setReportName(fileName);
	        
	        extent = new ExtentReports();
	        extent.attachReporter(htmlReporter);
	        	        
	        return extent;
	    }

	    
	    public static String screenshotPath;
		public static String screenshotName;
		
		public static void captureScreenshot() {

			File scrFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);

			Date d = new Date();
			screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

			try {
				FileUtils.copyFile(scrFile, new File(reportFilePath + screenshotName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		}
	

	}
