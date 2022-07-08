package BidCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BidCQ_BidManagement extends BaseInitbidCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: BCQ -  ALTERNATE PART ADDED FROM BIDCQ")
	public void bidMgmt() throws Exception {
		headerFormat("#. CASE : BID MANAGEMENT");
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("BID_MANAGEMENT"));
		log.info("~>> Clicked on Bid Management");
		search();
	}

	public void search() throws InterruptedException {
		removeFilter();
		waitforseconds(15);
		CheckXpath("BID_ASM_ID_INPUT").clear();
		CheckXpath("BID_ASM_ID_INPUT").sendKeys(ASM_ID);
		log.info("~>> Assembly id added");
		// CheckXpath("BID_ASM_ID_INPUT").sendKeys("61835");
		waitforseconds(5);
		boolean recordexist = false;
		try {
			recordexist = driver.findElement(By.xpath("(.//div[contains(@ng-click,'selectButtonClick')])[1]"))
					.isDisplayed();

		} catch (Exception e) {

		}
		if (recordexist) {
			log.info("~>> Records serached successfully");
			WebElement scrollbar = driver.findElement(
					By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
			waitforseconds(8);
			WebElement status = driver.findElement(By.xpath(".//select[@ng-model='colFilter.term']"));
			new Select(status).selectByVisibleText("Submitted");
			log.info("~>> Status submitted selected");
			waitforseconds(15);

			CheckXpath("BID_VIEW_BTN").click();
			log.info("~>> Clicked on bid view button");
			waitforseconds(15);
			boolean bidsheetview = false;
			try {
				bidsheetview = CheckXpath("BID_SHEET_VIEW").isDisplayed();
			} catch (Exception e) {

			}
			if (bidsheetview) {
				log.info("~>> RESULT :: BIDSHEET VIEW DISPLAYED");
				System.out.println("~>> RESULT :: BIDSHEET VIEW DISPLAYED");
			} else {
				log.info("~>> RESULT ::  BIDSHEET VIEW NOT DISPLAYED");
				System.out.println("~>> RESULT :: BIDSHEET VIEW  NOT DISPLAYED");
			}
		} else {
			log.info("~>> SEARCHED BID NOT FOUND");
			System.out.println("~>> SEARCHED BID NOT FOUND");
		}
	}
}
