package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.AddEmployeePage;
import com.automation.orangehrm.pages.LoginPage;

public class PimRequiredFieldsTest extends BaseOrangeHrmTest {
    @Test(
            groups = {"pim", "pim-validation"},
            description = "TC-PIM-002 Registro de empleado sin nombre")
    public void TestPim002FirstNameRequired() {
        AddEmployeePage page = openAddEmployee()
                .submitForValidation("", "Auto", "SinNombre", uniqueEmployeeId());
        Assert.assertTrue(page.isFirstNameRequired(),
                "First Name no mostró Required o el formulario permitió continuar.");
    }

    @Test(
            groups = {"pim", "pim-validation"},
            description = "TC-PIM-003 Registro de empleado sin apellido")
    public void TestPim003LastNameRequired() {
        AddEmployeePage page = openAddEmployee()
                .submitForValidation("SinApellido", "Auto", "", uniqueEmployeeId());
        Assert.assertTrue(page.isLastNameRequired(),
                "Last Name no mostró Required o el formulario permitió continuar.");
    }

    private AddEmployeePage openAddEmployee() {
        return new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword)
                .openPim()
                .openAddEmployee();
    }

    private String uniqueEmployeeId() {
        String unique = Long.toString(System.nanoTime(), 36).toUpperCase();
        String suffix = unique.substring(Math.max(0, unique.length() - 7));
        return "V" + suffix;
    }
}
