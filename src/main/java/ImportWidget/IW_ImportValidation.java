package ImportWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.asserts.SoftAssert;

public class IW_ImportValidation extends BaseInitImportWidget {

	static SoftAssert softAssertion = new SoftAssert();
	public static ArrayList<String> duplicateData = new ArrayList<String>();
	public static ArrayList<String> expMfgrMPN = new ArrayList<String>();
	public static String[] strduplicateMPN;
	public static HashSet<String> hashlineitem;
	public static HashSet<String> hashMPN;
	public static TreeSet<String> treeSet;
	public static ArrayList<String> arrParent = new ArrayList<String>();
	public static String strParentPart;
	public static ArrayList<String> expPartClass = new ArrayList<String>();
	public static String StrasmNum;
	public static HashSet<String> hashPartClass;

	public static void getAsmNo() {

		waitforseconds(8);
		try {
			StrasmNum = driver
					.findElement(
							By.xpath(".//*[@id='HeaderShow']//span[contains(text(),'Number')]//following::span[1]"))
					.getText();
			log.info("~>> Assembly Number - " + StrasmNum);
			System.out.println("~>> Assembly Number - " + StrasmNum);
		} catch (NoSuchElementException e) {

		}
	}

	// Verify Error Message popup
	public static void validatePopup() throws Exception {
		validateimport();
		gotoError();
		boolean errormsg = false;
		try {
			errormsg = CheckXpath("IMPORT_WIDGET_ERROR_MSG").isDisplayed();
		} catch (Exception e) {

		}
		if (errormsg) {
			log.info("~>>  ERROR MESSAGE DISPLAYED");
		} else {
			boolean validationAlert = false;
			try {
				validationAlert = CheckXpath("IW_ALERT_POPUP").isDisplayed();
			} catch (Exception e) {

			}
			if (validationAlert && arrParent == null) {
				duplicateMPN();
				waitforseconds(3);
				driver.findElement(
						By.xpath(".//div[@class='swal2-modal swal2-show']//button[@class='swal2-cancel swal2-styled']"))
						.click();
				// safeJavaScriptClick(CheckXpath("IW_ALERT_POPUP_REVIEW_BUTTON"));
				log.info("~>> Review Button Clicked");
				waitforseconds(5);
			} else if (validationAlert && arrParent != null) {
				duplicateMPN();
				waitforseconds(3);
				WebElement delete = driver.findElement(By
						.xpath(".//div[@class='swal2-modal swal2-show']//button[@class='swal2-confirm swal2-styled']"));
				delete.click();
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", delete);
				// safeJavaScriptClick(CheckXpath("IW_ALERT_POPUP_DELETE_BUTTON"));
				log.info("~>> Delete Button Clicked");
				System.out.println("~>> Delete Button Clicked");
				waitforseconds(5);
			}
		}
	}

