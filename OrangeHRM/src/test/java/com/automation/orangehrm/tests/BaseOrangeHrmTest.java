package com.automation.orangehrm.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automation.orangehrm.OrangeHrmUrls;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class BaseOrangeHrmTest {
    protected WebDriver driver;
    protected String validUsername;
    protected String validPassword;

    @BeforeMethod(alwaysRun = true)
    public void openBrowser() {
        validUsername = configuration("orangehrm.username", "ORANGEHRM_USERNAME");
        validPassword = configuration("orangehrm.password", "ORANGEHRM_PASSWORD");
        if (validUsername == null || validPassword == null) {
            throw new SkipException("Define ORANGEHRM_USERNAME y ORANGEHRM_PASSWORD para ejecutar Login.");
        }
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("orangehrm.headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1000");
        driver = new ChromeDriver(options);
        driver.get(System.getProperty("orangehrm.url", OrangeHrmUrls.DEMO_LOGIN_URL));
    }

    @AfterMethod(alwaysRun = true)
    public void finishTest(ITestResult result) throws IOException {
        try {
            if (!result.isSuccess() && driver instanceof TakesScreenshot) saveFailureScreenshot(result.getMethod().getMethodName());
        } finally {
            if (driver != null) driver.quit();
        }
    }

    private void saveFailureScreenshot(String methodName) throws IOException {
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path evidenceDirectory = Paths.get("target", "evidence");
        Files.createDirectories(evidenceDirectory);
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        Path destination = evidenceDirectory.resolve(methodName + "-" + timestamp + ".png");
        Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Evidencia de fallo: " + destination.toAbsolutePath());
    }

    private static String configuration(String propertyName, String environmentName) {
        String value = System.getProperty(propertyName);
        if (value == null || value.trim().isEmpty()) value = System.getenv(environmentName);
        return value == null || value.trim().isEmpty() ? null : value;
    }
}
