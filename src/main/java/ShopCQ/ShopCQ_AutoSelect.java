package ShopCQ;

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

public class ShopCQ_AutoSelect extends BaseInitshopCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - AUTO SELECT PRICING")
	public void autoSelect() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : AUTO SELECT PRICING");
		System.out.println("CASE : AUTO SELECT PRICING");
		// demandList();
		autoRun();
	}

	public void autoRun() throws Exception {
		waitforseconds(10);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft -= 3000", scrollbar);
		CheckXpath("DL_STATUS_FILTER").clear();
		CheckXpath("DL_STATUS_FILTER").sendKeys("Market Researched");
		log.info("~>> Status :: Market Researched");
		CheckXpath("DL_TAGS_FILTER").clear();
		CheckXpath("DL_TAGS_FILTER").sendKeys("shreyas");
		waitforseconds(15);
		Boolean records = false;
		try {
			records = driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).isDisplayed();
		} catch (Exception e) {

		}
		if (records) {
			waitforseconds(5);
			System.out.println("~>> Record displays");
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).click();
			log.info("~>> Records selcted for Auto Select");
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("DL_EDIT_BTN"));
			log.info("~>> Edit button clicked");
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("DL_EDIT_AUTOSELECT"));
			waitforseconds(8);
			driver.findElement(By.xpath(".//a[@title='Remove']")).click();
			waitforseconds(8);
			driver.findElement(By.xpath(".//a[@title='Add' and @ng-click='AddPreference()']")).click();
			waitforseconds(5);
			new Select(driver.findElement(By.xpath(".//*[@id='preferenceOrd']")))
					.selectByVisibleText("Lower Total Price");
			log.info("~>> Clicked on Lower Total Price");
			waitforseconds(10);
			safeJavaScriptClick(CheckXpath("AUTOSELECT_SUBMIT_BTN"));
			log.info("~>> Clicked on Run Autoselect button");
			pricingVerification();
		} else {
			log.info("~>> No Records for Auto Select");
			System.out.println("~>> No Records for Auto Select");
		}
	}

	public void pricingVerification() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(80));
		wait.until(ExpectedConditions.invisibilityOf(CheckXpath("AUTOSELECT_SUBMIT_BTN")));
		System.out.println("~>> RESULT  :: AUTO SELECT PRICING COMPLETED");
		log.info("~>> RESULT  :: AUTO SELECT PRICING COMPLETED");
	}
}
