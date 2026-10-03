package com.automation.eCommerce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Constants {
	// Prevents to instantiate another Constants object
	protected WebDriver driver;

	public Constants(WebDriver driver) {
		super();
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@FindBy(xpath = "//button[normalize-space()='Click Me']")
	   public static  WebElement bClick;
	
	@FindBy(xpath = "//button[normalize-space()='Click Me']")
	   public static  WebElement bDoubleClick;
	
	@FindBy(xpath = "//button[normalize-space()='Right Click Me']")
	   public static  WebElement bRightClick;
	
	@FindBy(id = "dynamicClickMessage")
	   public static  WebElement txtClickMessage;
	
	@FindBy(id = "doubleClickMessage")
	   public static  WebElement txtDoubleClickMessage;
	
	@FindBy(id = "rightClickMessage")
	   public static  WebElement txtRightClickMessage;
	
	
	public static By chkHome = By.cssSelector("label > span.rct-checkbox > svg");
		
}
