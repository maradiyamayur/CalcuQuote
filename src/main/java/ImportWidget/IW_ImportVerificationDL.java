package ImportWidget;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.timeout.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class IW_ImportVerificationDL extends BaseInitImportWidget {

	static SoftAssert softAssertion = new SoftAssert();
	public static ArrayList<String> actMfgrMPN = new ArrayList<String>();
	public static List<WebElement> lstlineItem;

	public static void verifyImportedItem() throws Exception {
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
		log.info("~>> Verifying imported demand lines");
		demandList();
		// pricing();
		searchTag();
		verifyImportedLineitemTotal();
		verifyMPN();
		verifySupplier();
		pricing();
		searchTag();
		verifyDuplicateMPN();
		IW_ImportVerificationDL.verifyFieldData("Unit Price", "DL_UNIT_PRICE_LABEL", 0.00001, 999999.99999);
		IW_ImportVerificationDL.verifyFieldData("Target Price", "DL_TARGET_PRICE_LABEL", 0.000001, 99999999);
		IW_ImportVerificationDL.verifyFieldData("Requested Qty", "DL_REQ_QTY_LABEL", 1, 999999999.999999);
		IW_ImportVerificationDL.verifyRohsNCNR();
		// clear();
	}

	public static void verifyDuplicateMPN() {
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
		waitforseconds(10);
		boolean ele = false;
		try {
			ele = driver.findElement(By.xpath("(.//span[contains(@class,'badge bg-color-darken')])[1]")).isDisplayed();
		} catch (Exception e) {

		}
		if (ele) {
			driver.findElement(By.xpath("(.//span[contains(@class,'badge bg-color-darken')])[1]")).click();
			boolean popup = false;
			try
			{
				popup = driver.findElement(By.xpath(".//div[@id='swal2-content']")).isDisplayed();
			}
			catch(Exception e)
			{
				
			}
			if(popup)
			{
				System.out.println("Update Pricing is in progress. You can open this pop-up once update pricing completed.");
			}
			else
			{
			waitforseconds(8);
			String Mfgrid = CheckXpath("DL_MFGR_LABEL").getAttribute("id").replaceAll("-header-text", "");
			String Mfgrid1 = Mfgrid.split("-")[1].concat("-").concat(Mfgrid.split("-")[2]);
			List<WebElement> lstMfgr = driver.findElements(By.xpath(".//div[contains(@id,'" + Mfgrid1 + "')]//div"));
			String MPNid = CheckXpath("DL_MPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
			String MPNid1 = MPNid.split("-")[1].concat("-").concat(MPNid.split("-")[2]);
			List<WebElement> lstMPN = driver.findElements(By.xpath(".//div[contains(@id,'" + MPNid1 + "')]//div"));
			// String[] Mfgrarr = new String[lstMfgr.size()];
			String[] MPNarr = new String[lstMPN.size()];
			for (int i = 0; i < lstMfgr.size(); i++) {
				MPNarr[i] = lstMfgr.get(i).getText() + "-" + lstMPN.get(i).getText();
				log.info("~>> Multiple MPN for same demand line :: " + MPNarr[i]);
			}

			for (int k = 0; k < MPNarr.length; k++) {
				for (int j = k + 1; j < MPNarr.length; j++) {
					if (MPNarr[k].equals(MPNarr[j])) {
						Assert.assertTrue(false);
						log.info("~>> RESULT :: DUPLICATE MPN ARE AVAILABLE FOR SAME DEMAND LINE");
						System.out.println("~>> RESULT :: DUPLICATE MPN ARE AVAILABLE FOR SAME DEMAND LINE ");
					} else {
						Assert.assertTrue(true);
						log.info("~>> RESULT :: NO DUPLICATE MPN ARE AVAILABLE FOR SAME DEMAND LINE");
						System.out.println("~>> RESULT :: NO DUPLICATE MPN ARE AVAILABLE FOR SAME DEMAND LINE");
					}
				}
			}
			waitforseconds(5);
			driver.findElement(By.xpath(".//span[@ng-click='dismiss()']")).click();
			waitforseconds(5);
		}
		}
	}

	// Verify total line item after import
	public static void verifyImportedLineitemTotal() {
		waitforseconds(15);
		lstlineItem = driver.findElements(
				By.xpath(".//div[@class='ui-grid-cell-contents']//div[contains(@ng-click,'selectButtonClick')]"));
		log.info("~>> EXPECTED  TOTAL LINE ITEMS :: " + lstlineItem.size());
		log.info("~>> ACTUAL  TOTAL LINE ITEMS  ::  " + IW_ImportValidation.hashlineitem.size());
		if (lstlineItem.size() == (IW_ImportValidation.hashlineitem.size())) {
			Assert.assertTrue(true);
			log.info("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE CORRECT ");
			System.out.println("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE CORRECT");
		} else {
			Assert.assertTrue(false);
			log.info("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE DIFFERENT");
			System.out.println("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE DIFFERENT");
		}
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
	}

	public static void pricing() {
		waitforseconds(10);
		log.info("~>> INNER TEXT - "
				+ driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']")).getAttribute("innerText"));
		boolean pricing = false;
		try {
			pricing = driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']")).getAttribute("innerText")
					.isEmpty();
		} catch (Exception e) {

		}
		if (!pricing) {
			log.info("~>> Pricing is running");
			try
			{
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(150));
			WebElement elePricing = driver.findElement(By.xpath(".//*[@id='TotalStatusPercentage']"));
			wait.until(ExpectedConditions.invisibilityOf(elePricing));
			log.info("~>> Pricing is running");
			}
			catch(Exception e)
			{
				
			}
		} else {
			log.info("~>> Pricing is not running");
		}
	}

	public static void searchTag() {
		// strTags = "09062021-16-11-33";
		scroll("element", CheckXpath("DL_TAGS_FILTER"), "");
		waitforseconds(3);
		CheckXpath("DL_TAGS_FILTER").clear();
		CheckXpath("DL_TAGS_FILTER").sendKeys(strTags);
		waitforseconds(5);
		log.info("~>> Tags filter added :: " + strTags);
	}

	public static void verifySupplier() {
		System.out.println("");
		scroll("element", CheckXpath("DL_SUPPLIER_LABEL"), "");
		waitforseconds(2);
		String id = CheckXpath("DL_SUPPLIER_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		List<WebElement> lstunitprice = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		for (WebElement e : lstunitprice) {
			try {
				String strSupplier = e.getText();

				if (strSupplier != null && strSupplier.length() > 0) {
					if (strSupplier.equals("Mouser")) {
						Assert.assertTrue(true);
						log.info("~>> RESULT ::  Supplier Name is Correct");
						System.out.println("~>> RESULT ::  Supplier Name is Correct");
					} else {
						Assert.assertTrue(false);
						log.info("~>> RESULT ::  Supplier Name is  not Correct");
						System.out.println("~>> RESULT ::  Supplier Name is not Correct");
					}
				}
			} catch (StaleElementReferenceException ex) {

			}
		}
	}

	public static void verifyFieldData(String elename, String element, double min, double max) {
		System.out.println("");
		scroll("element", CheckXpath(element), "");
		waitforseconds(2);
		String id = CheckXpath(element).getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		List<WebElement> lstunitprice = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		for (WebElement e : lstunitprice) {
			try {
				String output = e.getText();
				log.info("~>> " + elename + " - Actual Value :: " + output);
				if (output != null && output.length() > 0) {
					// String numoutput = output.replaceAll("[^a-zA-Z0-9]", "");
					String numoutput = output.replaceAll("[^0-9.]", "");
					// String numoutput = output.replaceAll("[^\\d]", "");
					double num = Double.parseDouble(numoutput);
					if (num >= min && num <= max) {
						Assert.assertTrue(true);
						log.info("~>> RESULT :: " + elename + " - Contains correct range value -" + min + "-" + max);
						System.out.println(
								"~>> RESULT :: " + elename + " - Contains correct range value - " + min + "-" + max);
					} else {
						Assert.assertTrue(false);
						log.info("~>> RESULT :: " + elename + " - Contains wrong value - " + min + "-" + max);
						System.out.println("~>> RESULT :: " + elename + " - Contains wrong value - " + min + "-" + max);
					}
				}
			} catch (StaleElementReferenceException ex) {

			}
		}
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
	}

	public static void verifyRohsNCNR() throws Exception {
		log.info("~>> Verifying RoHs and NCNR for imported line items");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft -= 2000", driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']")));
		waitforseconds(8);
		try {
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(CheckXpath("DL_STATUS_FILTER")));
			CheckXpath("DL_STATUS_FILTER").clear();
			CheckXpath("DL_STATUS_FILTER").sendKeys("Supplier");
			waitforseconds(5);
			wait.until(ExpectedConditions.visibilityOf(CheckXpath("DL_TAGS_FILTER")));
			CheckXpath("DL_TAGS_FILTER").clear();
			CheckXpath("DL_TAGS_FILTER").sendKeys(strTags);
			waitforseconds(5);
			scroll("element", CheckXpath("DL_SUPPLIER_LABEL"), "");
			waitforseconds(5);
			wait.until(ExpectedConditions.visibilityOf(CheckXpath("DL_SUPPLIER_FILTER")));
			CheckXpath("DL_SUPPLIER_FILTER").sendKeys("Mouser");
			waitforseconds(5);
		} catch (TimeoutException e) {
			log.info(e.getMessage());
		}
		boolean clearFilter = false;
		try {
			clearFilter = driver.findElement(By.xpath(".//button[@title='Click here to clear all filters']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (clearFilter) {
			log.info("~>> No Records for searched criteria exists");
		} else {
			WebElement scrollbar = driver.findElement(
					By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar);
			waitforseconds(5);
			boolean pricing = false;
			try {
				pricing = driver.findElement(By.xpath("(.//span[@title='Available Prices'])[1]")).isDisplayed();
			} catch (Exception e) {

			}
			if (pricing) {
				WebElement elepricing = driver.findElement(By.xpath("(.//span[@title='Available Prices'])[1]"));
				safeJavaScriptClick(elepricing);
				log.info("~>> Clicked on Pricing");
				waitforseconds(8);
				WebElement scrollbar1 = driver.findElement(By.xpath(
						".//*[@id='content']//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 2000", scrollbar1);
				waitforseconds(5);
				boolean Rohs = false;
				boolean Ncnr = false;
				try {
					Rohs = CheckXpath("CQPS_ROHS_LABEL").isDisplayed();
					Ncnr = CheckXpath("CQPS_NCNR_LABEL").isDisplayed();
				} catch (Exception e) {

				}
				if (Rohs && Ncnr) {
					String Roid = CheckXpath("CQPS_ROHS_LABEL").getAttribute("id").replaceAll("-header-text", "");
					String Roid1 = Roid.split("-")[1].concat("-").concat(Roid.split("-")[2]);
					WebElement lstRohs = driver.findElement(By.xpath(".//div[contains(@id,'" + Roid1 + "')]//div"));
					String Ncid = CheckXpath("CQPS_NCNR_LABEL").getAttribute("id").replaceAll("-header-text", "");
					String Ncid1 = Ncid.split("-")[1].concat("-").concat(Ncid.split("-")[2]);
					WebElement lstNcnr = driver.findElement(By.xpath(".//div[contains(@id,'" + Ncid1 + "')]//div"));
					SoftAssert softAssert = new SoftAssert();
					if (lstRohs.getText().equals("Yes") && lstNcnr.getText().equals("No")) {
						softAssert.assertTrue(true);
						log.info("~>> RESULT :: Expected and Actual RoHS and NCNR are correct");
						System.out.println("~>> RESULT :: Expected and Actual RoHS and NCNR are correct");
					} else {
						softAssert.assertTrue(false);
						log.info("~>> RESULT :: Expected and Actual RoHS and NCNR are not correct");
						System.out.println("~>> RESULT :: Expected and Actual RoHS and NCNR are not correct");
					}
				} else {
					log.info("~>> RoHS and NCNR fields are hidden");
				}
			} else {
				log.info("~>> Pricing button not available");
			}
		}
	}

	public static void verifyMPN() {
		log.info("verifying Mfgr and MPN");
		scroll("element", CheckXpath("DL_ORIGINALMPN_LABEL"), "");
		String MPNid = CheckXpath("DL_ORIGINALMPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String MPNid1 = MPNid.split("-")[1].concat("-").concat(MPNid.split("-")[2]);
		List<WebElement> lstmpn = driver.findElements(By.xpath(".//div[contains(@id,'" + MPNid1 + "')]//div"));
		for (WebElement elempn : lstmpn) {
			if (elempn.getText() != "") {
				log.info("RESULT :: ~>> ORIGINAL MPN IS AVAILABLE");
				System.out.println("RESULT :: ~>> ORIGINAL MPN IS AVAILABLE");
			} else {
				log.info("RESULT :: ~>> ORIGINAL MPN IS AVAILABLE");
				System.out.println("RESULT :: ~>> ORIGINAL MPN IS AVAILABLE");
			}
		}
	}

	public static void clear() {
		IW_ImportValidation.duplicateData.clear();
		actMfgrMPN.clear();
		// ImportValidation.strduplicateMPN = [];
		IW_ImportValidation.hashlineitem.clear();
		IW_ImportValidation.hashMPN.clear();
		IW_ImportValidation.expMfgrMPN.clear();
		lstlineItem.clear();
	}
	
	public static void scroll(String type, WebElement elementpath, String pixel) {
		if (type.equals("pixelLeft")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += ' + pixel + '", elementpath);
		} else if (type.equals("element")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementpath);
		} else if (type.equals("pixelRight")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft -= ' + pixel +'", elementpath);
		}
	}
}
