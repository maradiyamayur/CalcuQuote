package ImportWidget;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class IW_ImportVerification extends BaseInitImportWidget {

	static SoftAssert softAssertion = new SoftAssert();
	public static ArrayList<String> actMfgrMPN = new ArrayList<String>();
	public static List<WebElement> lstlineItem;

	public static void verifyImportedItem() throws Exception {
		driver.switchTo().defaultContent();
		log.info(
				"......................................................................................................");
		System.out.println("");
		log.info("~>> VERIFYING IMPORTED LINE ITEMS");
		System.out.println("~>> VERIFYING IMPORTED LINE ITEMS");
		gridmenu();
		verifyImportedLineitem();
		verifyImportedLineitemTotal();
		verifyImportedMPN();
		// verifyImportedPartClass();
		verifyFieldData("Attr Rate %", "BOM_ATTRRATE_LABEL", 0, 100);
		verifyFieldData("Qty Per", "BOM_QTYPER_LABEL", 0, 999999999);
		verifyFieldData("Lead Qty", "BOM_LEADQTY_LABEL", 0, 999999999);
		verifyFieldData("No Of Leads", "BOM_LEADS_LABEL", 0, 999999);
		verifyMPNDesc();
		verificationclear();
	}

	public static void verifyMultiLevelImportedItem() throws Exception {
		log.info(
				"......................................................................................................");
		log.info("~>> VERIFYING IMPORTED LINE ITEMS FOR MULTI-LEVEL BOM");
		System.out.println("~>>  VERIFYING IMPORTED LINE ITEMS FOR MULTI-LEVEL BOM");
		driver.switchTo().defaultContent();
		// gridmenu();
		verifyImportedLineitem();
		verifyImportedLineitemTotal();
	}

	public static void verifyImportedMPN() {
		driver.switchTo().defaultContent();
		waitforseconds(5);
		System.out.println("~>> Verifying imported MPN");

		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 1000", scrollbar);
		System.out.println("~>> Scrolling done");
		waitforseconds(10);
		String id = CheckXpath("BOM_MPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		log.info("*** Duplicate data size - " + IW_ImportValidation.duplicateData.size());
		for (int i = 0; i < IW_ImportValidation.duplicateData.size(); i++) {
			List<WebElement> lstMPN = driver
					.findElements(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div[@class='height-grid ng-scope'])["
							+ IW_ImportValidation.duplicateData.get(i) + "]//div[@class='ng-binding ng-scope']"));
			for (WebElement e : lstMPN) {
				String expMPN = IW_ImportValidation.duplicateData.get(i) + " = " + e.getText();
				actMfgrMPN.add(expMPN);
				System.out.println("** " + i + " - " + expMPN);
			}
		}
		log.info("~>> ACTUAL MULTIPLE MPN FOR SAME LINE ITEM ::  " + actMfgrMPN);
		if (IW_ImportValidation.expMfgrMPN.size() == actMfgrMPN.size()
				&& IW_ImportValidation.expMfgrMPN.containsAll(actMfgrMPN)) {
			Assert.assertTrue(true);
			log.info("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE SAME");
			System.out.println("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE SAME");
		} else {
			Assert.assertTrue(false);
			log.info("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE DIFFERENT");
			System.out.println("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE DIFFERENT");
		}
		List<WebElement> lstMPN = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		log.info("Assembly Number :: " + IW_ImportValidation.StrasmNum);
		for (WebElement e : lstMPN) {
			if (e.getText().contains(IW_ImportValidation.StrasmNum)) {
				log.info("~>> RESULT:: ASSEMBLY NUMBER IS  AS MPN");
				System.out.println("~>> RESULT:: ASSEMBLY NUMBER IS  AS MPN");
			} else {
				log.info("~>> RESULT:: ASSEMBLY NUMBER IS  NOT AS MPN");
				System.out.println("~>> RESULT:: ASSEMBLY NUMBER IS  NOT AS MPN");
			}
		}
	}

	// Verify there is no duplicate line item after import and total imported line
	// items
	public static void verifyImportedLineitem() {
		driver.switchTo().defaultContent();
		waitforseconds(8);
		String id = CheckXpath("BOM_LINE_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		lstlineItem = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		String[] lineitemarr = new String[lstlineItem.size()];
		for (int k = 0; k < lstlineItem.size(); k++) {
			lineitemarr[k] = lstlineItem.get(k).getText();
			log.info("~>> IMPORTED LINE ITEMS :: " + lineitemarr[k]);
		}
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
		for (int k = 0; k < lineitemarr.length; k++) {
			for (int j = k + 1; j < lineitemarr.length; j++) {
				if (lineitemarr[k].equals(lineitemarr[j])) {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: DUPLICATE LINE ITEMS ARE AVAILABLE");
					System.out.println("~>> RESULT :: DUPLICATE LINE ITEMS ARE AVAILABLE");
				} else {
					Assert.assertTrue(true);
					log.info("~>> RESULT :: NO DUPLICATE LINE ITEMS ARE AVAILABLE");
					System.out.println("~>> RESULT :: NO DUPLICATE LINE ITEMS ARE AVAILABLE");
				}
			}
		}
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
	}

	// Verify total line item after import
	public static void verifyImportedLineitemTotal() {
		driver.switchTo().defaultContent();
		waitforseconds(3);
		log.info("~>> EXPECTED  TOTAL LINE ITEMS :: " + lstlineItem.size());
		System.out.println("~>> EXPECTED  TOTAL LINE ITEMS :: " + lstlineItem.size());
		log.info("~>> ACTUAL  TOTAL LINE ITEMS  ::  " + IW_ImportValidation.hashlineitem.size());
		System.out.println("~>> ACTUAL  TOTAL LINE ITEMS  ::  " + IW_ImportValidation.hashlineitem.size());
		if (lstlineItem.size() == (IW_ImportValidation.hashlineitem.size())) {
			//Assert.assertTrue(true);
			log.info("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE CORRECT ");
			System.out.println("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE CORRECT");
		} else {
			//Assert.assertTrue(false);
			log.info("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE DIFFERENT");
			System.out.println("~>> RESULT :: ACTUAL AND EXPECTED TOTAL LINE ITEMS ARE DIFFERENT");
		}
		log.info(
				"..............................................................................................................");
		System.out.println(
				"..............................................................................................................");
	}

	public static void verifyImportedPartClass() {
		driver.switchTo().defaultContent();
		log.info(
				".............................................................................................................");
		waitforseconds(8);
		String lineid = CheckXpath("BOM_LINE_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String lineid1 = lineid.split("-")[1].concat("-").concat(lineid.split("-")[2]);
		List<WebElement> actlineItem = driver.findElements(By.xpath(".//div[contains(@id,'" + lineid1 + "')]//div"));
		String[] lineitemarr = new String[lstlineItem.size()];

		String id = CheckXpath("BOM_PARTCLASS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		List<WebElement> lstPartClass = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		String[] actPartclassarr = new String[lstPartClass.size()];
		try {
			for (int k = 0; k < actlineItem.size(); k++) {
				lineitemarr[k] = actlineItem.get(k).getText();
				actPartclassarr[k] = lineitemarr[k] + "-" + lstPartClass.get(k).getText();
			}
			log.info("~>> Actual Part class array" + actPartclassarr);
			/*
			 * HashSet<String> acthashPartClass = new
			 * HashSet<String>(Arrays.asList(actPartclassarr));
			 * 
			 * if (acthashPartClass.equals(ImportValidation.hashPartClass)) {
			 * Assert.assertTrue(true); log.info("~>>RESULT : PARTCLASS UPDATED CORRECTLY");
			 * System.out.println("~>>RESULT : PARTCLASS UPDATED CORRECTLY"); } else {
			 * Assert.assertTrue(false);
			 * log.info("~>> RESULT : PARTCLASS NOT UPDATED CORRECTLY");
			 * System.out.println("~>> RESULT : PARTCLASS NOT UPDATED CORRECTLY"); }
			 */
		} catch (ArrayIndexOutOfBoundsException e) {
			log.info(e.getMessage());
		}
	}

	public static void validationclear() {
		IW_ImportValidation.duplicateData.clear();
		IW_ImportValidation.expMfgrMPN.clear();
		// ImportValidation.strduplicateMPN = [];
		IW_ImportValidation.hashlineitem.clear();
		IW_ImportValidation.hashMPN.clear();
		IW_ImportValidation.arrParent.clear();
		IW_ImportValidation.strParentPart = "";
		IW_BOM_MultiLevel.parentPartarr.clear();
		IW_ImportValidation.expPartClass.clear();
		// SameDataMultilevelBOM.hashBomLevel.clear();
	}

	public static void verifyFieldData(String elename, String element, double min, double max) {

		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		Boolean horzscrollStatus = (Boolean) javascript
				.executeScript("return document.documentElement.scrollWidth>document.documentElement.clientWidth;");
		if (horzscrollStatus == true) {
			javascript.executeScript("arguments[0].scrollIntoView(true);", CheckXpath(element));
		}
		waitforseconds(2);
		String id = CheckXpath(element).getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		List<WebElement> lstelement = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));

		for (WebElement e : lstelement) {
			String output = e.getText();
			log.info("Imported Value for - " + elename + " :: " + output);
			if (output.matches("[a-zA-Z0-9]+") && output.contains("\"$!#%&'()*+,-./:;<=>?@[]^_`{|}\";")) {
				Assert.assertTrue(false);
				log.info("~>> RESULT ::  " + elename + " -  Contains alphanumeric value & Special character");
				System.out.println("~>> RESULT ::  " + elename + " -  Contains alphanumeric value & Special character");
			} else {
				Assert.assertTrue(true);
				log.info("~>> RESULT :: " + elename + " - Do not contains alphanumeric value & Special character");
				System.out.println(
						"~>> RESULT :: " + elename + " - Do not contains alphanumeric value & Special character");
			}
			String numoutput = output.replace("$", "").replace(",", "");

			if (numoutput.isEmpty()) {

			} else {
				double num = Double.parseDouble(numoutput);

				if (num >= min && num <= max) {
					Assert.assertTrue(true);
					log.info("~>> RESULT :: " + elename + " - Contains correct range value. (" + min + "  - " + max
							+ " )");
					System.out.println("~>> RESULT :: " + elename + " - Contains correct range value. (" + min + "  - "
							+ max + " )");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: " + elename + " - Contains wrong value. (" + min + "  - " + max + " )");
					System.out.println("~>> RESULT :: " + elename + " - Contains correct range value. (" + min + "  - "
							+ max + " )");
				}
			}
		}
	}

	public static void verifyMPNDesc() {
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		Boolean horzscrollStatus = (Boolean) javascript
				.executeScript("return document.documentElement.scrollWidth>document.documentElement.clientWidth;");
		if (horzscrollStatus == true) {
			javascript.executeScript("arguments[0].scrollIntoView(true);", CheckXpath("BOM_MPN_LABEL"));
		}
		waitforseconds(2);
		String mpnid = CheckXpath("BOM_MPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String mpnid1 = mpnid.split("-")[1].concat("-").concat(mpnid.split("-")[2]);
		List<WebElement> lstmpn = driver
				.findElements(By.xpath(".//div[contains(@id,'" + mpnid1 + "')]//div[@class='ng-binding ng-scope']"));

		String descid = CheckXpath("BOM_DESC_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String descid1 = descid.split("-")[1].concat("-").concat(descid.split("-")[2]);
		List<WebElement> lstdesc = driver.findElements(By.xpath(".//div[contains(@id,'" + descid1 + "')]//div"));
		try {
			for (int i = 0; i <= lstmpn.size(); i++) {
				String elempn = lstmpn.get(i).getText();
				String eledesc = lstdesc.get(i).getText();
				if (!elempn.isEmpty() || !eledesc.isEmpty()) {
					log.info("~>> RESULT :: Either MPN or Desc is available");
					System.out.println("~>> RESULT :: Either MPN or Desc is available");
				} else {
					log.info("~>> RESULT ::  Either MPN or Desc is not available");
					System.out.println("~>> RESULT ::  Either MPN or Desc is not available");
				}
			}
		} catch (IndexOutOfBoundsException e) {
			log.info("~>> " + e.getMessage());
		}
	}

	public static void verificationclear() {
		actMfgrMPN.clear();
		lstlineItem.clear();
	}
}
