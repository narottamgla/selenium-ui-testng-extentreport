package com.ui.testcases;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ui.base.BaseTest;
import com.ui.pages.HomePage;

public class HomeTest extends BaseTest {

	String url;
	HomePage homePage;

	@BeforeMethod
	public void setUp() {
		openBrowser();
		url = config.getProperty("HOME_PAGE_URL") + "/";
	}

	@Test(enabled = true, groups = { "smoke", "regression" })
	public void homePageNavigationTest() {
		homePage = new HomePage();
		homePage.open(url);
		homePage.waitForPageToLoad(homePage.getPageLoadCondition());
		homePage.verifyNavigationToHomePage();
	}

	@Test(enabled = true, groups = { "regression" })
	public void verifyTopLeftNavigations() {
		homePage = new HomePage();
		homePage.open(url);
		homePage.waitForPageToLoad(homePage.getPageLoadCondition());
		homePage.verifyHomeLeftNavMenus();
	}

	@Test(enabled = true, groups = { "regression" })
	public void verifyTopRightNavigations() {
		homePage = new HomePage();
		homePage.open(url);
		homePage.waitForPageToLoad(homePage.getPageLoadCondition());
		homePage.verifyHomeRightNavMenus();
	}

}