	public void chkParentpart() {
		waitforseconds(3);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
				CheckXpath("IW_SHEET_PARENT_LABEL"));
		waitforseconds(3);
		String lineParent = CheckXpath("IW_SHEET_PARENT_LABEL").getAttribute("data-x");
		List<WebElement> lstParentPartno = driver
				.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + lineParent
						+ "'  and contains(@name,'col')]"));
		for (WebElement e : lstParentPartno) {
			if (e.getText().isEmpty()) {
			} else {
				arrParent.add(e.getText());
			}
		}
		log.info(arrParent);
	}

	public static void duplicateMPN() {
		String errorMsg = driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText();
		if (errorMsg.contains("Duplicate MPN found")) {
			log.info("~>> Error Message :: " + errorMsg);
			String pattern = "\\d([\\d,]*\\d)?";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(errorMsg);
			while (m.find()) {
				strduplicateMPN = m.group().trim().split(",");
				log.info("~>> Line Items with Duplicate MPN :: " + Arrays.toString(strduplicateMPN));
				System.out.println("~>> Line Items with Duplicate MPN :: " + Arrays.toString(strduplicateMPN));
			}
		}
	}

	// Verify error message and related field and take action accordingly
	public static void validateError(String type) throws Exception {
		validatePopup();
		waitforseconds(5);
		while (true) {
			boolean errormsg = false;
			try {
				errormsg = CheckXpath("IMPORT_WIDGET_ERROR_MSG").isDisplayed();
			} catch (Exception e) {

			}
			if (errormsg) {
				gotoError();
				boolean eleactualError = false;
				try {
					eleactualError = CheckXpath("IMPORT_WIDGET_ERROR_MSG").isDisplayed();
				} catch (Exception e) {

				}
				// log.info("ele - " + eleactualError);
				if (eleactualError) {
					log.info("~>> Error msg displayed");
					String actualError = CheckXpath("IMPORT_WIDGET_ERROR_MSG").getText();
					System.out.println("");
					System.out.println("~>> Actual Error Message :: " + actualError);
					log.info("~>> Actual Error Message :: " + actualError);

					// Case-1
					if (actualError.contains("select a valid Part Class/Part Type(Top or Sub-Assembly)")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(2, "Part Class", "top");
					}

					// Case-2
					else if (actualError.contains("Qty Per must match for the same Line Item.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Qty", "5");
					}

					// Case-3
					else if (actualError.contains(
							"A sub-assembly line item should not have alternate parts. Please revise line item numbers at")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(3, "MPN", "1");
					}

					// Case - 4
					else if (actualError.contains("not found") && actualError.contains("Part Class")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(2, "Part Class", "default");
					}

					// case-5
					else if (actualError
							.contains("Parent Part # is required or Part class must be a Top level assembly.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						if (IW_BOM_MultiLevel.parentPartarr.isEmpty()) {
							inputData(1, "Parent", "Sub1");
						} else {
							String parentPart = IW_BOM_MultiLevel.parentPartarr.get(0);
							inputData(1, "Parent", parentPart);
						}
					}

					// Case-6
					else if (actualError.equals("Qty Per must be a numeric value between 0 and 999999999.")||actualError.equals("Qty must be a numeric value between 0 and 999999999.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Qty", "120");
					}

					// Case-7
					else if (actualError.contains("Attr Rate % must be a numeric value between 0 and 100.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Attr", "100");
					}

					// Case-8
					else if (actualError
							.contains("No Of Leads must be an integer between 0 and 999999 without decimal value.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Leads", "105");
					}

					else if (actualError.equals("Lead Qty must be a numeric value between 0 and 999999999.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Lead Qty", "15");
					}
					// Case-9
					else if (actualError.contains("A Sub-Assembly line item should have a value greater than 0.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Qty", "20");
					}

					// Case-10
					else if (actualError.contains("The Parent Part Number must be referenced in the MPN column.")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(4, "Parent", " ");
					}

					// Case-11
					else if (actualError.contains("Multiple parts were found in Parent Part Number")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						// inputData(4, "Parent", " ");
						strParentPart = actualError.replaceAll("Multiple parts were found in Parent Part Number", "")
								.split("at")[0].replaceAll("\\s", "");
						log.info("~>> Multiple Parent Part Number :: " + strParentPart);
						inputData(5, "MPN", "5014");
					}
					// Case-12
					else if (actualError.contains("Parent Part Number and MPN cannot be the same")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(4, "MPN", "");
					}

					// Case-13
					else if (actualError.contains("Your Assembly Number")
							&& actualError.contains("cannot be listed as a Parent Part Number")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "Parent", " ");
					}

					// Case-14
					else if (actualError.contains("Your Assembly Number")
							&& actualError.contains("cannot be listed as a MPN")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "MPN", "5555");
					}

					else if (actualError.contains("Either MPN or Description is required")) {
						log.info("~>> ERROR :: " + actualError + " - DISPLAYED");
						System.out.println("~>> ERROR :: " + actualError + " - DISPLAYED");
						softAssertion.assertTrue(true);
						inputData(1, "MPN", "2021");
					}

					// Demandline-cases
					else if (actualError
							.contains("Unit Price must be a numeric value between 0.00001 and 999999.99999.")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Unit Price", "100");
					}
					// Case -2
					else if (actualError
							.contains("Requested Qty must be a numeric value between 1 and 999999999.999999.")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Requested Qty", "1500");
					}
					// Case-3
					else if (actualError.contains("Target Price must be a numeric value between")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Target Price", "2000");
					}
					// Case-4
					else if (actualError.contains("Duplicate MPN found on the row(s)")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(7, "MPN", "");
					}
					// Case-5
					else if (actualError.contains("Supplier is required if you have UnitPrice")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Supplier", "Mouser");
						validateimport();
					}
					// Case-6
					else if (actualError.contains("Requested Quantity must match for same Line Number")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Requested Qty", "100");
					}
					// Case-7
					else if (actualError.contains("RoHS") && actualError.contains("not found")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(8, "RoHS", "Yes");
					}
					// Case-8
					else if (actualError.contains("NCNR") && actualError.contains("not found")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(8, "NCNR", "No");
					}
					// Case-9
					else if (actualError.contains("Requested Qty is required.")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Requested Qty", "120");
					}
					// Case-10
					else if (actualError.contains("Mfgr is required.")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(1, "Mfgr", "Mouser");
					}
					// Case-11
					else if (actualError.contains("MPN is required.")) {
						log.info("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						System.out.println("~>> RESULT ::  ERROR MESSAGE DISPLAYED - " + actualError);
						softAssertion.assertTrue(true);
						inputData(7, "MPN", "");
					}
				}

			} else {
				log.info("~>> NO MORE ERROR EXISTS");
				if (type.equals("BOM")) {
					getActualLineitem();
					getActualPartclass();
					IW_BOM_MultiLevel.getTotalsublevelBOM();
					validatePopup();
					validateimport();
					break;
				} else if (type.equals("DemandLine")) {
					getActualLineitem();
					validateimport();
					break;
				}
			}
		}
	}

	public static void inputData(int type, String field, String input) throws Exception {
		// Text input
		if (type == 1) {
			/*
			 * driver .findElement(By.xpath(
			 * ".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field
			 * + "')]")).click();
			 */
			String colNumber = driver
					.findElement(By.xpath(
							".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field + "')]"))
					.getAttribute("data-x");
			//System.out.println("col number - " + colNumber);
			List<WebElement> lstError = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@style,'background-color: rgb(241, 158, 154);')]"));
			// log.info("~>> Total error :: " + lstError.size());
			for (WebElement eleError : lstError) {
				waitforseconds(3);
				log.info("Error - " + eleError);
				Actions actions = new Actions(driver);
				actions.moveToElement(eleError).doubleClick().perform();
				waitforseconds(3);
				actions.sendKeys(Keys.BACK_SPACE);
				for (int i = 1; i <= 15; i++) {
					actions.sendKeys(Keys.BACK_SPACE);
				}
				actions.sendKeys(input).perform();
				waitforseconds(5);
				driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
				waitforseconds(2);
				safeJavaScriptClick(driver.findElement(By.xpath(".//*[@id='progressbar']")));
				gotoError();
				// break;
			}
		}
		// Drop-down input
		else if (type == 2) {
			String colNumber = driver
					.findElement(By.xpath(
							".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field + "')]"))
					.getAttribute("data-x");

			List<WebElement> lstError = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@style,'background-color: rgb(241, 158, 154);')]"));

			for (WebElement eleError : lstError) {
				waitforseconds(3);
				Actions actions = new Actions(driver);
				actions.moveToElement(eleError).doubleClick().perform();
				waitforseconds(3);
				actions.sendKeys(input).perform();
				waitforseconds(2);
				driver.findElement(
						By.xpath(".//div[@class='jdropdown-item' and not(contains(@style,'display: none;'))]//div"))
						.click();
				waitforseconds(2);
				driver.findElement(
						By.xpath(".//div[@class='swal2-buttonswrapper']//button[@class='swal2-cancel swal2-styled']"))
						.click();
				waitforseconds(3);
				gotoError();
				break;
			}
		}

		// Drop-down input
		else if (type == 3) {
			String colNumber = driver
					.findElement(By.xpath(
							".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field + "')]"))
					.getAttribute("data-x");

			String LineItemColNumber = CheckXpath("IW_SHEET_LINEITEM_LABEL").getAttribute("data-x");

			List<WebElement> lstError = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@style,'background-color: rgb(241, 158, 154);')]"));

			log.info("~>> Total error :: " + lstError.size());
			for (WebElement ele : lstError) {
				waitforseconds(3);
				// log.info("" + eleError);
				String row = ele.getAttribute("data-y");
				WebElement eleError = driver.findElement(By.xpath(".//*[@id='validationSpreadsheet']//td[@data-x='"
						+ LineItemColNumber + "' and @data-y='" + row + "']"));
				Actions actions = new Actions(driver);
				actions.moveToElement(eleError).doubleClick().perform();
				waitforseconds(3);
				actions.sendKeys(input).perform();
				waitforseconds(2);
				driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
				waitforseconds(3);
				gotoError();
			}
		}

		else if (type == 4) {
			String colNumber = driver
					.findElement(By.xpath(
							".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field + "')]"))
					.getAttribute("data-x");
			List<WebElement> lstError = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@style,'background-color: rgb(241, 158, 154);')]"));
			for (WebElement e : lstError) {
				Actions actions = new Actions(driver);

				actions.moveToElement(e).perform();
				log.info("Move to Element");
				actions.contextClick(e).build().perform();
				log.info("Click Performed");
				waitforseconds(2);
				driver.findElement(By.xpath(
						".//div[@class='jexcel_contextmenu']//ul//li//a[contains(text(),'Delete selected rows')]"))
						.click();
				log.info("Delete Selected Rows");
				waitforseconds(2);
				driver.findElement(By.xpath(".//*[@id='progressbar']/li[3]")).click();
				log.info("Progress Bar Clicked");
				gotoError();
			}
		}

		else if (type == 5) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					CheckXpath("IW_SHEET_MPN_LABEL"));
			waitforseconds(5);
			String colNumber = driver
					.findElement(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[text()='" + field + "']"))
					.getAttribute("data-x");
			List<WebElement> lstmpn = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(text(),'" + strParentPart + "')]"));

			// String[] parentMpn = new String[lstmpn.size()];
			log.info("~>> Total Multiple Parts :: " + lstmpn.size());
			for (int k = 2; k <= lstmpn.size(); k++)
			// for (WebElement e : lstmpn)
			{
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
						CheckXpath("IW_SHEET_MPN_LABEL"));
				waitforseconds(5);
				WebElement elempn = driver
						.findElement(By.xpath("(.//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
								+ "' and contains(text(),'" + strParentPart + "') ])[" + k + "]"));
				// log.info("::::: " + k + elempn);

				Actions actions = new Actions(driver);
				actions.moveToElement(elempn).doubleClick().perform();
				waitforseconds(3);
				for (int i = 1; i <= 15; i++) {
					actions.sendKeys(Keys.BACK_SPACE);
				}
				actions.sendKeys(input).perform();
				waitforseconds(5);
				// driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
				// waitforseconds(2);
				driver.findElement(By.xpath(".//*[@id='progressbar']/li[3]")).click();
				waitforseconds(3);
				WebElement ele = driver.findElement(By.xpath(".//input[@class='jexcel_search']"));
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].click()", ele);
				// driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
				gotoError();
			}
		}
		// update partclass
		else if (type == 6) {
			waitforseconds(5);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					CheckXpath("IW_SHEET_PARTCLASS_LABEL"));
			waitforseconds(3);
			String colNumber = CheckXpath("IW_SHEET_PARTCLASS_LABEL").getAttribute("data-x");

			WebElement eleupdate = driver
					.findElement(By.xpath("(.//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@name,'col-')])[1]"));
			waitforseconds(3);
			Actions actions = new Actions(driver);
			actions.moveToElement(eleupdate).doubleClick().perform();
			waitforseconds(2);
			actions.sendKeys(input).perform();
			waitforseconds(2);
			driver.findElement(
					By.xpath(".//div[@class='jdropdown-item' and not(contains(@style,'display: none;'))]//div"))
					.click();
			waitforseconds(2);
			safeJavaScriptClick(CheckXpath("DL_POPUP_YES"));
			waitforseconds(3);
			String expPartClass = eleupdate.getText();
			log.info("~>> PART CLASS UPDATED :: " + expPartClass);
			System.out.println("~>> PART CLASS UPDATED :: " + expPartClass);
		}
