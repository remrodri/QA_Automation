package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.LoginPage;
import com.automation.orangehrm.pages.PersonalDetailsPage;

public class PimDataDrivenEmployeeTest extends BaseOrangeHrmTest {
    @DataProvider(name = "validEmployees")
    public Object[][] validEmployees() {
        return new Object[][] {
                {"EMP-D01", "Ana", "", "Prueba"},
                {"EMP-D04", "Bruno", "Jose", "Control"},
                {"EMP-D05", "Carla", "", "Datos"}
        };
    }

    @Test(
            dataProvider = "validEmployees",
            groups = {"pim", "pim-data-driven"},
            description = "TC-PIM-004 Registro de empleados con datos validos multiples")
    public void TestPim004CreateEmployeesFromValidDatasets(
            String datasetId, String firstNameBase, String middleName, String lastName) {
        String uniqueSuffix = uniqueSuffix();
        String firstName = firstNameBase + uniqueSuffix;
        String employeeId = "D" + datasetId.substring(datasetId.length() - 2) + uniqueSuffix;

        PersonalDetailsPage details = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .openAddEmployee()
                .createEmployee(firstName, middleName, lastName, employeeId);

        Assert.assertTrue(
                details.hasEmployeeData(firstName, middleName, lastName, employeeId),
                "La ficha del dataset " + datasetId + " no conservó todos los datos esperados.");
    }

    private String uniqueSuffix() {
        String encodedTime = Long.toString(System.nanoTime(), 36).toUpperCase();
        return encodedTime.substring(Math.max(0, encodedTime.length() - 5));
    }
}
