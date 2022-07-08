package QuoteCQ;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import BOM.BaseInitBOM;
import utility.UtilityMethods;

public class QuoteCQ_SubmitSummary extends BaseInitquoteCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ -  SUBMIT SUMMARY")
	public void submitmain() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("----------------------------------------------------------------------------");
		System.out.println("CASE :  SUBMIT SUMMARY");
		log.info("CASE :  SUBMIT SUMMARY");
		BaseInitBOM.quoteOpen();
		submit();
	}

	public void submit() throws Exception {
		waitforseconds(12);
		safeJavaScriptClick(CheckXpath("SUBMIT_TAB"));
		log.info("~>> Redirected to Summary Tab ");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
		wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath(".//button[@ng-click='submitQuotation(false)' and @aria-disabled='false']")));
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("SUBMIT_SUMMARY"));
		log.info("~>> Click on Summary submit");
		waitforseconds(8);
		log.info("~>> RESULT :: SUMMARY SUBMITTED");
		System.out.println("~>> RESULT :: SUMMARY SUBMITTED");
		log.info("================================================================================");
	}
}