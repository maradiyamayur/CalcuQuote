package SmokeTest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.QuoteCQ_AutoSelect;
import QuoteCQ.QuoteCQ_ImportBOM;
import QuoteCQ.QuoteCQ_UpdatePricing;
import utility.UtilityMethods;

public class Smoke_BOMLineItems extends BaseInitsmokeTest {

	String LineItem;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. IMPORT BOM AND SUBMIT")
	public void importBOM() throws Exception {
		QuoteCQ_ImportBOM QIB = new QuoteCQ_ImportBOM();
		QIB.importMain();
	}

	@Test(priority = 2, description = "#. UPDATE PRICING AND AUTO SELECT")
	public void updatePricing() throws Exception {
		QuoteCQ_UpdatePricing QUP = new QuoteCQ_UpdatePricing();
		QuoteCQ_AutoSelect QAS = new QuoteCQ_AutoSelect();
		QUP.pricingmain();
		QAS.autoSelectmain();
	}

	@Test(priority = 3, description = "#. ADD MANUAL LINE ITEM AND SUBMIT BOM")
	public void manualLine() throws Exception {
		headerFormat("#. CASE : ADD MANUAL LINE ITEM AND MANUAL PRICING");
		chkOpenQuote();
		addManualLine();
	}

	@Test(priority = 4, description = "#. PERFORM MANUAL PRICING AND SUBMIT MATERIAL COSTING")
	public void manualPricing() throws Exception {
		headerFormat("#. CASE : PERFORM MANUAL PRICING");
		chkOpenQuote();
		addManualPricing();
		verifyPricing();
	}

