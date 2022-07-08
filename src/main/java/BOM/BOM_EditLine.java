package BOM;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BOM_EditLine extends BaseInitBOM {
	BOM_Properties BP = new BOM_Properties();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BOM")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "Single line edit BOM using Edit Button ")
	public void editSingleLine() throws Exception {
		bomPropertiesUpdate(0);
	}

	@Test(priority = 2, description = "Edit single line BOM using inline ")
	public void editSingleInline() throws Exception {
		bomPropertiesUpdate(1);
	}

	@Test(priority = 3, description = "Multi  line edit BOM using Edit Button")
	public void editMultiLine() throws Exception {
		bomPropertiesUpdate(2);
	}

	@Test(priority = 4, description = "Edit MPN details and verify it")
	public void editMPN() throws Exception {
		quoteOpen();
		waitforseconds(8);
		boolean add_line = false;
		try {
			add_line = driver.findElement(By.xpath(".//i[@ng-click='addLineItem();']")).isDisplayed();
		} catch (Exception e) {

		}
		if (add_line) {
			log.info("~>> No line item available to edit");
		} else {
			boolean spinner = false;
			try {
				spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
			} catch (Exception e) {

			}
			if (spinner) {
				log.info("~>> Previous operation is still in progress");
			} else {
				MPNdetail();
				MPNVerification();
			}
		}
	}

	public void bomPropertiesUpdate(int type) throws Exception {
		quoteOpen();
		boolean add_line = false;
		try {
			add_line = driver.findElement(By.xpath(".//i[@ng-click='addLineItem();']")).isDisplayed();
		} catch (Exception e) {

		}
		if (add_line) {
			log.info("~>> No line item available to edit");
		} else {
			BOM_Properties.gridmenu();
			BP.install(type);
			BP.purchase(type);
			BP.qtyPer(type);
			BP.description(type);
			BP.designator(type);
			BP.partNo(type);
			BP.NoLeads(type);
			BP.partClass(type);
			BP.partUOM(type);
			BP.leadQty(type);
			BP.attrRate(type);
			BP.MinPurchase(type);
		}
	}

	public void MPNdetail() throws InterruptedException {
		String id = CheckXpath("BOM_MPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-2-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		waitforseconds(5);
		WebElement desc = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		waitforseconds(2);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollRight = arguments[0].offsetWidth", desc);
		waitforseconds(3);
		Actions actions = new Actions(driver);
		log.info("~>> Editing MPN detail ");
		actions.moveToElement(desc).doubleClick().perform();
		waitforseconds(5);
		CheckXpath("MPN_FILTER").click();
		waitforseconds(3);
		if (CheckXpath("SUPPLIER_PN").getAttribute("class").contains("ui-grid-icon-cancel")) {
			CheckXpath("SUPPLIER_PN").click();
		}
		waitforseconds(3);
		if (CheckXpath("CUSTOMER_PN").getAttribute("class").contains("ui-grid-icon-cancel")) {
			CheckXpath("CUSTOMER_PN").click();
		}
		waitforseconds(3);
		if (CheckXpath("INTERNAL_PN").getAttribute("class").contains("ui-grid-icon-cancel")) {
			CheckXpath("INTERNAL_PN").click();
		}
		waitforseconds(3);
		if (CheckXpath("LIFECYCLE_STATUS").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("LIFECYCLE_STATUS").click();
		}
		waitforseconds(3);
		if (CheckXpath("CAGE_CODE").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("CAGE_CODE").click();
		}
		waitforseconds(3);
		if (CheckXpath("MPN_NOTES").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("MPN_NOTES").click();
		}
		waitforseconds(3);
		WebElement mpn = driver.findElement(By.xpath(".//*[@id='dvMPN_0']"));
		actions.moveToElement(mpn).doubleClick().perform();
		actions.sendKeys("5020").perform();
		actions.click();
		log.info("~>> MPN number added");
		waitforseconds(5);

		log.info("~>> MPN Edited");
		BP.inlineInput("PART_MFGR_LABEL", "Mfgr-Eaton", "-0-");
		log.info("~>> Mfgr Edited");
		waitforseconds(5);
		inlineMPN("Revision", "Rev 1.1");
		inlineMPN("Internal PN", "INT-123");
		inlineMPN("Customer PN", "CUST-123");
		inlineMPN("Supplier PN", "SUP-123");
		CheckXpath("BOM_SAVE").click();
		waitforseconds(8);
	}

	public void inlineMPN(String name, String input) throws InterruptedException {
		List<WebElement> headerlist = driver.findElements(
				By.xpath(" .//div[@id='PartGrid']//span[@class='ui-grid-header-cell-label ng-binding'] "));
		for (WebElement e : headerlist) {
			String header = e.getAttribute("id") + e.getAttribute("innerText");
			if (header.contains(name)) {
				String id = header.replaceAll("-header-text", "").replaceAll(name, "");
				String id1 = id.split("-")[0].concat("-0-").concat(id.split("-")[1]).concat("-")
						.concat(id.split("-")[2]);
				WebElement desc = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
				waitforseconds(3);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollRight = arguments[0].offsetWidth",
						desc);
				waitforseconds(3);
				Actions actions = new Actions(driver);
				actions.moveToElement(desc).doubleClick().perform();
				waitforseconds(3);
				actions.sendKeys(input).perform();
				actions.click();
				log.info("~>> " + name + "Edited");
				waitforseconds(5);
			}
		}
	}

	public void MPNVerification() throws Exception {
		waitforseconds(8);
		// CheckXpath("BOM_FILTER").click();
		safeJavaScriptClick(CheckXpath("BOM_FILTER"));
		if (CheckXpath("SUPPLIER_PN").getAttribute("class").contains("ui-grid-icon-cancel")) {
			CheckXpath("SUPPLIER_PN").click();
		}
		waitforseconds(3);
		if (CheckXpath("INTERNAL_PN").getAttribute("class").contains("ui-grid-icon-cancel")) {
			CheckXpath("INTERNAL_PN").click();
		}
		waitforseconds(3);
		if (CheckXpath("CUSTOMER_PN").getAttribute("class").contains("ui-grid-icon-cancel")) {
			CheckXpath("CUSTOMER_PN").click();
		}
		waitforseconds(3);
		if (CheckXpath("BOM_FILTER_PARTNO").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_PARTNO").click();
		}
		if (CheckXpath("BOM_FILTER_LEADS").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_LEADS").click();
		}
		if (CheckXpath("BOM_FILTER_PARTUOM").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_PARTUOM").click();
		}
		if (CheckXpath("BOM_FILTER_LEADQTY").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_LEADQTY").click();
		}
		if (CheckXpath("BOM_FILTER_ATTRRATE").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_ATTRRATE").click();
		}
		if (CheckXpath("BOM_FILTER_INSTALL").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_INSTALL").click();
		}
		if (CheckXpath("BOM_FILTER_PURCHASE").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_PURCHASE").click();
		}
		if (CheckXpath("BOM_FILTER_DESIG").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_DESIG").click();
		}
		if (CheckXpath("BOM_FILTER_DESCRIPTION").getAttribute("class").contains("ui-grid-icon-ok")) {
			CheckXpath("BOM_FILTER_DESCRIPTION").click();
		}
		BP.fieldCheck("MPN", "", "BOM_MPN_LABEL", "Mfgr-Eaton | 5020", "-2-");
		BP.fieldCheck("Revision", "Rev", "BOM_REV_LABEL", "Rev 1.1", "-2-");
		BP.fieldCheck("Internal PN", "", "BOM_INTPN_LABEL", "INT-123", "-2-");
		BP.fieldCheck("Supplier PN", "", "BOM_SUPPN_LABEL", "SUP-123", "-2-");
		BP.fieldCheck("Customer PN", "", "BOM_CUSPN_LABEL", "CUST-123", "-2-");
		CheckXpath("BOM_SAVE").click();
	}

}
