package com.ui.listeners;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;




public class ExtentListeners implements ITestListener {

	static Date d = new Date();
	static String fileName = "extent.html";

	private static ExtentReports extent = ExtentManager.createInstance(fileName);
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();
	
	public static boolean overAllFailure; 

	public void onTestStart(ITestResult result) {
		String testName = "TestCase : ";
		if (result.getParameters().length > 0) {
			testName = testName + result.getParameters()[0];
		} else {
			testName = testName + result.getMethod().getMethodName();
		}
		
		
		ExtentTest test = extent.createTest(testName);
        testReport.set(test);
        overAllFailure = false;
	}

	public void onTestSuccess(ITestResult result) {
		String methodName=result.getMethod().getMethodName();
		Markup m;
		if (overAllFailure) {
			String logText="<b>"+"TEST CASE:- "+ methodName.toUpperCase()+ " FAILED"+"</b>";		
			m=MarkupHelper.createLabel(logText, ExtentColor.RED);	
			testReport.get().fail(m);
		} else {
			String logText="<b>"+"TEST CASE:- "+ methodName.toUpperCase()+ " PASSED"+"</b>";		
			m=MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			testReport.get().pass(m);
		}
		
	}

	public void onTestFailure(ITestResult result) {

		String exceptionMessage=Arrays.toString(result.getThrowable().getStackTrace());
		testReport.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
				+ "</font>" + "</b >" + "</summary>" +exceptionMessage.replaceAll(",", "<br>")+"</details>"+" \n");
		
		try {

			ExtentManager.captureScreenshot();
			testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotName)
							.build());
		} catch (IOException e) {
			testReport.get().info("!Unable to Capture Screen Shot!");
			
		}
		
		String failureLogg="TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		testReport.get().log(Status.FAIL, m);
		overAllFailure = true;

	}

	public void onTestSkipped(ITestResult result) {
		String methodName=result.getMethod().getMethodName();
		String logText="<b>"+"Test Case:- "+ methodName+ " Skipped"+"</b>";		
		Markup m=MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		testReport.get().skip(m);

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {


	}

	public void onStart(ITestContext context) {

		

	}

	public void onFinish(ITestContext context) {

		if (extent != null) {
			extent.flush();
		}

	}

}