	public void addManualLine() throws Exception {
		safeJavaScriptClick(CheckXpath("BOM_TAB"));
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("ADD_LINE_ITEM"));
		waitforseconds(5);
		List<WebElement> lstLine = driver.findElements(By.xpath(".//div[contains(@class,'ng-scope invalid')]"));
		for (WebElement eleline : lstLine) {
			WebElement ele = eleline.findElement(By.xpath(".//parent::div[@role='gridcell']"));
			String id = ele.getAttribute("id");
			// System.out.println("id:" + id);
			String id1 = id.substring(id.indexOf("-") + 3).replace("cell", "");
			// System.out.println("1 :" + id1);
			String no = id.substring(id.indexOf("-") + 1);
			String result = no.split("-")[0];
			int i = Integer.parseInt(String.valueOf(result));
			String strheader = driver.findElement(By.xpath(".//span[contains(@id,'" + id1 + "')][1]"))
					.getAttribute("innerText");
			if (strheader.contains("#*")) {
				System.out.println("~>> Updating " + strheader);
				waitforseconds(3);
				String strlineitem = Integer.toString(i + 1);
				WebElement eleLineItem = driver
						.findElement(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div)[last()]"));
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
				waitforseconds(3);
				log.info("~>> Scrolling done");
				Actions actions = new Actions(driver);
				actions.moveToElement(eleLineItem).doubleClick().perform();
				waitforseconds(2);
				driver.findElement(By.xpath(".//*[@id='txtRowNumber_" + i + "']")).sendKeys(strlineitem);
			} else if (strheader.contains("Qty Per*")) {
				System.out.println("~>> Updating " + strheader);
				WebElement eleQty = driver.findElement(By.id("dvQty_" + i + ""));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", eleQty);
				waitforseconds(5);
				Actions actions = new Actions(driver);
				actions.moveToElement(eleQty).doubleClick().perform();
				waitforseconds(2);
				String strqQty = Integer.toString(i + 20);
				driver.findElement(By.id("txtQty_" + i + "")).sendKeys(strqQty);
				waitforseconds(2);
				driver.findElement(By.id("txtQty_" + i + "")).sendKeys(Keys.ENTER);
				waitforseconds(3);
			} else if (strheader.contains("MPN *")) {
				System.out.println("~>> Updating " + strheader);
				waitforseconds(3);
				WebElement elempn = driver.findElement(By.xpath(".//*[@id='dvMPN_0']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(elempn).doubleClick().perform();
				waitforseconds(2);
				String strMPN = Integer.toString(5014);
				driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]/div[2]/form/input")).sendKeys(strMPN);
				waitforseconds(3);
				driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]/div[2]/form/input"))
						.sendKeys(Keys.ENTER);
				waitforseconds(3);
			}
		}
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		log.info("~>> Submit button clicked");
		QuoteCQ_ImportBOM IB = new QuoteCQ_ImportBOM();
		IB.chkLaborupdate();
		alertacceptpopup();
		log.info("~>> RESULT :: BOM SUBMITTED");
		System.out.println("~>> RESULT :: BOM SUBMITTED");
		waitforseconds(5);
		driver.navigate().refresh();
		waitforseconds(5);
	}

	public void addManualPricing() throws Exception {
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		waitforseconds(10);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 3000", scrollbar);
		waitforseconds(8);
		WebElement sort = driver.findElement(By.xpath(
				".//div[@row='grid.appScope.purchases']//a//i//preceding::i[@class='ui-grid-icon-angle-down'][1]"));
		safeJavaScriptClick(sort);
		driver.findElement(By.xpath(".//button[contains(text(),'Sort Ascending')]")).click();
		waitforseconds(5);
		if (driver.findElement(By.xpath("(.//div[@row='grid.appScope.purchases']//a//i)[1]")).getAttribute("class")
				.contains("no-pricing")) {
			System.out.println("~>> Pricing is not Available");
		} else {
			System.out.println("~>> Pricing is Available");
			safeJavaScriptClick(CheckXpath("SELECT_LINE_1"));
			safeJavaScriptClick(CheckXpath("MC_EDIT_BTN"));
			safeJavaScriptClick(CheckXpath("MATERIAL_DELETE_PRICING"));
			alertacceptpopup();
			waitforseconds(5);
		}
		try {
			WebElement eleNoPricing = driver.findElement(By.xpath("(.//i[contains(@class,'no-pricing')])[1]"));
			eleNoPricing.click();
			waitforseconds(8);
		} catch (NoSuchElementException e) {
			log.info(e.getMessage());
		}
		boolean addPrice = false;
		try {
			addPrice = CheckXpath("CQPS_ADD_MANUAL_PRICE").isDisplayed();
		} catch (Exception e) {

		}
		if (addPrice) {
			LineItem = driver.findElement(By.xpath(".//*[@id='header1']//input[@ng-model='curruntItemNo']"))
					.getAttribute("value");
			System.out.println("~>> Adding Manual Pricing for Line Item :: " + LineItem);
			safeJavaScriptClick(CheckXpath("CQPS_ADD_MANUAL_PRICE"));
			waitforseconds(8);
			CheckXpath("CQPS_ADD_SUPPLIER_NAME").clear();
			CheckXpath("CQPS_ADD_SUPPLIER_NAME").sendKeys("Mouser");
			waitforseconds(5);
			driver.findElement(By.xpath(".//ul//li[1]")).click();
			CheckXpath("CQPS_ADD_MFGR").sendKeys("Mouser");
			waitforseconds(2);
			CheckXpath("CQPS_ADD_UNIT_PRICE").sendKeys("12");
			waitforseconds(2);
			CheckXpath("CQPS_ADD_MIN_ORDER_QTY").sendKeys("1000");
			CheckXpath("CQPS_ADD_STOCK").sendKeys("5000");
			waitforseconds(2);
			CheckXpath("CQPS_ADD_LEADTIME").sendKeys("5");
			waitforseconds(2);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					CheckXpath("CQPS_ADD_PRICE_SAVE"));
			waitforseconds(5);
			CheckXpath("CQPS_ADD_NOTES").sendKeys("Auto Test Notes");
			waitforseconds(2);
			safeJavaScriptClick(CheckXpath("CQPS_ADD_PRICE_SAVE"));
			System.out.println("~>> Manual Pricing Added Successfully");
		}
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("CQPS_CLOSE"));
		alertacceptpopup();
		driver.navigate().refresh();
		waitforseconds(10);
	}

	public void verifyPricing() {
		System.out.println("~>> Verifying pricing");
		log.info("~>> Verifying pricing");
		CheckXpath("MATERIAL_LINE_FILTER").clear();
		CheckXpath("MATERIAL_LINE_FILTER").sendKeys(LineItem);
		waitforseconds(5);
		log.info("~>> Line Item Searched");
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft +=3000", scrollbar);
		waitforseconds(10);
		if (driver.findElement(By.xpath("(.//div[@row='grid.appScope.purchases']//a)[1]")).getAttribute("uib-tooltip")
				.contains("Pricing Selected")) {
			System.out.println("~>> RESULT :: Pricing Selected");
			log.info("~>> RESULT :: Pricing Selected");
			Assert.assertTrue(true);
		} else {
			System.out.println("~>> RESULT :: Pricing Data Unavilable");
			log.info("~>> RESULT :: Pricing Data Unavilable");
			Assert.assertTrue(false);
		}
	}
}