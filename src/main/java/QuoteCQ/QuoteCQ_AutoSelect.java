package QuoteCQ;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_AutoSelect extends BaseInitquoteCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ -  PRICING SELECTION AND SUBMIT MATERIAL COSTING")
	public void autoSelectmain() throws Exception {
		headerFormat("CASE :  PRICING SELECTION (AUTO SELECT)");
		autoupdate();
		submit();
	}

	public void autoupdate() throws Exception {
		waitforseconds(5);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='btnAutoSelect']")));
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MATERIAL_AUTO_SELECT"));
		log.info("~>> Clicked on Auto Select");
		alertacceptpopup();
		waitforseconds(8);
		// safeJavaScriptClick(CheckXpath("MATERIAL_AUTOSELECT_ADVANCE"));
		WebElement Advance = CheckXpath("MATERIAL_AUTOSELECT_ADVANCE");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", Advance);
		log.info("~>> Clicked on Advanced options");
		waitforseconds(30);
		driver.findElement(By.xpath(".//button[@title='Remove Filter']")).click();
		waitforseconds(5);
		driver.findElement(By.xpath(".//button[@title='Add Filters' and @ng-click='addPreference()']")).click();
		waitforseconds(5);
		new Select(driver.findElement(By.xpath(".//*[@id='preferenceOrder0']")))
				.selectByVisibleText("Lower Total Price");
		log.info("~>> Clicked on Lower Total Price");
		// safeJavaScriptClick(CheckXpath("MATERIAL_PRICE_SENSITIVE"));
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MATERIAL_RUN_AUTOSELECT"));
		log.info("~>> Clicked on Run Autoselect");
		waitforseconds(6);
		log.info("~>> RESULT :: AUTO UPDATE DONE");
		System.out.println("~>> RESULT :: AUTO UPDATE DONE");
		log.info("================================================================================");
		waitforseconds(6);
	}

	public void submit() throws Exception {
		log.info("CASE :  SUBMIT MATERIAL COSTING");
		driver.navigate().refresh();
		waitforseconds(8);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(80));
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath(".//*[@id='btnPurchaseSubmit'and @aria-disabled='false']")));
		safeJavaScriptClick(CheckXpath("MATERIAL_SUBMIT"));
		log.info("~>> Clicked on Submit button");
		log.info("~>> RESULT  :: MATERIAL COSTING SUBMITTED");
		System.out.println("~>> RESULT :: MATERIAL COSTING SUBMITTED");
		alertacceptpopup();
		waitforseconds(8);
		log.info("================================================================================");
	}
}