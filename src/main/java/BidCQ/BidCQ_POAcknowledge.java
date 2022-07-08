package BidCQ;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import ShopCQ.BaseInitshopCQ;
import ShopCQ.ShopCQ_AddDemandline;
import ShopCQ.ShopCQ_CreatePO;
import ShopCQ.ShopCQ_POSent;
import utility.UtilityMethods;

public class BidCQ_POAcknowledge extends BaseInitbidCQ {

	ShopCQ_CreatePO CP = new ShopCQ_CreatePO();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: BCQ - PURCHASE ORDER ACKNOWLEDGE")
	public void poAcknowledge() throws Exception {
		headerFormat("#. CASE : PURCHASE ORDER ACKNOWLEDGE");
		safeJavaScriptClick(CheckXpath("SHOP_CQ"));
		waitforseconds(5);
		demandline();
		demandline();
		purchase();
		verification();
	}

	public void demandline() throws Exception {
		ShopCQ_AddDemandline AD = new ShopCQ_AddDemandline();
		if (BidCQ_BidGenerate.supplierName != null) {
			String demandList = driver.getCurrentUrl().replace("shopcqhome", "demandlist");
			driver.get(demandList);
			waitforseconds(10);
			AD.addDetails("15", BidCQ_BidGenerate.supplierName);
		}
	}

