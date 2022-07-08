package ShopCQ;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitshopCQ extends BaseInit {

	@BeforeSuite
	public void checkExecutionModeTestSuite() throws IOException {
		startup();
		// Code to check execution mode of Test Suite
		if (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "ShopCQ", "TestSuite")) {
			throw new SkipException("Execution mode of the test suite is set to NO");
		}
	}

	public void demandList() throws Exception {
		waitforseconds(10);
		Boolean list = false;
		try {
			list = CheckXpath("DEMAND_LIST_HEADER").isDisplayed();
		} catch (Exception e) {

		}
		if (list) {
			log.info("~>> Demand List is open already");
			waitforseconds(5);
		} else {
			safeJavaScriptClick(CheckXpath("SHOP_CQ"));
			waitforseconds(10);
			String demandList = driver.getCurrentUrl().replace("shopcqhome", "demandlist");
			driver.get(demandList);
			// safeJavaScriptClick(CheckXpath("DEMAND_LIST"));
			log.info("~>> Clicked on Demand list");
			waitforseconds(5);
		}
	}

	public void gridmenu() throws Exception {
		log.info("~>> Checking Grid Menu");
		waitforseconds(20);
		safeJavaScriptClick(CheckXpath("DEMAND_GRID"));
		log.info("~>> Grid Menu filter clicked");
		// Thread.sleep(8000);
		boolean filtermenu = false;
		try {
			filtermenu = driver.findElement(By.xpath(".//div[@ng-show='shownMid']")).isDisplayed();
		} catch (Exception e) {

		}
		if (filtermenu) {
			List<WebElement> lstmenu = driver.findElements(By.xpath(
					".//ul[@class='ui-grid-menu-items']//li//button[@class='ui-grid-menu-item ng-binding']//i[contains(@class,'ui-grid')]//parent::button[normalize-space(text())]"));
			log.info("~>> Total fields :: " + lstmenu.size());
			waitforseconds(5);
			// for (WebElement menu : lstmenu)

			for (int i = 0; i < lstmenu.size(); i++) {
				if (lstmenu.get(i).getAttribute("class").equals("ui-grid-icon-cancel")) {
					log.info(">> " + i + lstmenu.get(i).getText() + " ::  Disabled");
					safeJavaScriptClick(lstmenu.get(i));
					log.info(">> " + i + lstmenu.get(i).getText() + " ::  Enabled now");
				} else {
					log.info(">> " + i + lstmenu.get(i).getText() + " ::  Already Enabled");
				}
				waitforseconds(5);
			}
		} else {
			log.info("~>> Filter is not available");
		}
	}

	public static void removeFilter() throws InterruptedException {
		waitforseconds(5);
		log.info("~>> Checking for existing filter");
		List<WebElement> listfilter = driver.findElements(
				By.xpath(".//div[@class='ui-grid-filter-button ng-scope']//i[@class='ui-grid-icon-cancel']"));
		log.info("~>> No of Filter :: " + listfilter.size());
		for (WebElement filter : listfilter) {
			JavascriptExecutor je = (JavascriptExecutor) driver;
			je.executeScript("arguments[0].scrollIntoView(true);", filter);
			waitforseconds(5);
			je.executeScript("arguments[0].click()", filter);
			// filter.click();
			log.info("~>> filter removed");
			waitforseconds(5);
		}
	}

}
