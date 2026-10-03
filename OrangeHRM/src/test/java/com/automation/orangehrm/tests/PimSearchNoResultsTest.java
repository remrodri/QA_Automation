package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.LoginPage;
import com.automation.orangehrm.pages.PimEmployeeListPage;

public class PimSearchNoResultsTest extends BaseOrangeHrmTest {
    @Test(
            groups = {"pim", "pim-search-negative"},
            description = "TC-PIM-006 Busqueda de empleado inexistente")
    public void TestPim006SearchUnknownEmployee() {
        String unknownEmployeeId = "Z" + uniqueSuffix();

        PimEmployeeListPage employeeList = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .searchByEmployeeIdExpectingNoResults(unknownEmployeeId);

        Assert.assertTrue(
                employeeList.hasNoRecordsFound(),
                "La búsqueda de " + unknownEmployeeId
                        + " debía mostrar No Records Found sin filas de empleados.");
    }

    private String uniqueSuffix() {
        String encodedTime = Long.toString(System.nanoTime(), 36).toUpperCase();
        return encodedTime.substring(Math.max(0, encodedTime.length() - 7));
    }
}
