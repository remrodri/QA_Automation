package com.automation.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.orangehrm.pages.LoginPage;

public class LoginNegativeTest extends BaseOrangeHrmTest {
    @Test(groups = "login", description = "TC-AUTH-002 Inicio de sesión con usuario inválido")
    public void TestAuth002InvalidUsername() {
        Assert.assertEquals(loginPage().submit("usuario_invalido_qa", validPassword).waitForInvalidCredentials(), "Invalid credentials");
    }

    @Test(groups = "login", description = "TC-AUTH-003 Inicio de sesión con contraseña inválida")
    public void TestAuth003InvalidPassword() {
        Assert.assertEquals(loginPage().submit(validUsername, "clave_invalida_qa").waitForInvalidCredentials(), "Invalid credentials");
    }

    @Test(groups = "login", description = "TC-AUTH-004 Inicio de sesión con usuario vacío")
    public void TestAuth004EmptyUsername() {
        Assert.assertEquals(loginPage().submit("", validPassword).waitForRequiredMessages(1), 1);
    }

    @Test(groups = "login", description = "TC-AUTH-005 Inicio de sesión con contraseña vacía")
    public void TestAuth005EmptyPassword() {
        Assert.assertEquals(loginPage().submit(validUsername, "").waitForRequiredMessages(1), 1);
    }

    @Test(groups = "login", description = "TC-AUTH-006 Inicio de sesión con ambos campos vacíos")
    public void TestAuth006BothEmpty() {
        Assert.assertEquals(loginPage().submit("", "").waitForRequiredMessages(2), 2);
    }

    private LoginPage loginPage() {
        return new LoginPage(driver).waitUntilLoaded();
    }
}
