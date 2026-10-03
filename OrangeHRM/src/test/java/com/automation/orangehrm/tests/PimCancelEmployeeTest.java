package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.LoginPage;
import com.automation.orangehrm.pages.PimEmployeeListPage;

public class PimCancelEmployeeTest extends BaseOrangeHrmTest {
    @Test(
            groups = {"pim", "pim-cancel"},
            description = "TC-PIM-007 Cancelacion del registro de empleado")
    public void TestPim007CancelEmployeeCreation() {
        String suffix = uniqueSuffix();
        String employeeId = "C" + suffix;

        PimEmployeeListPage employeeList = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .openAddEmployee()
                .fillPartialFormAndCancel("Cancel" + suffix, "Employee", employeeId)
                .searchByEmployeeIdExpectingNoResults(employeeId);

        Assert.assertTrue(
                employeeList.hasNoRecordsFound(),
                "El Employee ID " + employeeId + " no debía existir después de cancelar el alta.");
    }

    private String uniqueSuffix() {
        String encodedTime = Long.toString(System.nanoTime(), 36).toUpperCase();
        return encodedTime.substring(Math.max(0, encodedTime.length() - 7));
    }
}
