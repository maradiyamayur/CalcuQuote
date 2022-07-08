package BOM;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.QuoteCQ_AutoSelect;
import QuoteCQ.QuoteCQ_UpdatePricing;
import utility.UtilityMethods;

public class BOM_Phase1 extends BaseInitBOM {
	private WebElement Pricefilter;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BOM")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = " BOM-Phase 1 - CONSOLIDATE BOM ITEMS")
	public void consolidatemain() throws Exception {
		log.info("==========================================================");
		log.info("3.  CONSOLIDATE BOM LINE ITEMS AND VERIFY IT ");
		BaseInitBOM.quoteOpen();
		MPNdetail();
		consolidate();
		multiplecaption();
	}

	@Test(priority = 2, description = "BOM-Phase 1 - UPDATE PRICING AND AUTO SELECT")
	public void pricing() throws Exception {
		log.info(
				"------------------------------------------------------------------------------------------------------------------------------");
		log.info("4. UPDATE PRICING");
		QuoteCQ_UpdatePricing QUP = new QuoteCQ_UpdatePricing();
		QUP.pricingmain();
		QuoteCQ_AutoSelect QAS = new QuoteCQ_AutoSelect();
		QAS.autoupdate();
	}

	@Test(priority = 3, description = "BOM-Phase 1 - EXCESS QTY AND PRICE CALCULATION VERIFICATION")
	public void excessQty() throws Exception {
		RFQ_list();
		first_RFQ();
		waitforseconds(8);
		BOM_Phase1 CB = new BOM_Phase1();
		CB.mcTab();
		scrolling();
		filter();
		calculation();
	}

	public void adddata() throws Exception {
		log.info("-----------------------------------------------------------------------------------------------");
		log.info("1. Add  same MPN, MFGR for two line items");
		WebElement BOMtab = CheckXpath("BOM_TAB");
		safeJavaScriptClick(BOMtab);
		log.info("~>> Redirected to BOM Tab");
		waitforseconds(8);
	}

	public void consolidate() throws Exception {
		log.info("2. Check BOM lineitem on MC tab is consolidate and should display line item with comma seperation");
		mcTab();
		boolean button = false;
		try {
			button = driver.findElement(By.xpath(".//*[@id='btnUpdatePricing']")).isDisplayed();
		} catch (Exception e) {

		}
		if (button) {
			consolidateverify();
		} else {
			mcTab();
			consolidateverify();
		}
	}

	public void mcTab() throws Exception {
		log.info("----------------------------------------------------------------------------------------------");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab");
		waitforseconds(8);
	}

