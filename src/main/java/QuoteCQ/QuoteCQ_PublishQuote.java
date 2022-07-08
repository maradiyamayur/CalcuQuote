package QuoteCQ;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_PublishQuote extends BaseInitquoteCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ - PUBLISH QUOTE")
	public void publishmain() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("----------------------------------------------------------------------------");
		System.out.println("CASE : PUBLISH QUOTE");
		log.info("CASE : PUBLISH QUOTE");
		publishquote();
	}

	public void publishquote() throws Exception {
		waitforseconds(8);
		if (driver.findElement(By.xpath(".//button[@ng-click='submitFinalQuote()']")).getAttribute("aria-disabled")
				.equals("false")) {
			safeJavaScriptClick(CheckXpath("PUBLISH_QUOTE"));
			waitforseconds(5);
			alertacceptpopup();
			waitforseconds(8);
			log.info("~>> RESULT :: QUOTE PUBLISHED");
			System.out.println("~>> RESULT :: QUOTE PUBLISHED");
		} else {
			log.info("~>> RESULT :: PUBLISH QUOTE IS DISABLED");
			System.out.println("~>> RESULT :: PUBLISH QUOTE IS DISABLED");
		}
		log.info("================================================================================");
	}
}