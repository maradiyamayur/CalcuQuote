package ImportWidget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class IW_BOM_MultiLevel extends BaseInitImportWidget {

	public static ArrayList<String> parentPartarr = new ArrayList<String>();
	public static HashSet<String> hashBomLevel;
	public static int expBOM;
	// public static List<WebElement> lstNoParent;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ImportWidget")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	// TASK : 53151
	@Test(description = "#. REMOVE DUPLICATE LINE ITEM IN IMPORT WIDGET HAVING THE SAME DATA IN MULTILEVEL BOM")
	public void multiLevelSamedataMain() throws Exception {
		headerFormat("#. CASE : REMOVE DUPLICATE LINE ITEM IN IMPORT WIDGET HAVING THE SAME DATA IN MULTILEVEL BOM");
		quoteChecker();
		importfile("BOM", "BOM_MultiLevel.xlsx");
		mappingScreen();
		IW_ImportValidation.validateError("BOM");
		boolean validate = false;
		boolean goToError = false;
		try {
			validate = driver.findElement(By.xpath(".//*[@id='btnValidateAndImport']")).isDisplayed();
			goToError = driver.findElement(By.xpath(".//*[@id='btnNavigateToError']")).isDisplayed();
		} catch (Exception e) {

		}
		if (goToError || validate) {
			IW_ImportVerification.validationclear();
			IW_ImportValidation.validateError("BOM");
		}
		IW_ImportVerification.verifyMultiLevelImportedItem();
		verifyMultilevelBOM();
		verifyQty();
		IW_ImportVerification.validationclear();
	}

	public void verifyQty() {
		log.info("~>> Verifying Qty Per");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
				CheckXpath("BOM_QTYPER_LABEL"));
		waitforseconds(3);
		String id = CheckXpath("BOM_QTYPER_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		List<WebElement> lstqtyPer = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		for (WebElement e : lstqtyPer) {
			int i = Integer.parseInt(e.getText());
			if (i > 0) {
				log.info("~>> RESULT :: QtyPer is > 0");
				System.out.println("~>> RESULT :: QtyPer is > 0");
				Assert.assertTrue(true);
			} else {
				log.info("~>> RESULT :: QtyPer is  < 0");
				System.out.println("~>> RESULT :: QtyPer is  < 0");
				Assert.assertTrue(false);
			}
		}
	}

	public static void getTotalsublevelBOM() {
		waitforseconds(8);
		boolean validate = false;
		try {
			validate = CheckXpath("VALIDATE_AND_IMPORT").isDisplayed();
		} catch (Exception e) {

		}
		if (validate) {
			log.info("~>> Checking total sublevel BOM");
			waitforseconds(3);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					CheckXpath("IW_SHEET_PARENT_LABEL"));
			waitforseconds(3);
			List<WebElement> lstPartClassTop = driver.findElements(By.xpath(
					".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='7'  and contains(@name,'col') and contains(text(),'Top')]"));

			List<WebElement> lstPartClassSub = driver.findElements(By.xpath(
					".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='7'  and contains(@name,'col') and contains(text(),'Sub')]"));

			expBOM = lstPartClassTop.size() + lstPartClassSub.size();
			log.info("~>> EXPECTED  TOTAL SUB LEVEL BOM  ::  " + expBOM);
			getparentpart();
		}
	}

	public static void getparentpart() {
		log.info("~>> Checking total sublevel BOM");
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
				parentPartarr.add(e.getText());
			}
		}
		hashBomLevel = new HashSet<String>(parentPartarr);
	}

	public static void verifyMultilevelBOM() throws Exception {
		System.out.println("~>> Current BOM :: " + CheckXpath("CURRENT_BOM_NAME").getText());
		log.info("~>> Current BOM :: " + CheckXpath("CURRENT_BOM_NAME").getText());
		String strcurrentBOM = CheckXpath("CURRENT_BOM_NAME").getText();
		boolean bomLevel = false;
		try {
			bomLevel = CheckXpath("BOM_LEVEL_BTN").isDisplayed();
		} catch (Exception e) {

		}
		if (bomLevel) {
			safeJavaScriptClick(CheckXpath("BOM_LEVEL_BTN"));
			waitforseconds(2);
			List<WebElement> lstbomLevel = driver.findElements(By.xpath(
					".//button[@class='dropdown-toggle btn-button' and contains(text(),'BOM Levels')]//following::ul[@class='ng-scope ng-isolate-scope']//li"));
			log.info("~>> ACTUAL TOTAL SUB LEVEL BOM  :: " + lstbomLevel.size());
			waitforseconds(2);
			safeJavaScriptClick(CheckXpath("BOM_LEVEL_BTN"));

			for (int i = 1; i <= lstbomLevel.size(); i++) {
				WebElement elebom = driver.findElement(By.xpath(
						".//button[@class='dropdown-toggle btn-button' and contains(text(),'BOM Levels')]//following::ul[@class='ng-scope ng-isolate-scope']//li["
								+ i + "]"));
				safeJavaScriptClick(CheckXpath("BOM_LEVEL_BTN"));
				waitforseconds(2);
				safeJavaScriptClick(elebom);
				// elebom.click();
				waitforseconds(3);
				log.info("~>> Current BOM :: " + CheckXpath("CURRENT_BOM_NAME").getText() + " > "
						+ CheckXpath("CURRENT_SUBLEVEL_BOM").getText());
				System.out.println("~>> Current BOM :: " + CheckXpath("CURRENT_BOM_NAME").getText() + " > "
						+ CheckXpath("CURRENT_SUBLEVEL_BOM").getText());

				IW_ImportVerification.verifyImportedLineitem();
				waitforseconds(3);
			}
			if (lstbomLevel.size() == expBOM) {
				Assert.assertTrue(true);
				log.info("~>> RESULT :: ACTUAL AND EXPECTED TOTAL BOM LEVELS ARE CORRECT ");
				System.out.println("~>> RESULT :: ACTUAL AND EXPECTED TOTAL BOM LEVELS ARE CORRECT");
			} else {
				Assert.assertTrue(false);
				log.info("~>> RESULT :: ACTUAL AND EXPECTED TOTAL BOM LEVELS ARE DIFFERENT");
				System.out.println("~>> RESULT :: ACTUAL AND EXPECTED TOTAL BOM LEVELS ARE DIFFERENT");
			}
		}
		waitforseconds(2);
		safeJavaScriptClick(CheckXpath("BOM_LEVEL_BTN"));
		waitforseconds(2);
		WebElement mainBOM = driver
				.findElement(By.xpath("(.//ul//li[contains(text(),'" + strcurrentBOM + "')]//i)[1]"));
		safeJavaScriptClick(mainBOM);
		waitforseconds(3);
	}
}
