package com.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;

import base.Basepage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllureListener implements ITestListener {

    private static final String RESULTS_DIR = "reports/allure-results";

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        if (!(testInstance instanceof Basepage)) return;

        Page page = ((Basepage) testInstance).getPage();
        if (page == null) return;

        System.out.println("Test Failed: " + result.getName() + ". Taking screenshot...");
        try {
            byte[] screenshot = page.screenshot(
                new ScreenshotOptions().setTimeout(5000)
            );
            Allure.addAttachment(
                "Failure Screenshot - " + result.getName(),
                "image/png",
                new ByteArrayInputStream(screenshot),
                "png"
            );
        } catch (Exception e) {
            System.out.println("⚠️  Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir = "reports/allure-report-" + timestamp;
        
        System.out.println("📊 Generating Allure report...");
        try {
            // Ensure report output folder exists
            new File(reportDir).mkdirs();

            // Generate the HTML report from raw results
            ProcessBuilder generate = new ProcessBuilder(
                "cmd.exe", "/c",
                "allure generate " + RESULTS_DIR + " --output " + reportDir + " --clean"
            );
            generate.redirectErrorStream(true);
            generate.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            Process genProcess = generate.start();
            genProcess.waitFor(); // wait for generation to complete

            System.out.println("✅ Allure report saved to: " + reportDir);

            if (Boolean.getBoolean("allure.open.report")) {
                ProcessBuilder open = new ProcessBuilder(
                    "cmd.exe", "/c",
                    "allure open " + reportDir
                );
                open.start();
            }

        } catch (Exception e) {
            System.out.println("⚠️  Could not generate/open Allure report: " + e.getMessage());
        }
    }
}
