package BidCQ;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.QuoteCQ_ImportBOM;
import utility.UtilityMethods;

public class BidCQ_DeleteBOM extends BaseInitbidCQ {

	BidCQ_BidGenerate BG = new BidCQ_BidGenerate();
	QuoteCQ_ImportBOM IB = new QuoteCQ_ImportBOM();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. DELETE BOM AFTER BID GENERATED FOR OPEN BID")
	public void deleteBOMOpen() throws Exception {
		headerFormat("#. CASE : DELETE BOM AFTER BID GENERATED FOR OPEN BID");
		windowHandle();
		driver.get("https://" + getHost()[0] + getHost()[1] + "/#/DashBoard");
		waitforseconds(15);
		BG.checkstatus("SELECT_ALL_LINES");
		BG.addtobid(0);
		bidStatus(1, "rgb(26, 113, 210)", "BLUE");
		verification();
		windowHandle();
	}

	@Test(priority = 2, description = "#. DELETE LINE AFTER BID GENERATED FOR OPEN BID")
	public void deleteLineOpen() throws Exception {
		log.info("** DELETE LINE AFTER BID GENERATED FOR OPEN BID ");
		System.out.println("** DELETE LINE AFTER BID GENERATED FOR OPEN BID");
		BG.importMain();
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		waitforseconds(12);
		safeJavaScriptClick(CheckXpath("SELECT_LINE_1"));
		log.info("~>> Lines selected");
		BG.addtobid(0);
		bidStatus(2, "rgb(26, 113, 210)", "BLUE");
		verification();
		windowHandle();
	}

	@Test(priority = 3, description = "#. DELETE LINE AFTER BID GENERATED FOR IN PROGRESS BID")
	public void deleteLineProgress() throws Exception {
		log.info("** DELETE LINE AFTER BID GENERATED FOR OPEN BID ");
		System.out.println("** DELETE LINE AFTER BID GENERATED FOR OPEN BID");
		checkBOM();
		safeJavaScriptClick(CheckXpath("SELECT_LINE_1"));
		log.info("~>> Lines selected");
		BG.addtobid(0);
		bidStatus(2, "rgb(255, 193, 6)", "YELLOW");
		verification();
		windowHandle();
	}

	@Test(priority = 4, description = "#. DELETE BOM AFTER BID GENERATED FOR IN PROGRESS BID")
	public void deleteBOMProgress() throws Exception {
		log.info("** DELETE BOM AFTER BID GENERATED FOR IN PROGRESS  BID ");
		System.out.println("** DELETE BOM AFTER BID GENERATED FOR IN PROGRESS  BID");
		checkBOM();
		safeJavaScriptClick(CheckXpath("SELECT_ALL_LINES"));
		/*
		 * BG.checkstatus("SELECT_ALL_LINES"); BG.addtobid(0);
		 */
		BG.addtobid(0);
		bidStatus(1, "rgb(255, 193, 6)", "YELLOW");
		verification();
		windowHandle();
	}

	public void checkBOM() throws Exception {
		safeJavaScriptClick(CheckXpath("BOM_TAB"));
		waitforseconds(10);
		IB.submitBOM();
		IB.chkLaborupdate();
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		waitforseconds(12);
	}

	public void bidStatus(int type, String color, String colorName) throws Exception {
		switchToNewtabWithUrl(driver, 0);
		waitforseconds(5);
		driver.navigate().refresh();
		waitforseconds(8);
		BG.getstatus("Send", color, colorName, "");
		if (colorName.endsWith("BLUE")) {
			System.out.println("~>> Bid is in OPEN Status");
		} else if (colorName.endsWith("YELLOW")) {
			System.out.println("~>> Bid is in IN PROGRESS Status");
		}
		safeJavaScriptClick(CheckXpath("BOM_TAB"));
		waitforseconds(10);
		driver.navigate().refresh();
		waitforseconds(10);
		if (type == 1) {
			safeJavaScriptClick(CheckXpath("SELECT_ALL_LINES"));
			safeJavaScriptClick(CheckXpath("BOM_ACTIONS_BUTTON"));
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("DELETE_BOM"));
			waitforseconds(8);
			alertacceptpopup();
			log.info("~>> BOM Deleted Successfully");
		} else if (type == 2) {
			waitforseconds(5);
			WebElement line = driver.findElement(By.xpath(".//button[@id='0_button']//i"));
			safeJavaScriptClick(line);
			boolean delete = false;
			try {
				delete = driver.findElement(By.xpath(".//a[contains(text(),'Delete Line')]")).isEnabled();
			} catch (Exception e) {

			}
			if (delete) {
				waitforseconds(5);
				driver.findElement(By.xpath(".//a[contains(text(),'Delete Line')]")).click();
				alertacceptpopup();
				log.info("~>> Line Item Deleted Successfully");
			}
			driver.navigate().refresh();
			waitforseconds(8);
			boolean spinner = false;
			try {
				spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
			} catch (Exception e) {

			}
			if (spinner) {
				WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(100));
				wait.until(ExpectedConditions
						.visibilityOf(driver.findElement(By.xpath(".//i[@ng-click='OpenBOMImportUsingWidget()']"))));
			}
		}
	}

	public void verification() throws Exception {
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		waitforseconds(5);
		driver.navigate().refresh();
		waitforseconds(10);
		List<WebElement> bidtiles = driver.findElements(By.xpath(".//div[@class='item cart ng-scope']"));
		if (bidtiles.isEmpty()) {
			log.info("~>> RESULT :: BID DELETED SUCCESSFULLY");
			System.out.println("~>> RESULT :: BID DELETED SUCCESSFULLY");
			Assert.assertTrue(true);
		} else {
			log.info("~>> RESULT :: BID EXISTS");
			System.out.println("~>> RESULT :: BID EXISTS");
			Assert.assertTrue(false);
		}
		System.out.println(
				"............................................................................................................................");
	}

	public void bidsheet() throws Exception {
		waitforseconds(10);
		driver.navigate().refresh();
		alertacceptpopup();
		waitforseconds(10);
		// wait("visible", "BID_INFO");
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
			safeJavaScriptClick(CheckXpath("BID_INFO"));
		} else {
			log.info("~>> Bid Information popup not displayed");
			System.out.println("~>> Bid Information popup not displayed");
		}
		waitforseconds(20);
		int intunit = BG.getIndex("Unit Price");
		WebElement Unitprice = driver
				.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[" + intunit + "]"));
		BG.updatebid(Unitprice, "12");
		BG.updatebid(Unitprice, "12");
		waitforseconds(5);
		BG.savedraft();
		alertacceptpopup();
		waitforseconds(5);
	}

}