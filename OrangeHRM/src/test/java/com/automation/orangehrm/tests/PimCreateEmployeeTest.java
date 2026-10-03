package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.PersonalDetailsPage;
import com.automation.orangehrm.pages.LoginPage;

public class PimCreateEmployeeTest extends BaseOrangeHrmTest {
    @Test(groups = "pim", description = "TC-PIM-001 Registro de empleado con datos válidos")
    public void TestPim001CreateValidEmployee() {
        String unique = Long.toString(System.currentTimeMillis(), 36).toUpperCase();
        String suffix = unique.substring(Math.max(0, unique.length() - 6));
        String firstName = "QA" + suffix;
        String middleName = "Auto";
        String lastName = "Codex";
        String employeeId = "Q" + suffix;

        PersonalDetailsPage details = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .openAddEmployee()
                .createEmployee(firstName, middleName, lastName, employeeId);

        Assert.assertTrue(
                details.hasEmployeeData(firstName, middleName, lastName, employeeId),
                "La ficha no conservó todos los datos del empleado creado.");
    }
}
