package SearchCQ;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class SearchCQ_SearchMPN extends BaseInitsearchCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SearchCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. SEARCH WITH MPN")
	public void SearchData() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : SEARCH WITH MPN");
		System.out.println("CASE : SEARCH WITH MPN");
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("(.//*[@id='pricing']//div[@class='panel-heading box-header'])[2]")));
		safeJavaScriptClick(CheckXpath("SEARCH_MPN"));
		WebElement MPNTextbox = driver.findElement(By.xpath(".//input[@id='txtMPN']"));
		Actions ElementFocus = new Actions(driver);
		ElementFocus.moveToElement(MPNTextbox).sendKeys("5014").build().perform();
		log.info("~>> MPN Added");
		driver.findElement(By.xpath("//body")).click();
		alertacceptpopup();
		long start = System.currentTimeMillis();
		waitforseconds(30);
		boolean IsLabelVisible = false;
		try {
			IsLabelVisible = CheckXpath("SEARCH_PERCENTAGE").isDisplayed();
			while (IsLabelVisible) {
				String Percentagereplace = CheckXpath("SEARCH_PERCENTAGE").getText().replace(" %", "");
				Double percentage = Double.parseDouble(Percentagereplace);
				if (percentage >= 0) {
					Assert.assertTrue(true);
					System.out.println("~>> RESULT :: PRICING IS RUNNING SUCCESSFULLY");
					log.info("~>> RESULT ::  PRICING IS RUNNING SUCCESSFULLY");
					break;
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: PRICING IS NOT RUNNING SUCCESFULLY");
					System.out.println("~>> RESULT :: PRICING IS NOT RUNNING SUCCESFULLY");
				}
			}
		} catch (Exception ex) {
			ex.getMessage();
		}
		try
		{
		WebDriverWait waitResult = new WebDriverWait(driver,Duration.ofSeconds(150));
		waitResult.until(ExpectedConditions.invisibilityOf(CheckXpath("SEARCH_PERCENTAGE")));
		}
		catch(TimeoutException e)
		{
		log.info(e.getMessage());
		}
		long finish = System.currentTimeMillis();
		long totalTime = finish - start;
		long secondtime = (totalTime / 1000) % 60;
		System.out.println(">>> Time to Complete Search  ::  " + secondtime + " Second(s)");
	}
}
