package com.tamizh.utils;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.net.UnknownHostException;

public class ExtentReportListeners extends ExtentManager implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+"Pass Test case is: " ,ExtentColor.INDIGO));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL,
                MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
        test.log(Status.FAIL,
                MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Skipped Test case is: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            extent.setSystemInfo("Hostname",SystemInfo.getHostName());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        extent.setSystemInfo("os",SystemInfo.getOs());
    }
}
