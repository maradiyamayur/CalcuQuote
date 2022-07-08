package ShopCQ;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.UtilityMethods;

public class ShopCQ_UpdatePricing extends BaseInitshopCQ {

	public double start, finish;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - UPDATE PRICING")
	public void updatePricing() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : UPDATE PRICING");
		System.out.println("CASE : UPDATE PRICING");
		//demandList();
		driver.navigate().back();
		waitforseconds(15);
		getPricing();
	}

	public void getPricing() throws Exception {
		waitforseconds(8);
		CheckXpath("DL_STATUS_FILTER").clear();
		CheckXpath("DL_STATUS_FILTER").sendKeys("Demand Imported");
		waitforseconds(8);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 1000", scrollbar);
		waitforseconds(5);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", CheckXpath("TOTAL_QTY_FILTER_MENU"));
		log.info("~>> Total Qty filter clicked ");
		waitforseconds(2);
		safeJavaScriptClick(CheckXpath("SORT_TOTAL_QTY_ASC"));
		log.info("~>> Sorting on Ascending order");
		Thread.sleep(8000);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 3000", scrollbar);
		waitforseconds(5);
		boolean getpricing = false;
		try {
			getpricing = driver.findElement(By.xpath("(.//span[@title='Get Pricing'])[1]")).isDisplayed();
		} catch (Exception e) {

		}

		if (getpricing) {
			log.info("~>> Demandline available to get pricing");
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("GET_PRICING"));
			waitforseconds(3);
			verification();
			progress();
		} else {
			log.info("~>> Demandline not available to get pricing");
		}

	}

	public void verification() {
		boolean progress_bar = false;
		try {
			progress_bar = driver.findElement(By.xpath("(.//span[@title='Pricing Running'])[1]")).isDisplayed();
		} catch (Exception e) {

		}
		if (progress_bar) {
			log.info("~>> RESULT :: UPDATE PRICING STARTED RUNNING");
			System.out.println("~>> RESULT ::  UPDATE PRICING STARTED RUNNING");
		} else {
			log.info("~>> RESULT ::  UPDATE PRICING NOT STARTED RUNNING");
			System.out.println("~>> RESULT ::   UPDATE PRICING NOT STARTED RUNNING");
		}
	}

	public void progress() throws InterruptedException {
		waitforseconds(5);
		boolean progress_bar = false;
		try {
			progress_bar = driver.findElement(By.xpath("(.//span[@title='Pricing Running'])[1]")).isDisplayed();
		} catch (Exception e) {

		}
		if (progress_bar) {
			log.info("~>> Pricing Started Running");
			try
			{
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(120));
			wait.until(ExpectedConditions
					.invisibilityOf(driver.findElement(By.xpath("(.//span[@title='Pricing Running'])[1]"))));

			System.out.println("~>> Pricing Stopped Running");
			}
			catch(TimeoutException e)
			{
			log.info(e.getMessage());
			System.out.println("~>> Pricing not completed");
			}
			waitforseconds(8);
			alertacceptpopup();
			waitforseconds(5);
		}
	}

}