//Demandline Input
		else if (type == 7) {
			String colNumber = driver
					.findElement(By.xpath(
							".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field + "')]"))
					.getAttribute("data-x");
			List<WebElement> lstError = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@style,'background-color: rgb(241, 158, 154);')]"));
			try {
				for (int i = 0; i <= lstError.size(); i++) {
					WebElement eleError = lstError.get(i);
					int j = 5014 + i;
					String Strmpn = String.valueOf(j);
					waitforseconds(3);
					Actions actions = new Actions(driver);
					actions.moveToElement(eleError).doubleClick().perform();
					waitforseconds(3);
					for (int k = 1; k <= 15; k++) {
						actions.sendKeys(Keys.BACK_SPACE);
					}
					waitforseconds(2);
					actions.sendKeys(Strmpn).perform();
					waitforseconds(3);
					driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
					waitforseconds(3);
					driver.findElement(By.xpath(".//*[@id='progressbar']/li[3]")).click();
					gotoError();
				}
			} catch (IndexOutOfBoundsException e) {
				log.info("~>> " + e.getMessage());
			}
		} // drop-down - DL
		else if (type == 8) {
			String colNumber = driver
					.findElement(By.xpath(
							".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'" + field + "')]"))
					.getAttribute("data-x");
			List<WebElement> lstError = driver
					.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber
							+ "' and contains(@style,'background-color: rgb(241, 158, 154);')]"));
			for (WebElement eleError : lstError) {
				waitforseconds(5);
				Actions actions = new Actions(driver);
				actions.moveToElement(eleError).doubleClick().perform();
				waitforseconds(3);
				actions.sendKeys(input).perform();
				waitforseconds(3);

				driver.findElement(
						By.xpath(".//div[@class='jdropdown-item' and not(contains(@style,'display: none;'))]//div"))
						.click();

				// actions.sendKeys(Keys.ENTER);
				waitforseconds(2);
				driver.findElement(
						By.xpath(".//div[@class='swal2-buttonswrapper']//button[@class='swal2-confirm swal2-styled']"))
						.click();
				waitforseconds(3);
				gotoError();
				break;
			}
		}
		log.info("~>> Input Data Corrected for :: " + field);
	}

	public static void getActualPartclass() {
		String partclassColumn = CheckXpath("IW_SHEET_PARTCLASS_LABEL").getAttribute("data-x");
		List<WebElement> lstPartclass = driver
				.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + partclassColumn
						+ "' and contains(@name,'col-')]"));
		String[] partclassarr = new String[lstPartclass.size()];
		String[] linearr = new String[lstPartclass.size()];
		for (int k = 0; k < lstPartclass.size(); k++) {
			linearr[k] = lstPartclass.get(k).getText();
			partclassarr[k] = linearr[k] + "-" + lstPartclass.get(k).getText();
		}
		treeSet = new TreeSet<String>(Arrays.asList(partclassarr));
		/*
		 * hashPartClass = new HashSet<String>(treeSet);
		 * log.info("~>>ACTUAL TOTAL PART CLASS :: " + hashPartClass);
		 */
		log.info("~>>ACTUAL TOTAL PART CLASS :: " + treeSet);
	}

	// Get Duplicate line item and related MPN's
	public static void getActualLineitem() {
		waitforseconds(8);
		boolean validate = false;
		try {
			validate = CheckXpath("VALIDATE_AND_IMPORT").isDisplayed();
		} catch (Exception e) {

		}
		if (validate) {
			log.info("~>> Checking actual line items");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					CheckXpath("IW_SHEET_LINEITEM_LABEL"));
			waitforseconds(5);
			boolean importscreen = false;
			try {
				importscreen = CheckXpath("IW_SHEET_LINEITEM_LABEL").isDisplayed();
			} catch (Exception e) {

			}
			if (importscreen) {
				String lineColumn = CheckXpath("IW_SHEET_LINEITEM_LABEL").getAttribute("data-x");
				String mfgrColumn = CheckXpath("IW_SHEET_MFGR_LABEL").getAttribute("data-x");
				String mpnColumn = CheckXpath("IW_SHEET_MPN_LABEL").getAttribute("data-x");

				waitforseconds(5);
				List<WebElement> lstlineitems = driver
						.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + lineColumn
								+ "' and contains(@name,'col')]"));

				log.info("~>> SIZE :: " + lstlineitems.size());
				String[] linearr = new String[lstlineitems.size()];
				for (int k = 0; k < lstlineitems.size(); k++) {
					linearr[k] = lstlineitems.get(k).getText();
					log.info("~>> Existing Line Item :: " + linearr[k]);
				}
				hashlineitem = new HashSet<String>(Arrays.asList(linearr));
				log.info("~>> ACTUAL  TOTAL LINE ITEMS  ::  " + hashlineitem.size());

				for (int k = 0; k < linearr.length; k++) {
					for (int j = k + 1; j < linearr.length; j++) {
						if (linearr[k].equals(linearr[j])) {

							duplicateData.add(linearr[j]);
							log.info("~>> Duplicate Line Item  :: " + linearr[j]);
							// log.info("~>> Duplicate Line Item2 :: " + linearr[k]);
							WebElement duplicateMfgr = driver
									.findElement(By.xpath(".//*[@id='validationSpreadsheet']//table//tr[" + (k + 1)
											+ "]//td[@data-x='" + mfgrColumn + "' and contains(@name,'col')]"));
							WebElement duplicateMPN = driver
									.findElement(By.xpath(".//*[@id='validationSpreadsheet']//table//tr[" + (k + 1)
											+ "]//td[@data-x='" + mpnColumn + "' and contains(@name,'col')]"));
							String finalMPN = linearr[k] + " = " + duplicateMfgr.getAttribute("innerText") + " | "
									+ duplicateMPN.getAttribute("innerText");
							expMfgrMPN.add(finalMPN);
							log.info("~>> Actual :: K " + expMfgrMPN);
							duplicateMfgr = driver.findElement(By.xpath(".//*[@id='validationSpreadsheet']//table//tr["
									+ (j + 1) + "]//td[@data-x='" + mfgrColumn + "' and contains(@name,'col')]"));
							duplicateMPN = driver.findElement(By.xpath(".//*[@id='validationSpreadsheet']//table//tr["
									+ (j + 1) + "]//td[@data-x='" + mpnColumn + "' and contains(@name,'col')]"));

							finalMPN = linearr[j] + " = " + duplicateMfgr.getAttribute("innerText") + " | "
									+ duplicateMPN.getAttribute("innerText");
							expMfgrMPN.add(finalMPN);
							log.info("~>> Actual :: J " + expMfgrMPN);
						}
					}
				}
				for (int i = 0; i < expMfgrMPN.size(); i++) {
					log.info("~>> Multiple MPN for Same lIne items :: " + expMfgrMPN.get(i));
				}
				hashMPN = new HashSet<String>(expMfgrMPN);
				log.info("~>> EXPECTED Multiple MPN for Same line items Without Duplication " + hashMPN);
				expMfgrMPN = new ArrayList<>(hashMPN);
			} else {
				log.info("~>> BOM import process completed");
			}
		}
	}

	/*
	 * public static void getActualPartclass() { ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView(true);",
	 * CheckXpath("IW_SHEET_PARTCLASS_LABEL")); waitforseconds(3); String colNumber
	 * = CheckXpath("IW_SHEET_PARTCLASS_LABEL").getAttribute("data-x");
	 * List<WebElement> lstPartclass = driver .findElements(By.xpath(
	 * ".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + colNumber +
	 * "' and contains(@name,'col-')]")); for (WebElement e : lstPartclass) { if
	 * (e.getText().isEmpty()) { } else { expPartClass.add(e.getText()); } }
	 * log.info("~>> EXPECTED PART CLASS  :: " + expPartClass); log.info(
	 * "~>>........................................................................................................................"
	 * ); }
	 */

	public static void gotoError() throws Exception {
		boolean goToError = false;
		try {
			goToError = driver.findElement(By.xpath(".//*[@id='btnNavigateToError']")).isDisplayed();
		} catch (Exception e) {

		}
		if (goToError) {
			safeJavaScriptClick(CheckXpath("IW_GOTO_ERROR_BTN"));
			waitforseconds(3);
			// System.out.println("~>> Go to Error Clicked");
		}
	}
}
