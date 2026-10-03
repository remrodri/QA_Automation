package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.DashboardPage;
import com.automation.orangehrm.pages.LoginPage;

public class LoginSmokeTest extends BaseOrangeHrmTest {
    @Test(
            groups = {"smoke", "login"},
            description = "TC-AUTH-001 Inicio de sesión con credenciales válidas")
    public void TestAuth001ValidLogin() {
        DashboardPage dashboard = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword);
        Assert.assertTrue(dashboard.isDisplayed(), "El Dashboard no quedó visible.");
    }
}
