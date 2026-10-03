package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.LoginPage;
import com.automation.orangehrm.pages.PersonalDetailsPage;
import com.automation.orangehrm.pages.PimEmployeeListPage;

public class EmployeeLifecycleE2ETest extends BaseOrangeHrmTest {
    @Test(
            groups = {"e2e"},
            description = "TC-E2E-001 Login alta busqueda verificacion y Logout")
    public void TestE2E001EmployeeLifecycle() {
        String suffix = uniqueSuffix();
        String firstName = "E2E" + suffix;
        String middleName = "QA";
        String lastName = "Lifecycle";
        String employeeId = "E" + suffix;

        PersonalDetailsPage createdDetails = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .openAddEmployee()
                .createEmployee(firstName, middleName, lastName, employeeId);

        Assert.assertTrue(
                createdDetails.hasEmployeeData(firstName, middleName, lastName, employeeId),
                "El alta E2E no conservó todos los datos.");

        PimEmployeeListPage employeeList = createdDetails
                .openEmployeeList()
                .searchByEmployeeId(employeeId);

        Assert.assertTrue(
                employeeList.hasEmployeeResult(employeeId, firstName, lastName),
                "La búsqueda E2E no devolvió el empleado creado.");

        PersonalDetailsPage reopenedDetails = employeeList.openEmployeeDetails(employeeId);
        Assert.assertTrue(
                reopenedDetails.hasEmployeeData(firstName, middleName, lastName, employeeId),
                "La ficha reabierta no conservó los datos del empleado E2E.");

        Assert.assertTrue(
                reopenedDetails.logout().isDisplayed(),
                "Logout no devolvió al formulario de Login.");
    }

    private String uniqueSuffix() {
        String encodedTime = Long.toString(System.nanoTime(), 36).toUpperCase();
        return encodedTime.substring(Math.max(0, encodedTime.length() - 6));
    }
}
