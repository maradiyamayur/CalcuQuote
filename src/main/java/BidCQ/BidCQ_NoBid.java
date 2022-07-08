package BidCQ;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.BaseInitquoteCQ;
import QuoteCQ.QuoteCQ_ImportBOM;
import utility.UtilityMethods;

public class BidCQ_NoBid extends BaseInitbidCQ {

	BidCQ_BidGenerate BG = new BidCQ_BidGenerate();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. NO BID FOR SINGLE LINE ITEM ")
	public void noBidSingle() throws Exception {
		headerFormat("#. NO BID FOR SINGLE LINE ITEM");
		windowHandle();
		driver.get("https://" + getHost()[0] + getHost()[1] + "/#/DashBoard");
		waitforseconds(5);
		driver.get("https://" + getHost()[0] + getHost()[1] + "/#/DashBoard");
		waitforseconds(5);
		checkBid();
		noBid(1);
		noBidVerification(1);
	}

	@Test(priority = 2, description = "#. NO BID FOR SINGLE LINE ITEM ")
	public void noBidMulti() throws Exception {
		log.info("** NO BID FOR MULTI LINE ITEM");
		System.out.println("** NO BID FOR MULTI LINE ITEM"); // BG.importMain();
		noBid(2);
		noBidVerification(2);
	}

	@Test(priority = 3, description = "#. NO BID FOR WHOLE BOM")
	public void noBidAll() throws Exception {
		log.info("** NO BID FOR WHOLE BOM");
		System.out.println("** NO BID FOR WHOLE BOM");
		noBid(3);
		noBidVerification(3);
	}

	public void noBid(int type) throws Exception {
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		// driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		waitforseconds(15);
		if (type == 1) {
			safeJavaScriptClick(CheckXpath("SELECT_LINE_1"));
		} else if (type == 2) {
			for (int i = 2; i < 4; i++) {
				WebElement eleLineItem = driver
						.findElement(By.xpath("(.//div[contains(@ng-click,'selectButtonClick')])[" + i + "]"));
				safeJavaScriptClick(eleLineItem);
			}
		} else if (type == 3) {
			WebElement eleAllLineItem = driver
					.findElement(By.xpath(".//div[contains(@ng-class,'grid.selection.selectAll')]"));
			safeJavaScriptClick(eleAllLineItem);
		}
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MC_EDIT_BTN"));
		log.info("~>> Edit button clicked");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("NO_BID_LINEITEM"));
		log.info("~>> No Bid Line Item Clicked");
		waitforseconds(5);
		CheckXpath("NO_BID_REASON_NOTES").sendKeys("No Bid Line Items");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("NO_BID_BUTTON"));
		waitforseconds(10);
	}

	public void noBidVerification(int type) {
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
		waitforseconds(5);
		List<WebElement> lstNoBid = driver.findElements(By.xpath(".//div[@row='grid.appScope.purchases']//a"));

		if (type == 1) {
			WebElement eleNoBid = driver.findElement(By.xpath("(.//div[@row='grid.appScope.purchases'])[1]//a"));
			if (eleNoBid.getText().equals("No Bid")) {
				System.out.println("~>> RESULT :: Succesfully No Bid Status Updated");
				Assert.assertTrue(true);
			} else {
				System.out.println("~>>RESULT :: No Bid Status not updated");
				Assert.assertTrue(false);
			}
		} else if (type == 2) {
			for (int i = 1; i <= 3; i++) {
				WebElement eleNoBid = driver
						.findElement(By.xpath("(.//div[@row='grid.appScope.purchases'])[" + i + "]//a"));
				if (eleNoBid.getText().equals("No Bid")) {
					System.out.println("~>> RESULT :: Succesfully No Bid Status Updated");
					Assert.assertTrue(true);
				} else {
					System.out.println("~>>RESULT :: No Bid not Status Updated");
					Assert.assertTrue(false);
				}
			}
		} else if (type == 3) {
			for (int i = 1; i <= lstNoBid.size(); i++) {
				WebElement eleNoBid = driver
						.findElement(By.xpath("(.//div[@row='grid.appScope.purchases'])[" + i + "]//a"));
				if (eleNoBid.getText().equals("No Bid")) {
					System.out.println("~>> RESULT :: Succesfully No Bid Status Updated");
					Assert.assertTrue(true);
				} else {
					System.out.println("~>>RESULT :: No Bid Status not Updated");
					Assert.assertTrue(false);
				}
			}
		}
		System.out.println(
				".............................................................................................................................................");
	}

	public void checkBid() throws Exception {
		BaseInitquoteCQ.RFQ_list();
		QuoteCQ_ImportBOM IB = new QuoteCQ_ImportBOM();
		IB.openQuote();
		BaseInitquoteCQ.first_RFQ();
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		waitforseconds(10);
		boolean lineitems = false;
		try {
			lineitems = driver.findElement(By.xpath(".//div[@class='ui-grid-row ng-scope']")).isDisplayed();
		} catch (Exception e) {

		}
		if (lineitems) {
			System.out.println("~>> Line Items are already available ");
		} else {
			BG.importMain();
		}
	}
}