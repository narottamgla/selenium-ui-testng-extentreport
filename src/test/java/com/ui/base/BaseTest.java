package com.ui.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

import com.common.utilities.ExcelReader;
import com.common.utilities.SlackNotification;
import com.ui.listeners.ExtentListeners;
import com.ui.utilities.DriverFactory;
import com.ui.utilities.DriverManager;
import com.ui.utilities.DriverManager.RunType;


public class BaseTest {

	private RemoteWebDriver driver;
	public Properties config = new Properties();
	private FileInputStream fis;
	public Logger log = Logger.getLogger(BaseTest.class);
	public String UserDir;
	public static String browser;
	public static String platform;
	public static String version;
	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "//src//test//resources//excel//testdata.xlsx");
	private ThreadLocal<String> sessionId = new ThreadLocal<String>();
	private boolean failTestSuite = false;

	
	
	@BeforeTest
	public void setUPFramework() {
		UserDir = System.getProperty("user.dir");
		configureLogging();
		DriverFactory.setConfigPropertyFile(UserDir + "/src/test/resources/properties/config.properties");
		
		try {
			fis = new FileInputStream(DriverFactory.getConfigPropertyFile());
			config.load(fis);
			log.info("Configuration File Loaded : " + DriverFactory.getConfigPropertyFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(System.getProperty("os.name"));
		if(System.getProperty("os.name").contains("Mac")) {
			DriverFactory.setChromeDriverExePath(UserDir + config.getProperty("CHROMEDRIVER_PATH"));
			DriverFactory.setIEDriverExePath(UserDir + config.getProperty("IEDRIVER_PATH"));
		} else {
			if (System.getProperty("os.arch").contains("x86")) {
				log.info("32 Bit System Detected");
				DriverFactory.setChromeDriverExePath(UserDir + config.getProperty("CHROMEDRIVER_PATH_WIN_32"));
				DriverFactory.setIEDriverExePath(UserDir + config.getProperty("IEDRIVER_PATH_WIN_32"));				
			} else {
				DriverFactory.setChromeDriverExePath(UserDir + config.getProperty("CHROMEDRIVER_PATH_WIN"));
				DriverFactory.setIEDriverExePath(UserDir + config.getProperty("IEDRIVER_PATH_WIN"));
			}
		}
		DriverManager.setGmailAccount(config.getProperty("QA_GMAIL_EMAIL_ADDRESS"));
		DriverManager.setGmailAPIProjectName(config.getProperty("GMAIL_APPLICATION_NAME"));
		DriverManager.setGmailAPICredentialsPath(config.getProperty("GMAIL_CREDENTIALS_FILE"));
		DriverManager.setGmailAPITokensPath(UserDir + config.getProperty("GMAIL_TOKENS_DIRECTORY_PATH"));
	}
	
	  @AfterMethod
	  public void teardown(ITestResult result) {
		if (DriverManager.getRunType().equals(RunType.SAUCE)) {
	      ((JavascriptExecutor)driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
		}
	    driver.quit();
	    log.info("Tear Down Completed"); // + result.getTestName());
		if (ExtentListeners.overAllFailure) {
	    	failTestSuite=true;
	    }
	  }

	  @AfterSuite
	  public void suiteTearDown() {
		if (failTestSuite) {
	    	Assert.fail("At least one test failed");
	    }
		else
		{
			if(DriverManager.slackEnabled()) {
				log.info("Sending Slack Message");
				SlackNotification.sendMessage("Test Suite Completed");
			}
		}
	  }
	
	private void createSauceDriver() {
        String username = config.getProperty("SAUCE_USERNAME");
        String accesskey = config.getProperty("SAUCE_ACCESS_KEY");
                
        //Create a new RemoteWebDriver, which will initialize the test execution on Sauce Labs servers
        String SAUCE_REMOTE_URL = "https://ondemand.saucelabs.com:443/wd/hub"; 
        URL url;
        log.info(SAUCE_REMOTE_URL);
        
	    /** The MutableCapabilities class  came into existence with Selenium 3.6.0 and acts as the parent class for
        all browser implementations--including the ChromeOptions class extension.
        Fore more information see: https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/MutableCapabilities.html */

       MutableCapabilities sauceOpts = new MutableCapabilities();
       sauceOpts.setCapability("name", "A MethodName");
       sauceOpts.setCapability("build", "Java-W3C-Examples");
       sauceOpts.setCapability("seleniumVersion", "3.141.59");
       sauceOpts.setCapability("username", username);
       sauceOpts.setCapability("accessKey", accesskey);
       sauceOpts.setCapability("tags", "patient tests");
        
        if(browser.equals("chrome")) {
        
	        /** ChomeOptions allows us to set browser-specific behavior such as profile settings, headless capabilities, insecure tls certs,
	        and in this example--the W3C protocol
	        For more information see: https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/chrome/ChromeOptions.html */
	
	       ChromeOptions chromeOpts = new ChromeOptions();
	       chromeOpts.setExperimentalOption("w3c", true);
		
	
	       /** Below we see the use of our other capability objects, 'chromeOpts' and 'sauceOpts',
	        defined in ChromeOptions.CAPABILITY and sauce:options respectively.
	        */
	       DesiredCapabilities caps = new DesiredCapabilities();
	       caps.setCapability(ChromeOptions.CAPABILITY,  chromeOpts);
	       caps.setCapability("sauce:options", sauceOpts);
	       caps.setCapability("browserName", "googlechrome");
	       caps.setCapability("browserVersion", "latest");
	       caps.setCapability("platformName", "windows 10");
	       
	        try {
	        	 url = new URL(SAUCE_REMOTE_URL);
	             driver = new RemoteWebDriver(url, caps);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

        } else {
        	
	
	       /** Below we see the use of our other capability objects, 
	        defined in InternetExplorerOptions.CAPABILITY and sauce:options respectively.
	        */
	       InternetExplorerOptions caps = new InternetExplorerOptions();
	       caps.setCapability("sauce:options", sauceOpts);
	       caps.setCapability("browserVersion", "latest");
	       caps.setCapability("platformName", "windows 10");
	       
	        try {
	        	 url = new URL(SAUCE_REMOTE_URL);
	             driver = new RemoteWebDriver(url, caps);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
        	
        }


        sessionId.set(driver.getSessionId().toString());

        // set current sessionId
        String id = (driver.getSessionId().toString());
        sessionId.set(id);
    }

    private void createLocalDriver() {
    	if(browser.equals("chrome")) {
    		
    		System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeDriverExePath());
			log.info("Loading Chromedriver : " + DriverFactory.getChromeDriverExePath());
			Map<String, Object> preferences = new HashMap<String, Object>();
			preferences.put("profile.default_content_setting_values.notifications", 2);
			preferences.put("credentials_enable_service", false);
			preferences.put("profile.password_manager_enabled", false);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", preferences);
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-infobars");
  
    		
    		driver = new ChromeDriver(options);
    		log.debug("Chrome Launched");
    		// driver.set(new ChromeDriver((ChromeOptions) capabilities));
    	} else {
    		System.setProperty("webdriver.ie.driver", DriverFactory.getIEDriverExePath());
			log.info("Loading IEdriver : " + DriverFactory.getIEDriverExePath());
    		InternetExplorerOptions options = new InternetExplorerOptions();
			options.destructivelyEnsureCleanSession();
			//options.disableNativeEvents();
			//options.requireWindowFocus();
			driver = new InternetExplorerDriver(options);
			log.debug("IE Launched");
    		// driver.set(new InternetExplorerDriver(options));
    	}
    }
	
	public void openBrowser() {
		
        DriverManager.setRunType(config.getProperty("RUN_TYPE") == null ? DriverManager.RunType.SAUCE : DriverManager.RunType.valueOf(config.getProperty("RUN_TYPE")));
        
		
		if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {
			browser = System.getenv("browser");
		} else {
			log.info("Getting Browser from Config");
		   	browser = config.getProperty("BROWSER");
		}
		
		if (System.getenv("platform") != null && !System.getenv("platform").isEmpty()) {
			platform = System.getenv("platform");
		} else {
			log.info("Getting platform from Config");
		   	platform = config.getProperty("PLATFORM");
		}
		
		if (System.getenv("version") != null && !System.getenv("version").isEmpty()) {
			version = System.getenv("version");
		} else {
			log.info("Getting version from Config");
		   	version = config.getProperty("VERSION");
		   	if (version.equals(null)) {
		   		version="latest";
		   		log.info("plaform set to " + platform);
		   	}
		}
		
		log.info("Using Browser Profile : " + browser);

		
		switch(DriverManager.getRunType()) {
        case LOCAL:
            createLocalDriver();
            break;
        case SAUCE:
    		log.info("Using Platform Profile : " + platform);
    		log.info("Using Browser version : " + version);
            createSauceDriver();
            break;
        }
		
		DriverManager.setBrowser(browser);
		DriverManager.setWebDriver(driver);
		log.info("Driver Initialized");
		DriverManager.getDriver().manage().window().maximize();
		DriverManager.setLoadTimeOut(Integer.parseInt((String) config.get("TIMEOUT_PAGE_LOAD")));
		DriverManager.setElementTimeOut(Integer.parseInt((String) config.get("TIMEOUT_ELEMENT_LOAD")));
		DriverManager.setStoryMode(Integer.parseInt((String) config.get("STORY_MODE")) == 1);
		DriverManager.setSlackHook(config.getProperty("SLACK_WEBHOOK") == null ? "" : config.getProperty("SLACK_WEBHOOK"));
		log.info("Slack Enabled? " + (DriverManager.slackEnabled() == false ? "No" : "Yes"));
		log.info("Story Mode Config: " + config.get("STORY_MODE"));
		log.info("TimeOut Pages : " + DriverManager.getLoadTimeOut() + " Elements: " + DriverManager.getElementTimeOut() );
		
	}
	
	public void quit() {
		DriverManager.getDriver().quit();
		log.info("Test Execution Complete");
	}
	
	public void configureLogging() {
		String log4jConfigFile = UserDir +  "//src//test//resources//properties//log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);
	}
	
	public void logInfo(String message) {	
		ExtentListeners.testReport.get().info(message);
	}

}
