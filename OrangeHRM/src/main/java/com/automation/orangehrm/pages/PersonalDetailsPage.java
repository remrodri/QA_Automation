package com.automation.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PersonalDetailsPage {
    private static final By PERSONAL_DETAILS = By.xpath("//h6[normalize-space()='Personal Details']");
    private static final By FIRST_NAME = By.name("firstName");
    private static final By MIDDLE_NAME = By.name("middleName");
    private static final By LAST_NAME = By.name("lastName");
    private static final By EMPLOYEE_ID = By.xpath(
            "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By EMPLOYEE_LIST =
            By.xpath("//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Employee List']");
    private static final By USER_MENU = By.cssSelector(".oxd-userdropdown-tab");
    private static final By LOGOUT_LINK = By.xpath("//a[normalize-space()='Logout']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PersonalDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public PersonalDetailsPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/pim/viewPersonalDetails/empNumber/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PERSONAL_DETAILS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME));
        return this;
    }

    public boolean hasEmployeeData(String firstName, String middleName, String lastName, String employeeId) {
        return wait.until(currentDriver ->
                firstName.equals(currentDriver.findElement(FIRST_NAME).getAttribute("value"))
                        && middleName.equals(currentDriver.findElement(MIDDLE_NAME).getAttribute("value"))
                        && lastName.equals(currentDriver.findElement(LAST_NAME).getAttribute("value"))
                        && employeeId.equals(currentDriver.findElement(EMPLOYEE_ID).getAttribute("value")));
    }

    public PimEmployeeListPage openEmployeeList() {
        wait.until(ExpectedConditions.elementToBeClickable(EMPLOYEE_LIST)).click();
        return new PimEmployeeListPage(driver).waitUntilLoaded();
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(USER_MENU)).click();
        wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_LINK)).click();
        return new LoginPage(driver).waitUntilLoaded();
    }
}
