package utility;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import parentclass.BaseInit;

public class UtilityMethods extends BaseInit {

	public static String Username = prop.getProperty("CQ_USERNAME");
	public static String url;
	static Map<String, Object[]> TestNGResults;

	public enum operationtype {
		eclick, eclear, esendkeys
	}

	public static void signIN(String cqUrl) throws InterruptedException, IOException {
		driver.get(cqUrl);
		waitforseconds(10);	
		log.info("~>> Enter UserName");
		CheckXpath("USER_NAME").clear();
		CheckXpath("USER_NAME").sendKeys(Username);
		log.info("~>> Enter Password");
		CheckXpath("PASSWORD").clear();
		// CheckXpath("PASSWORD").sendKeys(Password);
		CheckXpath("PASSWORD").sendKeys(prop.getProperty("CQ_PASSWORD"));
		log.info("~>> Click on Login Button");
		CheckXpath("LOGIN").click();
		waitforseconds(5);
		url = driver.getCurrentUrl().replace("/#/DashBoard", "");
	}

	public static boolean checkExecutionModeTestSuite(XLSDatatable_Connectivity data, String testSuiteName,
			String sheetName) {
		int rows = data.totalRow(sheetName);
		for (int row = 2; row <= rows; row++) {
			if (data.getData(sheetName, "TestSuiteID", row).equalsIgnoreCase(testSuiteName)) {
				if (data.getData(sheetName, "Execute", row).equalsIgnoreCase("y"))
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public static boolean checkExecutionModeTestCase(XLSDatatable_Connectivity data, String testCaseName,
			String sheetName) {
		int rows = data.totalRow(sheetName);
		for (int row = 2; row <= rows; row++) {
			if (data.getData(sheetName, "TestID", row).equalsIgnoreCase(testCaseName)) {
				if (data.getData(sheetName, "Execute", row).equalsIgnoreCase("y"))
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public static void ExplicitWait_Operation(String Locator, operationtype OT, String optionalsendkeys, String type) {
		try {
			WebElement Operation_wait = null;
			if (type == "XPATH")
				Operation_wait = CheckXpath(Locator);
			else if (type == "CSS")
				Operation_wait = CheckCss(Locator);
			else if (type == "LINK")
				Operation_wait = CheckLink(Locator);
			else if (type == "ID")
				Operation_wait = CheckID(Locator);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
					wait.until(ExpectedConditions.elementToBeClickable(Operation_wait));
			switch (OT) {
			case eclick:
				((WebElement) wait).click();
				break;
			case eclear:
				((WebElement) wait).clear();
				break;
			case esendkeys:
				((WebElement) wait).sendKeys(optionalsendkeys);
				break;
			}
		} catch (Exception e) {
		}
	}

	public static void StaleElement(WebElement element) {
		boolean staleElement = true;
		while (staleElement) {
			try {
				element.click();
				staleElement = false;
			} catch (StaleElementReferenceException e) {
				staleElement = true;
			}
		}
	}

	public static void submit_time() {
		log.info("~>> Checking Upload time");
		long start = System.currentTimeMillis();
		long finish = System.currentTimeMillis();
		long totalTime = finish - start;
		long secondtime = (totalTime / 1000) % 60;
		System.out.println(">>> Time to login  ::  " + secondtime + " Second(s)");
		log.info("Time to login   ::  " + secondtime + " Second(s)");
	}

	public static void waitForpageload() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	}

	public static void ClearBrowserCache() {
		driver.manage().getCookies();
		driver.manage().deleteAllCookies();
		log.info("~>> Cache Cleared");
	}

	public static boolean isAlertPresents() {
		try {
			driver.switchTo().alert();
			return true;
		} // try
		catch (Exception e) {
			return false;
		} // catch
	}
}
