package com.ui.base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.ui.listeners.ExtentListeners;
import com.ui.listeners.ExtentManager;
import com.ui.utilities.DriverManager;

public class Page {

	protected WebDriver driver;
	public int timeoutElement;
	public int timeoutLoad;
	public static WebDriverWait waitOnElement;

	public Page() {
		this.driver = DriverManager.getDriver();
		this.timeoutLoad = DriverManager.getLoadTimeOut();
		this.timeoutElement = DriverManager.getElementTimeOut();
		waitOnElement = new WebDriverWait(driver, this.timeoutElement);
	}

	/**
	 * Attests that the page has loaded according to the PageLoadCondition
	 * 
	 * @param pageLoadCondition
	 *            as stipulated by the Page Object
	 */
	public void waitForPageToLoad(ExpectedCondition<WebElement> pageLoadCondition) {
		waitForPageToLoad(pageLoadCondition, "Page");
	}

	/**
	 * Attests that the page has loaded according to the PageLoadCondition
	 * 
	 * @param pageLoadCondition
	 *            as stipulated by the Page Object
	 * @param pageDescription
	 *            is the Description to appear in the report
	 */
	public void waitForPageToLoad(ExpectedCondition<WebElement> pageLoadCondition, String pageDescription) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutLoad);
		wait.until(pageLoadCondition);
		logInfo(pageDescription + " Loaded");
	}

	/**
	 * Attests that information has been loaded into a table
	 * 
	 * @param parentElement
	 *            is the name of the element in which to find rows
	 * @param childElementCSS
	 *            is the row element to find
	 * @param tableName
	 *            is the name to be published in the report
	 */
	public void waitForTableToLoad(WebElement parentElement, String childElementCSS, String tableName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeoutLoad);
			wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentElement,
					By.cssSelector(childElementCSS)));
			logInfo("Waiting for Table to Populate: " + tableName);
		} catch (Exception e) {
			logInfo("Table did not pooulate: " + tableName);
		}
	}

	/**
	 * Waits for An Element to be visible
	 * 
	 * @param element
	 *            is the element to test
	 * @param elementName
	 *            is the name to be reported
	 */
	public void waitForElementToLoad(WebElement element, String elementName) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutLoad);
		wait.until(ExpectedConditions.visibilityOf(element));
		logInfo("Element Loaded: " + elementName);
	}

	/**
	 * Will wait for an element to load but not report it
	 * 
	 * @param element
	 *            to be tested
	 */
	public void waitForElementToLoad(List<WebElement> element) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutLoad);
		wait.until(ExpectedConditions.visibilityOfAllElements(element));
	}

	public void waitForStaleness(ExpectedCondition<Boolean> waitCondition) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutLoad);
		wait.until(waitCondition);
		logInfo("Waited for Staleness");
	}

	/**
	 * Wait until element with a specific text value is displayed Fluent Wait ,
	 * total wait time 10s with check every 1s
	 * 
	 * @param element
	 *            to search for
	 * @param text
	 *            element text value to find
	 */
	public void waitForElementTextToDisplay(WebElement element, String text) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(10))
					.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.textToBePresentInElement(element, text));
			logInfo("Waited for element with specific text: \"" + text + "\"");
		} catch (Exception e) {
			logError("Failed to find element with specific text: \"" + text + "\"");
		}
	}

	public void click(WebElement element, String elementName) {
		try {
			element.click();
			logInfo("Clicking on : " + elementName);
		} catch (Exception e) {
			logError("Can't Click on : " + elementName);
		}
	}

	/**
	 * Used to click on elements only visible when hovering
	 * 
	 * @param element
	 *            to search for
	 * @param text
	 *            element name for reporting
	 */
	public void hoverClick(WebElement element, String elementName) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().perform();
			logInfo("Hover-Clicking on : " + elementName);
		} catch (Exception e) {
			logError("Can't Hover-Click on : " + elementName);
		}
	}

	/**
	 * Used to hover on elements
	 * 
	 * @param element
	 *            to search for
	 * @param text
	 *            element name for reporting
	 */
	public void hoverOver(WebElement element, String elementName) {
		try {
			logInfo("Hover-over : " + elementName);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).perform();
		} catch (Exception e) {
			logError("Can't Hover-over : " + elementName);
		}
	}

	public void checkCheckbox(WebElement element, String elementName) {
		if (!element.isSelected()) {
			try {
				element.click();
				logInfo("Clicking on Checkbox: " + elementName);
			} catch (Exception e) {
				logError("Can't Click on Checkbox: " + elementName);
			}
		}
	}

	public void uncheckCheckbox(WebElement element, String elementName) {
		if (element.isSelected()) {
			try {
				element.click();
				logInfo("Clicking on Checkbox: " + elementName);
			} catch (Exception e) {
				logError("Can't Click on Checkbox: " + elementName);
			}
		}
	}

	public void clear(WebElement element, String elementName) {
		element.clear();
		logInfo("Clearing content of " + elementName + ".");
	}

	/**
	 * This function clears input fields, that use Autocomplete or do not comply
	 * with clear()
	 * 
	 * @param element
	 * @param elementName
	 */
	public void clearManually(WebElement element, String elementName) {
		click(element, elementName);
		String text = element.getAttribute("value");
		for (int i = 0; i < text.length(); i++) {
			element.sendKeys(Keys.LEFT);
			element.sendKeys(Keys.DELETE);
		}
		logInfo("Manually Clearing content of " + elementName + ".");
	}

	/**
	 * This function clears an input field, if it uses chips
	 * 
	 * @param inputSelector
	 *            the input field
	 * @param removeTagSelector
	 *            selector to find the delete button of chips
	 */
	public void clearAutoComplete(WebElement inputSelector, String removeTagSelector) {
		click(inputSelector, "");
		List<WebElement> buttons = driver.findElements(By.cssSelector(removeTagSelector));
		for (int i = 0; i < buttons.size(); i++) {
			inputSelector.sendKeys("\u0008");
		}
	}

	public void enterText(WebElement element, String value, String elementName) {
		element.sendKeys(value);
		logInfo("Typing in : " + elementName + " entered the value as : " + value);
	}

	public static boolean elementVisible(WebElement element, String elementName) {
		try {
			waitOnElement.until(ExpectedConditions.visibilityOf(element));
			logInfo("Element is Visible : " + elementName);
			return true;
		} catch (Exception e) {
			logError("Element not Visible : " + elementName);
			return false;
		}
	}

	/**
	 * Return count of given element on page by a css selector
	 * 
	 * @param elementCSS
	 * @return
	 */
	public int elementCount(String elementCSS) {
		List<WebElement> allElements = waitOnElement
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(elementCSS)));
		return allElements.size();
	}

	/**
	 * Scrolls a particular element into the view port -> May not work for IE
	 * 
	 * @param element:
	 *            WebElement to scroll to
	 * @param elementName:
	 *            String to display in the report.
	 */
	public void scrollIntoView(WebElement element, String elementName) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
			actions.perform();
			logInfo("Scrolled to: " + elementName);
		} catch (Exception e) {
			logError("Unable to Scroll to: " + elementName + " : \n " + e.getMessage());
		}
	}

	public boolean elementExists(String locator, String elementDescription) {
		boolean elementExists = !DriverManager.getDriver().findElements(By.cssSelector(locator)).isEmpty();
		logInfo("Element Exists : " + elementDescription + " : " + elementExists);
		return elementExists;
	}

	public WebElement findByText(String elementText) {
		try {
			return driver.findElement(By.xpath("//*[text()='" + elementText + "']"));
		} catch (Exception e) {
			logError("Could not Find Element with Text :" + elementText);
			return null;
		}
	}

	public void findAndClick(String searchString) {
		click(findByText(searchString), "Click element with text " + searchString);
	}

	public int getTableRowCount(WebElement table) {
		return waitOnElement.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(table, By.tagName("tr")))
				.size();
	}

	public String getText(WebElement element) {
		try {
			String text = element.getText();
			return text;
		} catch (Exception e) {
			logError("Unable to get text of: " + element.getTagName());
			return "";
		}
	}

	public String getValue(WebElement element) {
		try {
			String text = element.getAttribute("value");
			return text;
		} catch (Exception e) {
			logError("Unable to get value of: " + element.getTagName());
			return "";
		}
	}

	/**
	 * Enter text and tab out of field
	 * 
	 * @param element
	 * @param value
	 * @param elementName
	 */
	public void tabOutTextField(WebElement element, String value, String elementName) {
		element.sendKeys(value + Keys.TAB);
		logInfo("Typing in : " + elementName + " entered the value as : " + value + " , tabbed out of field");
	}

	/**
	 * Compares an Element Text to a given String If it does not match it will log
	 * an error
	 * 
	 * @param element
	 *            : WebElement of the text to check
	 * @param textToMatch
	 *            : String of the text to match
	 * @param elementName
	 *            : Name of the element to appear on the report
	 */
	public void textMustMatch(WebElement element, String textToMatch, String elementName) {
		if (getText(element).equals(textToMatch)) {
			logInfo(elementName + " matched " + textToMatch);
		} else {
			logError(elementName + " did not match '" + textToMatch + "' found '" + getText(element) + "'");
		}
	}

	/**
	 * Compares an Element Value to a given String If it does not match it will log
	 * an error
	 * 
	 * @param element
	 *            : WebElement of the text to check
	 * @param textToMatch
	 *            : String of the text to match
	 * @param elementName
	 *            : Name of the element to appear on the report
	 */
	public void valueMustMatch(WebElement element, String textToMatch, String elementName) {
		if (getValue(element).equals(textToMatch)) {
			logInfo(elementName + " matched " + textToMatch);
		} else {
			logError(elementName + " did not match value '" + textToMatch + "' found '" + getValue(element) + "'");
		}
	}

	/**
	 * Checks Element Text contains a given String If it does not find it ,log an
	 * error
	 * 
	 * @param element
	 *            : WebElement of the text to check
	 * @param textToFind
	 *            : String of the text to contain
	 * @param elementName
	 *            : Name of the element to appear on the report
	 */
	public void textMustContain(WebElement element, String textToFind, String elementName) {
		try {
			if (getText(element).contains(textToFind)) {
				logInfo(elementName + " contains " + textToFind);
			} else {
				logError(elementName + " did not contain '" + textToFind + "' in '" + getText(element) + "'");
			}
		} catch (Exception e) {
			logError("Error finding element with text: '" + textToFind + "' in '" + elementName + "'");
		}
	}

	/**
	 * Checks Element Value contains a given String If it does not find it ,log an
	 * error
	 * 
	 * @param element
	 *            : WebElement of the text to check
	 * @param textToFind
	 *            : String of the text to contain
	 * @param elementName
	 *            : Name of the element to appear on the report
	 */
	public void valueMustContain(WebElement element, String textToFind, String elementName) {
		if (getValue(element).contains(textToFind)) {
			logInfo(elementName + " has value " + textToFind);
		} else {
			logError(elementName + " did not contain value of '" + textToFind + "' in '" + getValue(element) + "'");
		}
	}

	/**
	 * Checks Element Text contains has content If it does not,log an error
	 * 
	 * @param element
	 *            : WebElement of the text to check
	 * @param elementName
	 *            : Name of the element to appear on the report
	 */
	public void textMustNotBeEmpty(WebElement element, String elementName) {
		if (getText(element) != "") {
			logInfo(elementName + " contains text");
		} else {
			logError(elementName + " did not contain text");
		}
	}

	public void scrollToTop() {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0, 0)");
		logInfo("Scrolling to the Top");
	}

	public void scrollToBottom() {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		logInfo("Scrolling to the Bottom");
	}

	public void scrollDown(Integer pixelDistance) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,arguments[0]);", pixelDistance);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void select(WebElement element, String value, String elementName) {
		try {
			Select select = new Select(element);
			select.selectByVisibleText(value);
			logInfo("Selected : " + value + " from " + elementName);
		} catch (Exception e) {
			logError("Unable to Select  : " + value + " from " + elementName);
		}
	};

	public void selectByIndex(WebElement element, Integer index, String elementName) {
		try {
			Select drpDwn = new Select(element);
			drpDwn.selectByIndex(index);
			logInfo("Select index " + index + " in element " + elementName);
		} catch (Exception e) {
			logError("Unable to Select index: " + index + " from " + elementName);
		}
	}

	/**
	 * Used to access elements in an iFrame. Needed for resolving Captchas.
	 *
	 */
	public void switchToFrame(WebElement element, String elementName) {
		try {
			driver.switchTo().frame(element);
			logInfo("Switch to Frame " + elementName);
		} catch (Exception e) {
			logError("Unable to switch to Frame " + elementName);
		}
	};

	/**
	 * Used to switch back to the default content after accessing elements in an
	 * iFrame. Needed for resolving Captchas.
	 *
	 */
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logInfo("Switch back to Default Content");
	};

	public List<WebElement> dropDownOptions(WebElement element) {
		Select drpDwn = new Select(element);
		return drpDwn.getOptions();
	}

	public String randomDropDownSelectOption(WebElement dropDownElementLocator) {
		List<WebElement> theOptions = dropDownOptions(dropDownElementLocator);
		WebElement anOption;
		boolean hiddenOption;
		boolean gotOption = false;
		do {
			anOption = theOptions.get(new Random().nextInt(theOptions.size()));
			hiddenOption = isAttributePresent(anOption, "hidden");
			if (!hiddenOption) {
				gotOption = true;
			}
		} while (!gotOption);
		return anOption.getText();
	}

	public void toggleCheckboxON(WebElement element) {
		Boolean isChecked = element.findElement(By.tagName("input")).isSelected();
		if (!isChecked) {
			click(element, "Toggle Checkbox on");
		}
	}

	public void toggleCheckboxOFF(WebElement element) {
		Boolean isChecked = element.findElement(By.tagName("input")).isSelected();
		if (isChecked) {
			click(element, "Toggle checkbox off");
		}
	}

	public boolean isAttributePresentWithValue(WebElement element, String attribute, String attrValue) {
		String value = element.getAttribute(attribute);
		if (value.equalsIgnoreCase(attrValue)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAttributePresent(WebElement element, String attribute) {
		String value = element.getAttribute(attribute);
		if (value != null) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * Given a message Then write as information to the report engine And if
	 * StoryMode is enabled: Capture a screenshot
	 * 
	 * @param message
	 *            : String of what to write to the report
	 */
	public static void logInfo(String message) {
		ExtentListeners.testReport.get().info(message);
		if (DriverManager.getStoryMode()) {
			ExtentManager.captureScreenshot();
			try {
				ExtentListeners.testReport.get().info(
						"<b>" + "<font color=" + "blue>" + "Screenshot" + "</font>" + "</b>",
						MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotName).build());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void logError(String message) {
		ExtentListeners.testReport.get().error(message);
		ExtentManager.captureScreenshot();
		try {
			ExtentListeners.testReport.get().fail(
					"<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotName).build());
		} catch (IOException e) {
			ExtentListeners.testReport.get().info("!Unable to Capture Screen Shot!");
		}
		ExtentListeners.overAllFailure = true;
	}

	public static void logWarning(String message) {
		ExtentListeners.testReport.get().warning(message);
	}

	public static void logAnnotation(String message) {
		ExtentListeners.testReport.get().debug("<span style='background-color: darkblue'> <b>" + " <font color="
				+ "white>" + message + "</font>" + "</b></span>");
	}

	/**
	 * Returns if a file exists at the specified path.
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public Boolean fileExists(String path, String fileName) {
		if (path == null) {
			logError("Specified path is null, trying to use <user.home>/downloads");
			path = System.getProperty("user.home") + "/downloads/";
		}

		logInfo("Checking if " + fileName + " is present in " + path);
		File downloadedFile = new File(path + fileName);
		return downloadedFile.exists();
	}

	public static void createNewTab() {
		((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.open();");
		logInfo("Close Current Tab");
	}

	/***
	 * Close the Current Tab
	 */
	public static void closeCurrentTab() {
		DriverManager.getDriver().close();
	}

	/***
	 * Switch to a nominate tab in the browser Then write as information to the
	 * report engine And if StoryMode is enabled: Capture a screenshot
	 * 
	 * @param tabNumber
	 *            : integer of tab array to select
	 */
	public static void switchToTab(int tabNumber) {
		try {
			DriverManager.getDriver().switchTo()
					.window((String) DriverManager.getDriver().getWindowHandles().toArray()[tabNumber]);
			logInfo("Switch to tab " + tabNumber);
		} catch (Exception e) {
			logError("Unable to switch to " + tabNumber);
		}
	}
}