	public void purchase() throws Exception {
		CP.demandList();
		BaseInitshopCQ.removeFilter();
		CP.checkRecord(1, BidCQ_BidGenerate.supplierName, 1);
		// CP.createPO();
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("DL_LEFT_MENU"));
		log.info("~>> Clicked on Left Menu");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("DL_PO_LIST"));
		log.info("~>> Clicked on PO List");
		BaseInitshopCQ.removeFilter();
		poEdit("SENDTO_BID_CQ");
	}

	public void poEdit(String Export) throws Exception {
		waitforseconds(10);
		if (ShopCQ_CreatePO.PurchaseID.contains("New PO")) {
			CheckXpath("PO_STATUS_FILTER").clear();
			CheckXpath("PO_STATUS_FILTER").sendKeys("create");
			waitforseconds(5);
			CheckXpath("PO_SUPPLIER_FILTER").clear();
			// CheckXpath("PO_SUPPLIER_FILTER").sendKeys("");
			CheckXpath("PO_SUPPLIER_FILTER").sendKeys(BidCQ_BidGenerate.supplierName);
			waitforseconds(5);
			String id = CheckXpath("PO_NUMBER_LABEL").getAttribute("id").replaceAll("-header-text", "");
			String id1 = (id.split("-")[1]).concat("-").concat(id.split("-")[2]);
			String PO = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div//span)[1]")).getText();
			ShopCQ_CreatePO.PurchaseID = PO;
		} else {
			CheckXpath("PO_NUMBER_FILTER").clear();
			CheckXpath("PO_NUMBER_FILTER").sendKeys(ShopCQ_CreatePO.PurchaseID);
			log.info("~>> Searching done for Purchase Order Number :: " + ShopCQ_CreatePO.PurchaseID);
		}
		waitforseconds(5);
		boolean poEdit = false;
		try {
			poEdit = CheckXpath("PO_EDIT").isDisplayed();
		} catch (Exception e) {

		}
		if (poEdit) {
			log.info("~>> Searched PO found :: " + ShopCQ_CreatePO.PurchaseID);
			safeJavaScriptClick(CheckXpath("PO_EDIT"));
			waitforseconds(5);
			boolean pobtn = false;
			try {
				pobtn = CheckXpath("SEND_PO_BTN").isDisplayed();
			} catch (Exception e) {

			}
			if (pobtn) {
				waitforseconds(8);
				safeJavaScriptClick(CheckXpath("SEND_PO_BTN"));
				log.info("~>> Send Purchase Order Button Clicked");
				ShopCQ_POSent PS = new ShopCQ_POSent();
				PS.updatePO(Export);

			} else {
				log.info("~>> Send Purchase Order button not displayed");
			}
		} else {
			log.info("~>> Searched PO does not exits");
			System.out.println("~>> Searched PO does not exists");
		}
	}

	public void verification() throws Exception {
		waitforseconds(10);
		view();
		bidverification();
	}

	public void view() throws InterruptedException {
		SoftAssert softAssertion = new SoftAssert();
		waitforseconds(8);
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
				softAssertion.assertTrue(true);
				log.info("~>>  PO SEND HISTORY DISPLAYED");
				System.out.println("~>>  PO SEND HISTORY DISPLAYED");
				waitforseconds(5);
				if (getid("BID_PO_METHOD_LABEL").equalsIgnoreCase("BidCQ")) {
					log.info("~>> RESULT-1 :: Method is correct");
					System.out.println("~>> RESULT-1 :: Method is correct");
				} else {
					log.info("~>> RESULT-1 :: Method is wrong");
					System.out.println("~>> RESULT-1 :: Method is wrong");
				}
				if (getid("BID_PO_NUMBER_LABEL").contains(ShopCQ_CreatePO.PurchaseID)) {
					log.info("~>> RESULT-2 :: PO Number is correct");
					System.out.println("~>> RESULT-2 :: PO Number is correct");
				} else {
					log.info("~>> RESULT-2 :: PO Number is wrong");
					System.out.println("~>> RESULT-2 :: PO Number is wrong");
				}
				if (getid("BID_PO_RESPONSE_LABEL").equalsIgnoreCase("Success")) {
					log.info("~>> RESULT-3 :: Response is correct");
					System.out.println("~>> RESULT-3 :: Response is correct");
				} else {
					log.info("~>> RESULT-3 :: Response is wrong");
					System.out.println("~>> RESULT-3 :: Response is wrong");
				}
				waitforseconds(5);
				CheckXpath("PO_HISTORY_CLOSE").click();
			} else {
				softAssertion.assertTrue(false);
				log.info("~>> PO SEND HISTORY NOT DISPLAYED");
				System.out.println("~>> PO SEND HISTORY NOT DISPLAYED");
			}
		} else {
			log.info("~>> PO History button not displayed");
		}

	}

	public void bidverification() throws Exception {
		log.info("~>> Verifying Bid from PO list ");
		switchToNewtabWithUrl(driver, 1);
		waitforseconds(5);
		driver.navigate().refresh();
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("BID_MENU"));
		log.info("~>> Clicked on BidSheet Menu");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BID_PO_SUMMARY_LIST"));
		log.info("~>> Clicked on PO Summary list");
		waitforseconds(5);
		CheckXpath("BID_PO_NUMBER_FILTER").clear();
		CheckXpath("BID_PO_NUMBER_FILTER").sendKeys(ShopCQ_CreatePO.PurchaseID);
		log.info("~>> Added Purchase ID");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_PO_NEW"));
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_PO_APPROVE"));
		log.info("~>> PO Bid Approved");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BID_PO_REJECT"));
		log.info("~>> PO Bid Rejected");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BID_PO_SUBMIT"));
		log.info("~>> PO Bid Submitted");
		waitforseconds(5);
		switchToNewtabWithUrl(driver, 0);
		safeJavaScriptClick(CheckXpath("PURCHASE_ORDER_BACK"));
		log.info("~>> Back to Purchase Order List");
		waitforseconds(8);
		BaseInitshopCQ.removeFilter();
		CheckXpath("PO_NUMBER_FILTER").clear();
		CheckXpath("PO_NUMBER_FILTER").sendKeys(ShopCQ_CreatePO.PurchaseID);
		waitforseconds(3);
		String id = CheckXpath("PO_STATUS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-0-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		waitforseconds(5);

		String Strstatus = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div")).getText();
		if (Strstatus.contains("Order Exception")) {
			log.info("~>> RESULT-1 :: PURCHASE ORDER ACKNOWLEDGED ");
			System.out.println("~>> RESULT-1 :: PURCHASE ORDER ACKNOWLEDGED");
			waitforseconds(3);
			driver.findElement(By.xpath(".//i[@class='ng-scope ui-grid-icon-plus-squared']")).click();
			if (CheckXpath("BID_EXPORT_LINE").isDisplayed()) {
				log.info("~>> RESULT-2 ::  Export for approved line displayed");
				System.out.println("~>> RESULT-2 ::  Export for approved line displayed");
			} else {
				log.info("~>> RESULT-2 :: Export for approved line not displayed");
				System.out.println("~>> RESULT-2 :: Export for approved line not displayed");
			}
			if (CheckXpath("BID_EXPORT_REJECTED_LINE").isDisplayed()) {
				log.info("~>> RESULT-3 ::  Export for Rejected line dispalyed");
				System.out.println("~>> RESULT-3 ::  Export for Rejected line dispalyed");
			} else {
				log.info("~>> RESULT-2 :: Export for Rejected line not displayed");
				System.out.println("~>> RESULT-2 :: Export for Rejected line not displayed");
			}
		} else {
			log.info("~>> RESULT :: PURCHASE ORDER NOT ACKNOWLEDGED ");
			System.out.println("~>> RESULT :: PURCHASE ORDER NOT ACKNOWLEDGED ");
		}
	}

	public String getid(String label) {
		String id = CheckXpath(label).getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		waitforseconds(5);
		String actualresult = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div)[last()]"))
				.getText();
		return actualresult;
	}
}
