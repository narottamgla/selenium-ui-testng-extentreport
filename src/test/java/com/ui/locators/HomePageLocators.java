package com.ui.locators;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageLocators {

	@FindBy(css = "img.logo")
	public WebElement homePageLogo;
	
	@FindBy(css = "ul li:nth-child(1) .user-segment_link")
	public WebElement personalHomeLeftMenu;
	
	@FindBy(css = "ul li:nth-child(2) .user-segment_link")
	public WebElement businessHomeLeftMenu;
	
	
	@FindBy(css = "ul li:nth-child(3) .user-segment_link")
	public WebElement wealthHomeLeftMenu;
	
	@FindBy(css = "ul.secondary-nav__misc-links a")
	public List<WebElement> rightTopNavOptions;
}
