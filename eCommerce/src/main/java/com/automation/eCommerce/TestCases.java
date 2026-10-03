package com.automation.eCommerce;

import static com.automation.core.UserConstants.*;

import java.lang.reflect.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

import com.automation.core.TestEnvironment;
import com.automation.core.TestSuite;
import com.automation.core.UserDetails;


public class TestCases extends TestSuite
{
	TestUser tUser;

	public TestCases() throws Exception
	{
		super(PROJECT_ROOT_FOLDER_NAME, 
				EXCEL_TESTS_TO_RUN_FILE_NAME,
				TestCases.class.getSimpleName());
	}

    @Parameters({ "server", "environment", "browserName", "browserVersion" })
    @BeforeClass
    public void setUpClass(String server, String environment, String browserName, String browserVersion)
    		 throws Exception
    {
    	super.setUpClass(server, environment, browserName, browserVersion);
    	
    	UserDetails details = excel.readGeneralUserSheet("General User");
    	    			
    	tUser = new TestUser(details.getUserName(),
                details.getUserPassword());
    	
    }

    @BeforeMethod
    public void setUp(Method method) throws Exception
    {
    	super.setUp(method, new TestEnvironment(envName).getUrl());
    	tUser.initializeUser(driver, commonCore);
    	
    }

    @Test(description = "#<JIRA ID> <Login Test>")
    public void TestButton() throws Exception
    {
    	driver.get("https://demoqa.com/buttons");
    	tUser.clickSingle();
    	    	
    }
}