	public void consolidateverify() throws Exception {
		String id = CheckXpath("BOM_LINE-ITEM-NO_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-0-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		WebElement desc = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		String Actual = desc.getAttribute("innerText");
		log.info("~>> Actual Consolidate BOM line item is :: " + Actual);
		String expected = "130,131";
		if (Actual.equals(expected)) {
			Assert.assertTrue(true);
			log.info("RESULT ::  Line Item Consolidated Succesfully");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT :: Line Item is not Consolidated");
		}
	}

	public void multiplecaption() throws Exception {
		waitforseconds(5);
		log.info(
				"-----------------------------------------------------------------------------------------------------------");
		log.info("3. Multiple captions should be there on Partclass, LeadQty, Attr Rate%");

		BOM_Properties.gridmenu();
		waitforseconds(5);
		log.info(
				"------------------------------------------------------------------------------------------------------------------------------");
		log.info("1. CHECK FOR MULTIPLE CAPTION FOR PARTCLASS");
		String Pcid = CheckXpath("MC_PARTCLASS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String Pcid1 = Pcid.split("-")[0].concat("-0-").concat(Pcid.split("-")[1]).concat("-")
				.concat(Pcid.split("-")[2]);
		WebElement PC = driver.findElement(By.xpath(".//div[contains(@id,'" + Pcid1 + "')]//div"));
		if (PC.getAttribute("innerText").equals("Multiple")) {
			Assert.assertTrue(true);
			log.info("RESULT :: PartClass having MULTIPLE Caption");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT :: PartClass don't have MULTIPLE Caption");
		}
		log.info(
				"------------------------------------------------------------------------------------------------------------------------------");
		log.info("2. CHECK FOR MULTIPLE CAPTION FOR LEAD QTY");
		String Lqid = CheckXpath("BOM_LEADQTY_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String Lqid1 = Lqid.split("-")[0].concat("-0-").concat(Pcid.split("-")[1]).concat("-")
				.concat(Lqid.split("-")[2]);
		WebElement LQ = driver.findElement(By.xpath(".//div[contains(@id,'" + Lqid1 + "')]//div"));
		if (LQ.getAttribute("innerText").equals("Multiple")) {
			Assert.assertTrue(true);
			log.info("RESULT :: Lead Qty having MULTIPLE Caption");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT :: Lead Qty don't have MULTIPLE Caption");
		}
		log.info(
				"------------------------------------------------------------------------------------------------------------------------------");
		log.info("3. CHECK FOR MULTIPLE CAPTION FOR ATTR RATE%");
		String Arid = CheckXpath("BOM_ATTRRATE_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String Arid1 = Arid.split("-")[0].concat("-0-").concat(Pcid.split("-")[1]).concat("-")
				.concat(Arid.split("-")[2]);
		WebElement AR = driver.findElement(By.xpath(".//div[contains(@id,'" + Arid1 + "')]//div"));
		if (AR.getAttribute("innerText").equals("Multiple")) {
			Assert.assertTrue(true);
			log.info("RESULT :: Attr Rate having MULTIPLE Caption");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT :: Attr Rate don't have MULTIPLE Caption");
		}
	}

	public void MPNdetail() throws Exception {
		waitforseconds(8);
		BOM_Properties BP = new BOM_Properties();
		BOM_Properties.gridmenu();
		waitforseconds(5);
		WebElement header = driver.findElement(By.xpath(".//span[contains(text(),'MPN')]"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", header);
		waitforseconds(5);
		String id = CheckXpath("BOM_MPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-1-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);

		waitforseconds(5);
		WebElement desc = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		waitforseconds(2);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollRight = arguments[0].offsetWidth", desc);
		waitforseconds(3);
		Actions actions = new Actions(driver);
		log.info("~>> Editing MPN detail ");
		actions.moveToElement(desc).doubleClick().perform();
		waitforseconds(8);
		WebElement mpn = driver.findElement(By.xpath(".//*[@id='dvMPN_0']"));
		actions.moveToElement(mpn).doubleClick().perform();
		actions.sendKeys("5014").perform();
		actions.click();
		log.info("~>> MPN number added");
		waitforseconds(5);
		log.info("~>> MPN Edited");
		BP.inlineInput("PART_MFGR_LABEL", "Keystone", "-0-");
		log.info("~>> Mfgr Edited");
		waitforseconds(5);
		WebElement BOMsubmit = CheckXpath("BOM_SUBMIT");
		safeJavaScriptClick(BOMsubmit);
		log.info("~>> BOM Submitted");
		laborpopup();
		waitforseconds(8);
	}

	public void laborpopup() throws Exception {
		waitforseconds(3);
		boolean activity = false;
		try {
			activity = CheckXpath("LABOR_CALC_UPDATE").isDisplayed();
		} catch (Exception e) {

		}
		if (activity) {
			log.info("~>> Update Labor calculation popup displayed ");
			safeJavaScriptClick(CheckXpath("LABOR_CALC_SUBMIT"));
			log.info("~>> Submit clicked");
			alertacceptpopup();
		} else {
			log.info("~>> No labor activity popup displayed");
		}
	}

	public void scrolling() throws InterruptedException {
		waitforseconds(5);
		log.info("6.  EXCESS QTY AND PRICE CALCULATION VERIFICATION ");
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
		// log.info("~>> Scrolling done");
		waitforseconds(3);
	}

	public void filter() throws InterruptedException {
		Thread.sleep(8000);
		List<WebElement> filter = driver
				.findElements(By.xpath(".//i[@class='ui-grid-icon-angle-down' and @aria-hidden='true']"));
		int size = filter.size();
		Pricefilter = driver.findElement(
				By.xpath("(.//i[@class='ui-grid-icon-angle-down' and @aria-hidden='true'])[" + size + "]"));
		Pricefilter.click();
		waitforseconds(5);
		driver.findElement(By.xpath(".//button[contains(text(),'Excess Amt')]")).click();
		log.info("~>> Clicked on excess Amount");
		waitforseconds(2);
		Pricefilter.click();
		driver.findElement(By.xpath(".//button[contains(text(),'Sort Descending')]")).click();
		log.info("~>> Sorting done on ascending order");
		waitforseconds(5);
	}

	public void calculation() throws Exception {
		String priceid = Pricefilter.findElement(By.xpath(".//parent::div[1]")).getAttribute("id")
				.replaceAll("-menu-button", "");
		String priceid1 = priceid.split("-")[0].concat("-0-").concat(priceid.split("-")[1]).concat("-")
				.concat(priceid.split("-")[2]);
		WebElement pricedesc = driver.findElement(By.xpath(".//div[contains(@id,'" + priceid1 + "')]//div"));

		Actions action = new Actions(driver);
		action.moveToElement(pricedesc).perform();
		WebElement tooltip = pricedesc.findElement(By.xpath(".//a"));
		String Strtooltip = tooltip.getAttribute("uib-tooltip").replace(",", "");
		log.info("RESULT 1 :: TOOL TIP -  " + Strtooltip);
		String ActualExcess_Qty = "Excess Qty:" + Strtooltip;

		log.info("~>> CALCULATING EXCESS QTY AND PRICE");
		safeJavaScriptClick(pricedesc);
		log.info("~>> Clicked on CQ Price Selector");
		waitforseconds(10);
		String totalQty = CheckXpath("CQPS_TOTALQTY").getAttribute("value").replaceAll(",", "");

		String ordid = CheckXpath("CQPS_ORDQTY_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String ordid1 = ordid.split("-")[0].concat("-0-").concat(ordid.split("-")[1]).concat("-")
				.concat(ordid.split("-")[2]);

		waitforseconds(8);
		WebElement orddesc = driver.findElement(By.xpath(".//div[contains(@id,'" + ordid1 + "')]//div"));
		String orderQty = orddesc.getAttribute("innerText").replaceAll(",", "");

		String unitid = CheckXpath("CQPS_UNIT_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String unitid1 = unitid.split("-")[0].concat("-0-").concat(unitid.split("-")[1]).concat("-")
				.concat(unitid.split("-")[2]);

		waitforseconds(8);
		WebElement unitdesc = driver.findElement(By.xpath(".//div[contains(@id,'" + unitid1 + "')]//div"));
		String unitprice = unitdesc.getAttribute("innerText");

		log.info("~>> TOTAL QTY :: " + totalQty);
		log.info("~>> ORDER QTY :: " + orderQty);
		log.info("~>> UNIT PRICE :: " + unitprice);

		double ExpectedExcess_Qty = Double.parseDouble(orderQty) - Double.parseDouble(totalQty);
		BigDecimal bdExpExcess_Qty = new BigDecimal(Double.toString(ExpectedExcess_Qty)).setScale(6)
				.stripTrailingZeros();
		String ExpExcess_Qty = bdExpExcess_Qty.toPlainString();
		log.info("~>> EXPECTED EXCESS QTY (Order Qty - Total Qty) :: " + ExpExcess_Qty);
		safeJavaScriptClick(CheckXpath("CQPS_CLOSE"));
		waitforseconds(10);
		double ExpectedExcess$ = Double.parseDouble(unitprice) * ExpectedExcess_Qty;
		String ExpExcess_$ = Double.toString(ExpectedExcess$);
		System.out.println("!" + ExpExcess_$);
		log.info("~>> EXPECTED EXCESS $ (Unit Price * Excess Qty) :: " + ExpExcess_$.split("[.]")[0]);

		String ActualExcess_$ = tooltip.getText().replaceAll("[^\\d.]", "").split("[.]")[0];
		log.info("~>> ACTUAL EXCESS $  :: " + ActualExcess_$);
		if (ActualExcess_Qty.contains(ExpExcess_Qty)) {
			Assert.assertTrue(true);
			log.info("RESULT 2 :: EXCESS QTY CALCULATION IS CORRECT");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT 2 :: EXCESS QTY CALCULATION IS WRONG");
		}
		if (ExpExcess_$.equals(ActualExcess_$)) {
			Assert.assertTrue(true);
			log.info("RESULT 3 :: EXCESS $ CALCULATION IS CORRECT");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT 3 :: EXCESS $ CALCULATION IS WRONG");
		}
	}
}