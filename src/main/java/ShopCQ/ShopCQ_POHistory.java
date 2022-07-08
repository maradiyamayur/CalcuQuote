package ShopCQ;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ShopCQ_POHistory extends BaseInitshopCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - PURCHASE ORDER HISTORY")
	public void poHistory() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		waitforseconds(5);
		log.info("CASE : PURCHASE ORDER HISTORY");
		System.out.println("CASE : PURCHASE ORDER HISTORY");
		view();
	}

	public void view() throws Exception {
		waitforseconds(8);
		boolean view = false;
		try {
			view = CheckXpath("PO_HISTORY_VIEW").isDisplayed();
		} catch (Exception e) {

		}
		if (view) {
			log.info("PO History view Displayed");
			safeJavaScriptClick(CheckXpath("PO_HISTORY_VIEW"));
			log.info("~>> PO History view button clicked");
			SendPOView();
			boolean button = false;
			try {
				button = CheckXpath("PO_SEND_HISTORY").isDisplayed();
			} catch (Exception e) {

			}
			if (button) {
				log.info("~>> PO History button displayed");
				CheckXpath("PO_SEND_HISTORY").click();
				waitforseconds(8);
				if (CheckXpath("PO_SEND_WINDOW").isDisplayed()) {
					Assert.assertTrue(true);
					log.info("~>> RESULT-2 :: PO SEND HISTORY DISPLAYED");
					System.out.println("~>> RESULT-2 :: PO SEND HISTORY DISPLAYED");
					waitforseconds(10);
					CheckXpath("PO_HISTORY_CLOSE").click();
					waitforseconds(15);
					driver.findElement(By.xpath(".//span[@class='pull-left']")).click();
					waitforseconds(5);
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT-2 :: PO SEND HISTORY NOT DISPLAYED");
					System.out.println("~>> RESULT-2 :: PO SEND HISTORY NOT DISPLAYED");
				}
			} else {
				log.info("~>> PO History button not displayed");
			}
		} else {
			log.info("~>> PO HISTORY VIEW NOT DISPLAYED");
			System.out.println("~>> PO HISTORY VIEW NOT DISPLAYED");
		}
	}

	public void SendPOView() throws InterruptedException {
		waitforseconds(8);
		Boolean SendPO = false;
		try {
			SendPO = CheckXpath("SEND_PO_BTN").isDisplayed();
		} catch (Exception e) {

		}
		if (SendPO) {
			log.info("~>> SEND PO  DISPLAYED");
			if (CheckXpath("SEND_PO_BTN").getAttribute("aria-disabled").equals("true")) {
				Assert.assertTrue(true);
				log.info("~>> RESULT-1 :: SEND PURCHASE ORDER BUTTON IS DISABLED");
				System.out.println("~>> RESULT-1 :: SEND PURCHASE ORDER BUTTON IS DISABLED");
			} else {
				Assert.assertTrue(false);
				log.info("~>> RESULT-1 :: SEND PURCHASE ORDER BUTTON IS ENABLED");
				System.out.println("~>> RESULT-1 :: SEND PURCHASE ORDER BUTTON IS ENABLED");
			}
			waitforseconds(5);
		} else {
			log.info("~>>SEND PO BUTTON IS NOT DISPLAYED");
		}
	}
}