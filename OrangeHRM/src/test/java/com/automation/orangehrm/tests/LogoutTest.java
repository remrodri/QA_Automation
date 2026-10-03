package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.DashboardPage;
import com.automation.orangehrm.pages.LoginPage;

public class LogoutTest extends BaseOrangeHrmTest {
    @Test(groups = "login", description = "TC-AUTH-007 Cierre de sesión")
    public void TestAuth007Logout() {
        DashboardPage dashboard = new LoginPage(driver)
                .waitUntilLoaded()
                .loginAs(validUsername, validPassword);

        LoginPage loginPage = dashboard.logout();
        Assert.assertTrue(loginPage.isDisplayed(), "No se regresó a Login después de Logout.");
    }
}
