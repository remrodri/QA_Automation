package com.automation.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PimEmployeeListPage {
    private static final By EMPLOYEE_INFORMATION =
            By.xpath("//h5[normalize-space()='Employee Information']");
    private static final By ADD_EMPLOYEE =
            By.xpath("//nav[contains(@class,'oxd-topbar-body-nav')]//a[normalize-space()='Add Employee']");
    private static final By EMPLOYEE_ID = By.xpath(
            "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By SEARCH = By.xpath("//button[@type='submit' and normalize-space()='Search']");
    private static final By RESULT_ROWS = By.cssSelector(".oxd-table-card");
    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");
    private static final By NO_RECORDS_FOUND = By.xpath("//*[normalize-space()='No Records Found']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PimEmployeeListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public PimEmployeeListPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/pim/viewEmployeeList"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMPLOYEE_INFORMATION));
        return this;
    }

    public AddEmployeePage openAddEmployee() {
        wait.until(ExpectedConditions.elementToBeClickable(ADD_EMPLOYEE)).click();
        return new AddEmployeePage(driver).waitUntilLoaded();
    }

    public PimEmployeeListPage searchByEmployeeId(String employeeId) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(EMPLOYEE_ID));
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(employeeId);
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
        wait.until(currentDriver -> currentDriver.findElements(RESULT_ROWS).stream()
                .anyMatch(row -> row.getText().contains(employeeId)));
        return this;
    }

    public boolean hasEmployeeResult(String employeeId, String firstName, String lastName) {
        return driver.findElements(RESULT_ROWS).stream().anyMatch(row -> {
            String text = row.getText();
            return text.contains(employeeId) && text.contains(firstName) && text.contains(lastName);
        });
    }

    public PimEmployeeListPage searchByEmployeeIdExpectingNoResults(String employeeId) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(EMPLOYEE_ID));
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(employeeId);
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
        wait.until(ExpectedConditions.visibilityOfElementLocated(NO_RECORDS_FOUND));
        return this;
    }

    public boolean hasNoRecordsFound() {
        return !driver.findElements(NO_RECORDS_FOUND).isEmpty()
                && driver.findElements(RESULT_ROWS).isEmpty();
    }

    public PersonalDetailsPage openEmployeeDetails(String employeeId) {
        By editEmployee = By.xpath(
                "//div[contains(@class,'oxd-table-card')]"
                        + "[.//div[@role='cell'][normalize-space()='" + employeeId + "']]"
                        + "//button[.//i[contains(@class,'bi-pencil-fill')]]");
        wait.until(ExpectedConditions.elementToBeClickable(editEmployee)).click();
        return new PersonalDetailsPage(driver).waitUntilLoaded();
    }
}
