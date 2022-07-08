package ShopCQ;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ShopCQ_ConsolidateLineitem extends BaseInitshopCQ {

	private String comment;
	private int expResult;
	int Line1, Line2;
	private String exp_result;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - CONSOLIDATE LINE ITEM")
	public void consolidateMain() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : CONSOLIDATE LINE ITEM");
		System.out.println("CASE : CONSOLIDATE LINE ITEM");
		demandList();
		waitforseconds(35);
		//gridmenu();
		searchRecord();
		checkRecord();
	}

	public void searchRecord() throws Exception {
		waitforseconds(8);
		CheckXpath("DL_STATUS_FILTER").sendKeys("Supplier Selected");
		waitforseconds(8);
		CheckXpath("DL_TAGS_FILTER").sendKeys("shreyas");
		waitforseconds(8);
		CheckXpath("DL_ORIGINAL_MPN_FILTER").sendKeys("5014");
		log.info("~>> Searching Done");
		waitforseconds(10);

		Boolean records = false;
		try {
			records = driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).isDisplayed();
		} catch (Exception e) {

		}
		if (records) {
			waitforseconds(8);
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).click();
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[3]")).click();
			log.info("~>> Records selcted to consolidate");
			consolidate();
		} else {
			log.info("~>> NO RECORDS TO CONSOLIDATE");
			System.out.println("~>> NO RECORDS TO CONSOLIDATE");
		}
	}

	public void consolidate() throws Exception {
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("DL_EDIT_BTN"));
		log.info("~>> Edit button clicked");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("DL_EDIT_CONSOLIDATE"));
		log.info("~>> Consolidate button clicked");
		waitforseconds(5);
		boolean noconsolidateitem = false;
		try {
			noconsolidateitem = driver
					.findElement(
							By.xpath(".//*[@id='consolidateDemandLine']//h2[@class='NoDataToConsolidate ng-scope']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (noconsolidateitem) {
			log.info("~>> RESULT :: NOTHING TO CONSOLIDATE");
			System.out.println("~>> RESULT :: NOTHING TO CONSOLIDATE");
			driver.findElement(By.xpath(".//*[@id='btnCancel']")).click();
			waitforseconds(5);
		} else {
			boolean popup = false;
			try {
				popup = driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).isDisplayed();
			} catch (Exception e) {

			}
			if (popup) {
				driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
				log.info("~>> Update pricing is already running for previously selected demand line(s)");
				System.out.println("~>> Update pricing is already running for previously selected demand line(s)");
			} else {
				log.info("~>> No alert");
				// targetPrice();
				// Thread.sleep(5000);
				waitforseconds(5);
				WebElement Consolidatecomment = CheckXpath("DL_CONSOLIDATE_COMMENT");
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", Consolidatecomment);
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				comment = dtf.format(now);
				Consolidatecomment.sendKeys(comment);
				log.info("~>> Merge Line Comment Added ");

				waitforseconds(5);
				safeJavaScriptClick(CheckXpath("DL_CONSOLIDATE_SUBMIT"));
				log.info("~>> Submit button clicked");
				log.info("~>> RESULT-1 :: DEMAND LINE CONSOLIDATED");
				alertacceptpopup();
				System.out.println("~>> RESULT-1 :: DEMAND LINE CONSOLIDATED");
				waitforseconds(45);
				// pricingstatus();
				// verification();
			}

		}
	}

	public void targetPrice() {
		exp_result = driver
				.findElement(
						By.xpath(".//*[@id='modal-body']//table//tr[@ng-repeat='item in lineData'][3]//td[7]//span"))
				.getText();
		log.info("~>> Actual Target Price :: " + exp_result);
	}

	public void targetPrice1() {

		String Line_1 = driver
				.findElement(
						By.xpath(".//*[@id='modal-body']//table//tr[@ng-repeat='item in lineData'][1]//td[7]//span"))
				.getText();

		String Line_2 = driver
				.findElement(
						By.xpath(".//*[@id='modal-body']//table//tr[@ng-repeat='item in lineData'][2]//td[7]//span"))
				.getText();

		if (Line_1 == " " && Line_2 == " ") {
			log.info("~>> target price is empty");
		} else {
			Line1 = Integer.parseInt(Line_1);
			Line2 = Integer.parseInt(Line_2);
			expResult = Math.min(Line1, Line2);
			log.info("~>> Expected Target Price :: " + expResult);
			int actResult = Integer.parseInt(driver
					.findElement(By
							.xpath(".//*[@id='modal-body']//table//tr[@ng-repeat='item in lineData'][3]//td[7]//span"))
					.getText());
			log.info("~>> Actual Target Price :: " + actResult);
			if (expResult == actResult) {
				log.info("~>> Target Value for consolidate item is correct on Consolidate screen");
			} else {
				log.info("~>> Target Value for consolidate item is wrong on Consolidate screen");
			}
		}
	}

	public void pricingstatus() {
		boolean status = false;
		try {
			status = driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']")).isDisplayed();
		} catch (Exception e) {

		}
		if (status) {
			log.info("~>> Pricing percentage displayed");
			log.info("~>> Pricing Running");
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(200));
			wait.until(ExpectedConditions
					.invisibilityOf(driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']"))));
			log.info("~>> Pricing Completed");
		} else {
			log.info("~>> Pricing Percentage not displayed");
		}
	}

	public void verification() throws InterruptedException {
		waitforseconds(5);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
		CheckXpath("DL_FILENAME_FILTER").sendKeys("Consolidation");
		CheckXpath("DL_COMMENT_FILTER").sendKeys(comment);
		log.info("~>> Searching done");
		waitforseconds(5);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft -=1000", scrollbar);
		waitforseconds(5);
		String id = CheckXpath("DL_TARGET_PRICE_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-0-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		waitforseconds(5);

		String Strtarget = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div")).getText()
				.replaceAll("[^0-9]", "");
		// int dltargetprice = Integer.parseInt(Strtarget);
		waitforseconds(5);
		// log.info("~>> Target Price after Consolidate :: " + dltargetprice);
		if (Strtarget.contains(exp_result)) {
			log.info("~>> RESULT-2 :: TARGET PRICE IS CORRECT");
			System.out.println("~>> RESULT-2 :: TARGET PRICE IS CORRECT");
		} else {
			log.info("~>> RESULT-2 :: TARGET PRICE IS WRONG");
			System.out.println("~>> RESULT-2 :: TARGET PRICE IS WRONG");
		}
	}

	public void checkRecord() {
		boolean isRecord = false;
		try {
			isRecord = driver.findElement(By.xpath(".//div[contains(@ng-click,'selectButtonClick') and @style='']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (isRecord) {
			List<WebElement> lstRecord = driver
					.findElements(By.xpath(".//div[contains(@ng-click,'selectButtonClick') and @style='']"));
			for (WebElement ele : lstRecord) {
				ele.click();
			}
		} else {
			log.info("No existing records selected");
		}
		waitforseconds(5);
	}
}
