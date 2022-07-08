package BOM;

import org.testng.annotations.Test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import utility.UtilityMethods;

public class BOM_BOMOperations extends BaseInitBOM {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BOM")) {
			// Code to check execution mode of Test Case
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	public void NavigateBOM(String AssemblyName) throws Exception {
		waitforseconds(2);
		RFQ_list();
		waitforseconds(4);
		CheckXpath("ASMNAME_FILER").clear();
		waitforseconds(2);
		CheckXpath("ASMNAME_FILER").sendKeys(AssemblyName);
		waitforseconds(2);
		first_RFQ();
		waitforseconds(5);
		log.info("~> Open First RFQ");
		checkLineItems();
	}

	@Test(description = "#. Happy Path :: QCQ - Copy Single level BOM")
	public void copyBOM_SingleLevel() throws Exception {
		NavigateBOM("DoNotTouch_AutoQuote_Name");
		CopyBOMFilter("DNT_AutoNormalQuote_Number", "04/17/2022", "04/24/2022");
		safeJavaScriptClick(CheckXpath("CB_SELECT_BOM_OKBUTTON"));
		waitforseconds(1);
		safeJavaScriptClick(CheckXpath("CB_SUBMIT_BUTTON"));
		waitforseconds(1);
		safeJavaScriptClick(CheckXpath("CB_OK_CONFIRM_POPUP"));
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		System.out.println("~>> Single level BOM copying successfully.");
		waitforseconds(10);
	}

	@Test(description = "#. Happy Path :: QCQ - Copy Multi-level BOM")
	public void copyBOM_MultiLevel() throws Exception {
		checkLineItems();
		CopyBOMFilter("DNT_AutoMultiLevelQuote_Number", "04/17/2022", "04/24/2022");
		safeJavaScriptClick(CheckXpath("CB_SELECT_BOM_OKBUTTON"));
		waitforseconds(1);
		safeJavaScriptClick(CheckXpath("CB_SUBMIT_BUTTON"));
		waitforseconds(1);
		safeJavaScriptClick(CheckXpath("CB_OK_CONFIRM_POPUP"));
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		System.out.println(">>> Top level BOM copying successfully.");
		waitforseconds(10);
		driver.navigate().refresh();
		waitforseconds(8);
		CheckBOMLevel();
	}

	public void CheckBOMLevel() throws Exception {
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
				System.out.println(CheckXpath("CURRENT_SUBLEVEL_BOM").getText() + "~>> level copying Successfully.");
				waitforseconds(3);
				int j = i + 1;
				CopyBOMFilter("DNT_AutoMultiLevelQuote_Number", "04/17/2022", "04/24/2022");
				WebElement SelectBOMRadioBTN = driver
						.findElement(By.xpath(".//label[@class=\"radio ng-binding ng-scope\"][" + j + "]"));
				waitforseconds(2);
				safeJavaScriptClick(SelectBOMRadioBTN);
				waitforseconds(2);
				safeJavaScriptClick(CheckXpath("CB_SELECT_BOM_OKBUTTON"));
				waitforseconds(1);
				safeJavaScriptClick(CheckXpath("CB_SUBMIT_BUTTON"));
				waitforseconds(1);
				safeJavaScriptClick(CheckXpath("CB_OK_CONFIRM_POPUP"));
				waitforseconds(3);
				safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
				waitforseconds(10);
				driver.navigate().refresh();
				waitforseconds(10);
			}
		}
	}

	@Test(description = "#. Happy Path :: QCQ - Copy Package Quote BOM")
	public void copyBOM_PackageQuote() throws Exception {
		NavigateBOM("AutoPackageQuote_AsmName");
		CopyBOMFilter("DNT_AutoPackageQuote_Number", "04/17/2022", "04/24/2022");
		safeJavaScriptClick(CheckXpath("CB_SELECT_BOM_OKBUTTON"));
		waitforseconds(1);
		safeJavaScriptClick(CheckXpath("CB_SUBMIT_BUTTON"));
		waitforseconds(1);
		safeJavaScriptClick(CheckXpath("CB_OK_CONFIRM_POPUP"));
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		waitforseconds(2);
		safeJavaScriptClick(CheckXpath("BETA_IMPORT_CONFIRM"));
		System.out.println("~>> Package BOM copying successfully.");
		waitforseconds(10);
	}
}
