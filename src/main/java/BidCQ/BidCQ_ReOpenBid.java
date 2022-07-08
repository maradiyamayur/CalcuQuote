package BidCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BidCQ_ReOpenBid extends BaseInitbidCQ {
	BidCQ_BidGenerate BG = new BidCQ_BidGenerate();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. Generate Bid and Submit it.")
	public void GeneRateBid() throws Exception {
		headerFormat("#. CASE : GENERATE BID AND SUBMIT IT ");
		windowHandle();
		BG.generate();
	}

	@Test(priority = 2, description = "#. Verify Submitted Bid Data in CQPS")
	public void verifyCQPSSubmit() throws Exception {
		headerFormat("#. CASE : VERIFYING SUBMITTED BID DATA FROM CQPS");
		CQPS();
		verifyCQPS("MPN", "Panasonic | 5020");
		verifyCQPS("Unit $", "100.000000");
		safeJavaScriptClick(CheckXpath("CQPS_CLOSE_BUTTON"));
	}

	@Test(priority = 3, description = "#. REOPEN Submitted Bid")
	public void bidReOpen() throws Exception {
		headerFormat("#. CASE : REOPENING SUBMITTED BID");
		safeJavaScriptClick(CheckXpath("MC_BID_OPTIONS"));
		safeJavaScriptClick(CheckXpath("REOPEN_BID"));
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("REOPEN_BID_CONFIRMATION"));
		waitforseconds(10);
		BG.getstatus("Send", "rgb(26, 113, 210)", "BLUE", "RE-OPEN");
	}

	@Test(priority = 4, description = "#. Update Data for Reopen Bid")
	public void UpdateBid() throws Exception {
		headerFormat("#. CASE :  UPDATE DATA FOR REOPEN BID");
		switchToNewtabWithUrl(driver, 1);
		waitforseconds(5);
		driver.navigate().refresh();
		waitforseconds(10);
		BidCQ_Filter.existFilter();
		BG.verifyStatus(0);
		openBid(2);
		BG.bidsheet(200, 5014, "Mouser");
		switchToNewtabWithUrl(driver, 0);
		waitforseconds(5);
		driver.navigate().refresh();
		waitforseconds(5);
		BG.getstatus("Send", "rgb(0, 150, 136)", "GREEN", "SUBMITTED");
		waitforseconds(5);
		CQPS();
		verifyCQPS("Original MPN", "Mouser | 5014");
		verifyCQPS("Unit $", "200.000000");
		safeJavaScriptClick(CheckXpath("CQPS_CLOSE_BUTTON"));
	}

	public void CQPS() throws Exception {
		log.info("~>> Opening CQPS");
		waitforseconds(10);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
		waitforseconds(8);
		driver.findElement(By.xpath("(.//a[contains(@uib-tooltip,'Pricing Available')]//i)[1]")).click();
		waitforseconds(15);
		safeJavaScriptClick(CheckXpath("CQPS_TOGGLE"));
		waitforseconds(8);
		boolean isChecked = false;
		try {
			isChecked = driver.findElement(By.xpath(
					".//ul[@class='custdropdown-menu']//li//a[contains(text(),'Unpreferred Suppliers')]//span[@class='glyphicon glyphicon-ok']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (isChecked) {
			System.out.println("~>> Unpreferred Suppliers already clicked");
		} else {
			safeJavaScriptClick(CheckXpath("CQPS_UNPREFERRED_SUPPLIER"));
			System.out.println("~>> Unpreferred Suppliers clicked");
		}
		waitforseconds(20);
	}

	public void verifyCQPS(String Label, String Result) throws Exception {
		log.info("~>> Verifying " + Label + " in CQPS for submitted Bid");
		String id = driver
				.findElement(By.xpath(
						" (.//span[@class='ui-grid-header-cell-label ng-binding' and text()='" + Label + "'])[1]"))
				.getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		WebElement actResult = driver.findElement(
				By.xpath(".//div[contains(@id,'" + id1 + "')]//div[contains(@class,'ui-grid-cell-contents')]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", actResult);
		waitforseconds(5);
		if (actResult.getText().equals(Result)) {
			System.out.println("~>> RESULT :: Correct Data updated in CQPS for " + Label);
			log.info("~>> RESULT :: Correct Data updated in CQPS for " + Label);
			Assert.assertTrue(true);
		} else {
			System.out.println("~>> RESULT :: Wrong Data updated in CQPS for " + Label);
			log.info("~>> RESULT :: Wrong Data updated in CQPS for " + Label);
			Assert.assertTrue(false);
		}
		waitforseconds(5);
	}
}