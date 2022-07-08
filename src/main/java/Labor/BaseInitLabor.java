package Labor;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitLabor extends BaseInit {

	SoftAssert softAssertion = new SoftAssert();
	public static String Add_QuoteID, Add_Asm_name, Add_Asm_no, Add_job, Add_order, Add_Customer, asm_Revision;

	@BeforeSuite
	public void checkExecutionModeTestSuite() throws IOException {
		startup();
		// Code to check execution mode of Test Suite
		if (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "Labor", "TestSuite")) {
			throw new SkipException("Execution mode of the test suite is set to NO");
		}
	}

	public void alertacceptpopup() throws InterruptedException {
		boolean popup = false;
		try {
			popup = driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).isDisplayed();
		} catch (Exception e) {

		}
		if (popup) {
			driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
			log.info("~>> Alert accepted");
		} else {
			log.info("~>> No alert");
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			log.info("~>>  Alert present");
			return true;
		} catch (NoAlertPresentException Ex) {
			log.info("~>>  Alert absent");
			return false;
		}
	}

	public void submit(int type) throws Exception {
		Thread.sleep(5000);
		if (type == 1) {
			safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
			log.info("~>> Clicked on BOM Submit");
			Thread.sleep(8000);
			if (driver.findElement(By.xpath(".//*[@id='tabbom']/a")).getAttribute("class")
					.contains("ActivityStatusComplete")) {
				log.info("VALIDTION 1 ::  BOM Submitted");
				softAssertion.assertTrue(true);
			} else {
				log.info("VALIDTION 1 ::  BOM not Submitted");
				softAssertion.assertTrue(false);
			}
		}
		if (type == 2) {
			Thread.sleep(8000);
			safeJavaScriptClick(CheckXpath("LABOR_TAB"));
			log.info("~>> Labor Tab Clicked");
			Thread.sleep(10000);
			safeJavaScriptClick(CheckXpath("LABOR_SUBMIT"));
			log.info("~>> Clicked on Labor  Submit");
			Thread.sleep(5000);
			// alertacceptpopup();
			Thread.sleep(8000);
			if (driver.findElement(By.xpath(".//*[@id='tablabouractivityprofile']/a")).getAttribute("class")
					.contains("ActivityStatusComplete")) {
				log.info("VALIDTION 2 :: Labor Submitted");
				softAssertion.assertTrue(true);
			} else {
				log.info("VALIDTION 2 :: Labor not Submitted");
				softAssertion.assertTrue(false);
			}
		}
		Thread.sleep(10000);
	}

	public void installcheck() throws Exception {
		Thread.sleep(5000);
		List<WebElement> lstinstall = driver
				.findElements(By.xpath(".//input[@ng-change='grid.appScope.ResetAllFlag('Install', row.entity)]"));
		for (WebElement inst : lstinstall) {
			if (inst.getAttribute("aria-disabled").equals("false")) {
				inst.click();
				Thread.sleep(4000);
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
			}
		}

	}

}
