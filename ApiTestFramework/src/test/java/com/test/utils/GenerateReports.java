package com.test.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class GenerateReports {
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest logger;
	private static GenerateReports ob;
	private GenerateReports() {
		
	}
	public static GenerateReports getInstance() {
		if(ob==null) {
			ob=new GenerateReports();
			System.out.println("GenerateReports object created");
			
		}
		else {
			System.out.println("same GenerateReports returned");
		}
		return ob;
	}

	public void startExtentReport() {

		htmlReporter = new ExtentHtmlReporter(sourcepath.GENERATE_REPORT_PATH);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", "tekarch");
		extent.setSystemInfo("Environment", "Automation Testing");
		extent.setSystemInfo("User Name", "manju");

		htmlReporter.config().setDocumentTitle("Test Execution Report");
		htmlReporter.config().setReportName("firebase regression tests");
		htmlReporter.config().setTheme(Theme.STANDARD);
		System.out.println("report started");
		
		
	}

	public void startSingleTestReport(String testName) {
		logger = extent.createTest(testName);
	
	}

	public void logTestInfo(String message) {

		logger.log(Status.INFO, message);
	}

	public void logTestpassed(String testcaseName) {
		logger.log(Status.PASS, MarkupHelper.createLabel(testcaseName + "is passTest", ExtentColor.GREEN));
	}

	public void logTestFailed(String testcaseName) {
		logger.log(Status.FAIL, MarkupHelper.createLabel(testcaseName + "is not passTest", ExtentColor.RED));
	}
	public void logTestFailedWithException(Exception e) {
	logger.log(Status.ERROR,e);
	}
	public void logTestSkipped(String testcaseName) {
		logger.log(Status.SKIP,
				MarkupHelper.createLabel(testcaseName + " skipped the Test", ExtentColor.YELLOW));
	}

	public void endReport() {
		extent.flush();
	}
	

}


