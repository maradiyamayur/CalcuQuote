package QuoteCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_MarkasSent extends BaseInitquoteCQ {
	String quoteId;
	String timenow;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ - MARK AS SENT PUBLISHED QUOTE")
	public void markSent() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("----------------------------------------------------------------------------");
		log.info("CASE : MARK AS SENT PUBLISHED QUOTE");
		System.out.println("CASE : MARK AS SENT PUBLISHED QUOTE");
		complete();
	}

	public void complete() throws InterruptedException {
		waitforseconds(25);
		CheckXpath("SUBMIT_ACTION").click();
		waitforseconds(3);
		boolean marksent = false;
		try {
			marksent = driver.findElement(By.xpath(".//*[@id='btnCompleteStatus' and @class='linkDisabled']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (marksent) {
			log.info("~>>RESULT ::  Mark as Sent is disabled");
		} else {
			waitforseconds(3);
			CheckXpath("MARK_SENT").click();
			alertacceptpopup();
			waitforseconds(5);
			new Select(CheckXpath("QUOTE_STATUS")).selectByVisibleText("Won");
			waitforseconds(5);
			new Select(CheckXpath("QUOTE_CLOSED_QTY")).selectByVisibleText("Other Quantity");
			waitforseconds(5);
			CheckXpath("QUOTE_OTHER_QTY").sendKeys("15");
			waitforseconds(5);
			CheckXpath("QUOTE_STATUS_SUBMIT").click();
			log.info("~>>RESULT ::  MARK AS SENT QUOTE DONE");
			System.out.println("~>>RESULT ::  MARK AS SENT QUOTE DONE");
		}
	}

}