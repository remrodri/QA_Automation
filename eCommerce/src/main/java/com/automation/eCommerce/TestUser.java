package com.automation.eCommerce;


import static com.automation.eCommerce.Constants.*;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.automation.core.CommonCore;

import com.automation.core.User;

public class TestUser extends User
{
	Constants wElements;
    public TestUser(String username, String password)
	{
		super(username, password);
	}
    
    public void initializeUser(WebDriver driver,
            CommonCore commonCore) throws Exception
	{
		setEnvironment(driver, commonCore);
		wElements = new Constants(driver);
	}
    
    public void clickSingle() throws InterruptedException 
    {

    	click(bClick,"Click Single button");
    	Assert.assertEquals(wElements.txtClickMessage.getText(), "You have done a dynamic click");
    }
}
