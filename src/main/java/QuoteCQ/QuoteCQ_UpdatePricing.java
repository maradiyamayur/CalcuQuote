package QuoteCQ;

import java.text.DecimalFormat;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_UpdatePricing extends BaseInitquoteCQ {

	public double start, finish;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ - UPDATE PRICING")
	public void pricingmain() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE :  UPDATE PRICING");
		System.out.println("CASE :  UPDATE PRICING");
		// BaseInitBOM.quoteOpen();
		update();
	}

	public void update() throws Exception {
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(80));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='btnUpdatePricing']")));
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("MATERIAL_UPDATE_PRICING"));
		log.info("~>> Clicked on update pricing");
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("MATERIAL_PRICING_SUBMIT"));
		log.info("~>> Submitted Pricing API's");
		progress();
		log.info("~>> RESULT :: UPDATE PRICING DONE");
		System.out.println("~>> RESULT :: UPDATE PRICING DONE");
		log.info("================================================================================");
	}

	public void progress() throws InterruptedException {
		waitforseconds(5);
		boolean progress_bar = false;
		try {
			progress_bar = driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']")).isDisplayed();
		} catch (Exception e) {

		}
		if (progress_bar) {
			start = System.currentTimeMillis();
			System.out.println(">>> Time started :: " + String.valueOf(start));
			log.info("~>> Progress Bar started");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(400));
			// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='btnUpdatePricing']]")));
			wait.until(ExpectedConditions
					.invisibilityOf(driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']"))));
			log.info("~>> Progress Bar finished");
			finish = System.currentTimeMillis();
			System.out.println(">>> Time ended :: " + String.valueOf(finish));
			double totalTime = finish - start;
			double minutetime = (totalTime / 1000) / 60;
			System.out.println(">>> Total Time to upload file ::  " + new DecimalFormat("#.####").format(minutetime)
					+ " minute(s)");
			log.info("~>> Total Time to upload file  ::  " + new DecimalFormat("#.####").format(minutetime)
					+ " minute(s)");
			waitforseconds(8);
			alertacceptpopup();
			waitforseconds(5);
		}
	}
}