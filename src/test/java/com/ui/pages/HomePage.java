package com.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ui.base.Page;
import com.ui.locators.HomePageLocators;
import com.ui.utilities.DriverManager;

public class HomePage extends Page {

	HomePageLocators selector ;
	WebDriver driver;
	
	public HomePage() {
		selector  = new HomePageLocators();
		this.driver = DriverManager.getDriver();
		AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver, 30);
		PageFactory.initElements(factory, this.selector);
	}
	
	public ExpectedCondition<WebElement> getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(selector.homePageLogo);
	}
	
	public void open(String url) {
		DriverManager.getDriver().navigate().to(url);
	}

	public void verifyNavigationToHomePage() {
		waitForElementToLoad(selector.homePageLogo, "Home Page Logo");
	}

	public void verifyHomeLeftNavMenus() {
		textMustContain(selector.personalHomeLeftMenu, "PERSONAL","Personal Menu");
		textMustContain(selector.businessHomeLeftMenu, "BUSINESS","Business Menu");
		textMustContain(selector.wealthHomeLeftMenu, "WEALTH", "Wealth Menu");
	}

	public void verifyHomeRightNavMenus() {
        textMustContain(selector.rightTopNavOptions.get(0), "About Us", "About Us Menu");	
        textMustContain(selector.rightTopNavOptions.get(1), "Careers", "Careers Menu");	
        textMustContain(selector.rightTopNavOptions.get(2), "Contact Us", "Contact US Menu");	
        textMustContain(selector.rightTopNavOptions.get(3), "Locations", "Locations Menu");	

	}

}
