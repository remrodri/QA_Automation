package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.LoginPage;
import com.automation.orangehrm.pages.PersonalDetailsPage;
import com.automation.orangehrm.pages.PimEmployeeListPage;

public class PimSearchEmployeeTest extends BaseOrangeHrmTest {
    @Test(
            groups = {"pim", "pim-search"},
            description = "TC-PIM-005 Busqueda de empleado registrado por Employee ID")
    public void TestPim005SearchCreatedEmployeeById() {
        String suffix = uniqueSuffix();
        String firstName = "Search" + suffix;
        String middleName = "QA";
        String lastName = "Employee";
        String employeeId = "S" + suffix;

        PersonalDetailsPage details = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .openAddEmployee()
                .createEmployee(firstName, middleName, lastName, employeeId);

        Assert.assertTrue(
                details.hasEmployeeData(firstName, middleName, lastName, employeeId),
                "El alta usada como precondición no persistió correctamente.");

        PimEmployeeListPage employeeList = details
                .openEmployeeList()
                .searchByEmployeeId(employeeId);

        Assert.assertTrue(
                employeeList.hasEmployeeResult(employeeId, firstName, lastName),
                "La búsqueda no devolvió una fila coincidente con el empleado recién creado.");
    }

    private String uniqueSuffix() {
        String encodedTime = Long.toString(System.nanoTime(), 36).toUpperCase();
        return encodedTime.substring(Math.max(0, encodedTime.length() - 6));
    }
}
