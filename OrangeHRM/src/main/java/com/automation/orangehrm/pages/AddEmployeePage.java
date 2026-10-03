package com.automation.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddEmployeePage {
    private static final By ADD_EMPLOYEE_HEADING = By.xpath("//h6[normalize-space()='Add Employee']");
    private static final By FIRST_NAME = By.name("firstName");
    private static final By MIDDLE_NAME = By.name("middleName");
    private static final By LAST_NAME = By.name("lastName");
    private static final By EMPLOYEE_ID = By.xpath(
            "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By SAVE = By.cssSelector("button[type='submit']");
    private static final By CANCEL = By.xpath("//button[@type='button' and normalize-space()='Cancel']");
    private static final By FIRST_NAME_REQUIRED = By.xpath(
            "//input[@name='firstName']/ancestor::div[contains(@class,'oxd-input-group')]//span[normalize-space()='Required']");
    private static final By LAST_NAME_REQUIRED = By.xpath(
            "//input[@name='lastName']/ancestor::div[contains(@class,'oxd-input-group')]//span[normalize-space()='Required']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AddEmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public AddEmployeePage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/pim/addEmployee"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_EMPLOYEE_HEADING));
        return this;
    }

    public PersonalDetailsPage createEmployee(
            String firstName, String middleName, String lastName, String employeeId) {
        driver.findElement(FIRST_NAME).sendKeys(firstName);
        if (middleName != null && !middleName.isEmpty()) driver.findElement(MIDDLE_NAME).sendKeys(middleName);
        driver.findElement(LAST_NAME).sendKeys(lastName);
        replaceValue(driver.findElement(EMPLOYEE_ID), employeeId);
        wait.until(ExpectedConditions.elementToBeClickable(SAVE)).click();
        return new PersonalDetailsPage(driver).waitUntilLoaded();
    }

    public AddEmployeePage submitForValidation(
            String firstName, String middleName, String lastName, String employeeId) {
        if (firstName != null && !firstName.isEmpty()) driver.findElement(FIRST_NAME).sendKeys(firstName);
        if (middleName != null && !middleName.isEmpty()) driver.findElement(MIDDLE_NAME).sendKeys(middleName);
        if (lastName != null && !lastName.isEmpty()) driver.findElement(LAST_NAME).sendKeys(lastName);
        replaceValue(driver.findElement(EMPLOYEE_ID), employeeId);
        wait.until(ExpectedConditions.elementToBeClickable(SAVE)).click();
        return this;
    }

    public boolean isFirstNameRequired() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_REQUIRED));
        return driver.getCurrentUrl().contains("/pim/addEmployee");
    }

    public boolean isLastNameRequired() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LAST_NAME_REQUIRED));
        return driver.getCurrentUrl().contains("/pim/addEmployee");
    }

    public PimEmployeeListPage fillPartialFormAndCancel(
            String firstName, String lastName, String employeeId) {
        driver.findElement(FIRST_NAME).sendKeys(firstName);
        driver.findElement(LAST_NAME).sendKeys(lastName);
        replaceValue(driver.findElement(EMPLOYEE_ID), employeeId);
        wait.until(ExpectedConditions.elementToBeClickable(CANCEL)).click();
        return new PimEmployeeListPage(driver).waitUntilLoaded();
    }

    private void replaceValue(WebElement element, String value) {
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(value);
    }
}
