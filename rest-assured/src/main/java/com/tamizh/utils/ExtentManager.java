package com.tamizh.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    public static ExtentSparkReporter sparkReporter;
    public static ExtentReports extent;
    public static ExtentTest test;

    public static void setExtent() {
        extent = new ExtentReports();
        sparkReporter = new ExtentSparkReporter("TestResult.html");
        extent.attachReporter(sparkReporter);
        test = extent.createTest("APITestResults");
    }

    public static void endExtent() {
        extent.flush();
    }


}
