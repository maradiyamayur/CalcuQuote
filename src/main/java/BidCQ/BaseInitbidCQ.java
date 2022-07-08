package BidCQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitbidCQ extends BaseInit {

	protected String user = "payal.nanavati@triveniglobalsoft.com";

	protected static String RFQ_ID, ASM_ID, ASM_NUMBER;
	String asmId, quoteId, lineItem;

	@BeforeSuite
	public void checkExecutionModeTestSuite() throws IOException {
		startup();
		// Code to check execution mode of Test Suite
		if (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "BidCQ", "TestSuite")) {
			throw new SkipException("Execution mode of the test suite is set to NO");
		}
	}

	public String autoId() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String autoid = salt.toString();
		return autoid;
	}

	public static void switchToNewtabWithUrl(WebDriver driver, int tab) {
		((JavascriptExecutor) driver).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tab));
	}

	public void removeFilter() throws InterruptedException {
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

	public void openBid(int type) throws Exception {
		removeFilter();
		waitforseconds(5);
		String id = CheckXpath("BID_STATUS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		boolean open = false;
		try {
			open = driver
					.findElement(By.xpath("(.//div[contains(@id,'" + id1
							+ "')]//div[contains(@class,'bidsheet-status') or contains(@class,'shared-Status')])[1]"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (open) {
			waitforseconds(5);
			driver.findElement(By.xpath("(.//div[contains(@id,'" + id1
					+ "')]//div[contains(@class,'bidsheet-status') or contains(@class,'shared-Status')])[1]")).click();
			alertacceptpopup();
			bidInfoPopup();
			if (type == 1) {
				waitforseconds(8);
				lineItem = driver.findElement(By.xpath("(.//i[contains(@class,'doc-btn')])[1]//preceding::td"))
						.getText();
				driver.findElement(By.xpath("(.//i[contains(@class,'doc-btn')])[1]")).click();
				waitforseconds(5);
				asmId = driver.findElement(By.xpath(".//strong[contains(text(),'Assembly Id')]//parent::small"))
						.getText().replaceAll("[^\\d]", "");
				System.out.println("~>> Assembly Id :: " + asmId);
				quoteId = driver.findElement(By.xpath(".//strong[contains(text(),'Quote')]//parent::small")).getText()
						.replaceAll("[^\\d]", "");
				System.out.println("~>> Quote Id ::  " + quoteId);
			}
			if (type == 2) {
				asmId = driver
						.findElement(
								By.xpath(".//*[@id='sub-header']//a[@ng-click='openDrower(bidSheetSummary.Id)']//u"))
						.getText();
				System.out.println("~>> Assembly Id :: " + asmId);
			}
		} else {
			log.info("~>> No Open Bid for document attachment");
		}
	}

	public void bidInfoPopup() throws Exception {
		waitforseconds(2);
		boolean bidinfo = false;
		try {
			bidinfo = driver
					.findElement(By.xpath(".//div[@class='modal-header HeaderText']//span[@ng-click='closeDrower()']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (bidinfo) {
			log.info("~>> Bid Information popup displayed");
			System.out.println("~>> Bid Information popup displayed");
			quoteId = driver.findElement(By.xpath(
					".//div[@class='btn-info-content']//label[contains(text(),'Quote ID:')]//following-sibling::small"))
					.getText();
			asmId = driver.findElement(By.xpath(
					".//div[@class='btn-info-content']//label[contains(text(),'Assembly ID:')]//following-sibling::small"))
					.getText();
			safeJavaScriptClick(CheckXpath("BID_INFO"));
		} else {
			log.info("~>> Bid Information popup not displayed");
			System.out.println("~>> Bid Information popup not displayed");
		}
	}

	public static void windowHandle() {
		String originalHandle = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(originalHandle)) {
				driver.switchTo().window(handle);
				driver.close();
			}
		}
		driver.switchTo().window(originalHandle);
	}
}
