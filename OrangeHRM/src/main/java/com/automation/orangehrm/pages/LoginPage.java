package com.automation.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class LoginPage {
    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By LOGIN_HEADING = By.xpath("//h5[normalize-space()='Login']");
    private static final By INVALID_CREDENTIALS = By.cssSelector(".oxd-alert-content-text");
    private static final By REQUIRED_MESSAGES = By.cssSelector(".oxd-input-field-error-message");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public LoginPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_HEADING));
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        return this;
    }

    public boolean isDisplayed() {
        return driver.findElement(LOGIN_HEADING).isDisplayed()
                && driver.getCurrentUrl().contains("/auth/login");
    }

    public DashboardPage loginAs(String username, String password) {
        submit(username, password);
        return new DashboardPage(driver).waitUntilLoaded();
    }

    public LoginPage submit(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME)).clear();
        driver.findElement(PASSWORD).clear();
        if (username != null && !username.isEmpty()) driver.findElement(USERNAME).sendKeys(username);
        if (password != null && !password.isEmpty()) driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
        return this;
    }

    public String waitForInvalidCredentials() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(INVALID_CREDENTIALS)).getText();
    }

    public int waitForRequiredMessages(final int expectedCount) {
        wait.until(currentDriver -> currentDriver.findElements(REQUIRED_MESSAGES).size() == expectedCount);
        List<org.openqa.selenium.WebElement> messages = driver.findElements(REQUIRED_MESSAGES);
        return messages.size();
    }
}
