package SearchCQ;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class SearchCQ_VerifyResult extends BaseInitsearchCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SearchCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. VERIFY SEARCH RESULTS")
	public void SearchData() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : VERIFY SEARCH RESULTS");
		System.out.println("CASE : VERIFY SEARCH RESULTS");
		verifyfilter("Pricing & Availability", "isCalcuQuoteCustomer");
		verifyfilter("Quote Reference", "IsCalcuQuoteCustomerQuoteRefrence");
		verifyQuoteRef();
	}

	public void verifyfilter(String type, String element) {
		log.info("~>> Verifying filter visibility for " + type);
		System.out.println("~>> Verifying filter visibility " + type);
		boolean filter = false;
		try {
			filter = driver.findElement(By.xpath("(.//div[@class='ng-scope' and @ng-if='" + element
					+ "']//following::div[@class='ng-scope']//input)[1]")).isDisplayed();
			if (filter) {
				Assert.assertTrue(true);
				System.out.println("~>> RESULT :: PRICING COMPLETED SUCCESSFULLY");
				log.info("~>> RESULT :: PRICING COMPLETED SUCCESSFULLY");
			} else {
				Assert.assertTrue(false);
				System.out.println("~>> RESULT :: PRICING NOT COMPLETED SUCCESSFULLY");
				log.info("~>> RESULT :: PRICING  NOT COMPLETED SUCCESSFULLY");
			}
		} catch (Exception e) {

		}
		System.out.println("----------------------------------------------------------------------------");
	}

	public void verifyQuoteRef() throws Exception {
		log.info("~>> Verifying Quote Reference");
		System.out.println("~>> Verifying Quote Reference");
		boolean idpath = false;
		try {
			idpath = CheckXpath("SEARCH_ASM_ID_LABEL").isDisplayed();
		} catch (Exception e) {

		}
		waitforseconds(8);
		if (idpath) {
			String id = CheckXpath("SEARCH_ASM_ID_LABEL").getAttribute("id").replaceAll("-header-text", "");
			String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
			WebElement lstresult = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div)[1]"));
			String asmID = lstresult.getText();
			System.out.println("~>> Expected Assembly ID  : " + asmID);
			waitforseconds(3);
			safeJavaScriptClick(lstresult.findElement(By.xpath(".//a")));

			Set<String> handles = driver.getWindowHandles();
			List<String> handlesList = new ArrayList<String>(handles);
			waitforseconds(10);
			driver.switchTo().window(handlesList.get(1));
			driver.close();
			driver.switchTo().window(handlesList.get(0));
			waitforseconds(5);
			safeJavaScriptClick(lstresult.findElement(By.xpath(".//a")));
			waitforseconds(25);

			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().contains("Material")) {
					log.info("Material tab");
				}
			}

			//waitforseconds(20);
			
			  WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(100));
			  wait.until(ExpectedConditions.visibilityOf(CheckXpath("MC_ASM_ID_LABEL")));
			 
			boolean bolAsmID = false;
			try {
				bolAsmID = CheckXpath("MC_ASM_ID").isDisplayed();
			} catch (Exception e) {

			}
			if (bolAsmID) {
				String strAsmID = CheckXpath("MC_ASM_ID").getAttribute("innerText");
				System.out.println("~>> Opened Assembly ID  : " + strAsmID);
				if (strAsmID.equals(asmID)) {
					Assert.assertTrue(true);
					System.out.println("~>> RESULT ::  CORRECT ASSEMBLY ID OPENED");
					log.info("~>> RESULT ::  CORRECT ASSEMBLY ID OPENED");
				} else {
					Assert.assertTrue(false);
					System.out.println("~>> RESULT ::  WRONG ASSEMBLY ID OPENED");
					log.info("~>> RESULT ::  WRONG ASSEMBLY ID OPENED");
				}
			} else {
				log.info("~>> Assembly ID not displayed");
			}
		}
	}
}