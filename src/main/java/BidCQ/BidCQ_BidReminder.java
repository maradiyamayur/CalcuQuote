package BidCQ;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BidCQ_BidReminder extends BaseInitbidCQ {

	BidCQ_BidGenerate BG = new BidCQ_BidGenerate();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. SEND BID REMINDER FROM MATERIAL COSTING ")
	public void bidReminderMc() throws Exception {
		headerFormat("#. CASE : SEND BID REMINDER FROM MATERIAL COSTING");
		windowHandle();
		BG.checkstatus("SELECT_ALL_LINES");
		BG.addtobid(0);
		waitforseconds(5);
		switchToNewtabWithUrl(driver, 0);
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MC_ACTIONS_MENU"));
		log.info("~>> Clicked on Material Costing Actions Menu");
		Actions action = new Actions(driver);
		action.moveToElement(CheckXpath("MC_ACTIONS_BIDSHEET")).perform();
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("MC_ACTIONS_SHOWBID"));
		log.info("~>> Bid Status Opened");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MC_ACTIONS_SHOWBID_MENU"));
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("MC_ACTIONS_SEND_REMINDER"));
		checkSendReminderAlert(1);
		waitforseconds(10);
		driver.findElement(By.xpath(".//*[@id='bidheader']//span[@ng-click='closeDrower()']")).click();
		System.out.println(
				".............................................................................................");
		log.info(".............................................................................................");
	}

	@Test(priority = 2, description = "#. SEND BID REMINDER FROM MATERIAL COSTING BID TILE")
	public void bidReminderTile() throws Exception {
		System.out.println("** SEND BID REMINDER FROM MATERIAL COSTING BID TILE");
		log.info("** SEND BID REMINDER FROM MATERIAL COSTING BID TILE");
		waitforseconds(8);
		boolean suppliercarousel = false;
		try {
			suppliercarousel = CheckXpath("SUPPLIER_CAROUSEL").isDisplayed();
		} catch (Exception e) {

		}
		if (suppliercarousel) {
			safeJavaScriptClick(CheckXpath("SUPPLIER_DROPDOWN"));
			safeJavaScriptClick(CheckXpath("SUPPLIER_SENDREMINDER"));
			checkSendReminderAlert(2);
		}
	}

	/*
	 * @Test(priority = 3, description = "#. SEND BID REMINDER FROM BID MANAGEMENT")
	 * public void bidReminder() throws Exception { waitforseconds(50);
	 * System.out.println("** SEND BID REMINDER"); log.info("** SEND BID REMINDER");
	 * String bidmgmt = "https://" + getHost()[0] + getHost()[1] +
	 * "/#/BidManagement"; switchToNewtabWithUrl(driver, 1); waitforseconds(5);
	 * driver.get(bidmgmt); waitforseconds(5); driver.get(bidmgmt);
	 * waitforseconds(10); BidCQ_Filter.existFilter(); waitforseconds(5); WebElement
	 * scrollbar = driver.findElement( By.
	 * xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"
	 * )); ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
	 * waitforseconds(5); new
	 * Select(CheckXpath("BID_STATUS_FILTER")).selectByVisibleText("Open");
	 * CheckXpath("BID_SUPPLIER_EMAIL").sendKeys(
	 * "payal.nanavati@triveniglobalsoft.com"); waitforseconds(10); boolean records
	 * = false; try { records = driver.findElement(By.xpath(
	 * "(.//div[contains(@ng-click,'selectButtonClick')])[1]")) .isDisplayed(); }
	 * catch (Exception e) {
	 * 
	 * } if (records) { driver.findElement(By.xpath(
	 * "(.//div[contains(@ng-click,'selectButtonClick')])[1]")).click();
	 * waitforseconds(3); safeJavaScriptClick(CheckXpath("BID_MGMT_ACTION"));
	 * waitforseconds(3); safeJavaScriptClick(CheckXpath("BID_MGMT_REMINDER"));
	 * waitforseconds(3); checkAlert(); } }
	 */
	public void checkSendReminderAlert(int type) throws Exception {
		boolean popup = false;
		try {
			popup = driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).isDisplayed();
		} catch (Exception e) {

		}
		if (popup) {
			if (type == 1) {
				driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
				System.out.println("~>> Yes Add Email address");
				CheckXpath("BID_CC_EMAIL").sendKeys("tgs.payal@outlook.com");
				CheckXpath("BID_CC_EMAIL").click();
				waitforseconds(5);
				safeJavaScriptClick(CheckXpath("BID_CC_EMAIL_SAVE"));
			} else if (type == 2) {
				driver.findElement(By.xpath(".//button[@class='swal2-cancel swal2-styled']")).click();
				System.out.println("~>> No, email the same list");
			}
			log.info("~>> Alert accepted");

		} else {
			log.info("~>> No alert");
		}
		waitforseconds(5);
		boolean alert = false;

		try {
			alert = driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).isDisplayed();
		} catch (Exception e) {

		}
		if (alert) {
			driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("BID_CC_EMAIL_SAVE"));
			WebElement toastAlert = driver.findElement(By.xpath(".//*[@id='toast-container']/div/div[2]"));
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(toastAlert));
			if (toastAlert.getText().contains("Send bid(s) reminder is in progress.")) {
				System.out.println("~>> RESULT :: " + toastAlert.getText());
				Assert.assertTrue(true);
			} else {
				System.out.println("~>> RESULT :: No Alert Displayed");
				Assert.assertTrue(false);
			}
		} else {
			log.info("~>> No Alert");
		}

	}
}