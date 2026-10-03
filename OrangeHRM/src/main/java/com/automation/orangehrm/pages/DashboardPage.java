package com.automation.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage {
    private static final By DASHBOARD_HEADING =
            By.xpath("//h6[normalize-space()='Dashboard']");
    private static final By USER_MENU = By.cssSelector(".oxd-userdropdown-tab");
    private static final By LOGOUT_LINK = By.xpath("//a[normalize-space()='Logout']");
    private static final By PIM_MENU = By.cssSelector("a[href*='/pim/viewPimModule']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public DashboardPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/dashboard/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(DASHBOARD_HEADING));
        return this;
    }

    public boolean isDisplayed() {
        return driver.findElement(DASHBOARD_HEADING).isDisplayed();
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(USER_MENU)).click();
        wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_LINK)).click();
        return new LoginPage(driver).waitUntilLoaded();
    }

    public PimEmployeeListPage openPim() {
        wait.until(ExpectedConditions.elementToBeClickable(PIM_MENU)).click();
        return new PimEmployeeListPage(driver).waitUntilLoaded();
    }
}
