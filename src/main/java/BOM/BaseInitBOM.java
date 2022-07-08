package BOM;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import parentclass.BaseInit;

public class BaseInitBOM extends BaseInit {

	public static String Add_QuoteID, Add_Asm_name, Add_Asm_no, Add_job, Add_order, Add_Customer, asm_Revision;

	/*
	 * @BeforeSuite public void checkExecutionModeTestSuite() throws IOException {
	 * startup(); // Code to check execution mode of Test Suite if
	 * (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "BOM", "TestSuite"))
	 * { throw new SkipException("Execution mode of the test suite is set to NO"); }
	 * }
	 */

	public static void quoteOpen() throws Exception {
		boolean quote = false;
		try {
			quote = driver.findElement(By.xpath(".//*[@id='HeaderShow']")).isDisplayed();
		} catch (Exception e) {

		}
		if (quote) {
			log.info("~>> Quote is already open for operations");
		} else {
			RFQ_list();
			first_RFQ();
			Thread.sleep(8000);
		}
	}

	public static void RFQ_list() throws Exception {
		log.info("~>> Click on RFQ List icon");
		WebElement RFQ_List = CheckXpath("RFQ_LIST");
		/*
		 * JavascriptExecutor executor = (JavascriptExecutor) driver;
		 * executor.executeScript("arguments[0].click();", RFQ_List);
		 */
		safeJavaScriptClick(RFQ_List);
		Thread.sleep(5000);
	}

	public static void first_RFQ() throws Exception {
		Thread.sleep(5000);
		log.info("~>> Select RFQ");
		WebElement first_Quote = driver
				.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope']//a)[3]"));
		log.info("~>> Assembly Id  :: " + first_Quote.getAttribute("innerText"));
		Thread.sleep(10000);
		// driver.findElement(By.linkText(first_Quote.getAttribute("innerText"))).click();
		WebElement first = driver.findElement(By.linkText(first_Quote.getAttribute("innerText")));
		safeJavaScriptClick(first);
	}

	public void checkLineItems() throws Exception {
		boolean importBOM = false;
		try {
			importBOM = CheckXpath("IMPORT_BOM").isDisplayed();
		} catch (Exception e) {

		}
		if (importBOM) {
			log.info("~>> BOM line items are not available");
		} else {
			log.info("~>> BOM line items are already available");
			safeJavaScriptClick(CheckXpath("BOM_ACTIONS_BUTTON"));
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("DELETE_BOM"));
			waitforseconds(5);
			alertacceptpopup();
			waitforseconds(5);
			log.info("~>> BOM Deleted Successfully");
			driver.navigate().refresh();
			waitforseconds(12);
		}
	}

	public void CheckBOMExistOrNot() throws Exception {
		safeJavaScriptClick(CheckXpath("CB_SELECT_ASSEMBLY"));
		waitforseconds(2);
		boolean SelectBOMRadioButton = false;
		try {
			SelectBOMRadioButton = CheckXpath("CB_SELECT_BOM_FIRST_RADIO").isDisplayed();
		} catch (Exception e) {

		}
		if (SelectBOMRadioButton) {
			log.info("~>> BOM is available");
		} else {
			log.info("~>> BOM is not available");
		}
	}

	public void CopyBOMFilter(String asmnumber, String FQuoteDate, String TQuoteDate) throws Exception {
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BOM_ACTIONS_BUTTON"));
		waitforseconds(1);
		log.info("~> Copy BOM");
		safeJavaScriptClick(CheckXpath("COPY_BOM"));
		waitforseconds(3);
		CheckXpath("CB_ASM_NUMBER").clear();
		CheckXpath("CB_ASM_NUMBER").sendKeys(asmnumber);
		CheckXpath("CB_FROM_QUOTE_INDATE").clear();
		CheckXpath("CB_FROM_QUOTE_INDATE").sendKeys(FQuoteDate);
		CheckXpath("CB_TO_QUOTE_INDATE").clear();
		CheckXpath("CB_TO_QUOTE_INDATE").sendKeys(TQuoteDate);
		safeJavaScriptClick(CheckXpath("COPY_BOM_SEARCH_ASSEMBLY"));
		waitforseconds(2);
		log.info("~> Get Assembly List");
		CBPleaseSelectAssembly();
		CheckBOMExistOrNot();
	}

	public void CBPleaseSelectAssembly() throws Exception {
		safeJavaScriptClick(CheckXpath("CB_SUBMIT_BUTTON"));
		boolean PleaseSelectAssembly = false;
		try {
			PleaseSelectAssembly = CheckXpath("CB_PLEASE_SELECT_ASSEMBLY_POPUP").isDisplayed();
		} catch (Exception e) {

		}
		if (PleaseSelectAssembly) {
			log.info("~>> Please Select Assembly");
			waitforseconds(2);
			safeJavaScriptClick(CheckXpath("CB_PLEASE_SELECT_ASSEMBLY_OK_BTN"));
		} else {
			log.info("~>> There is not any assembly");
		}
	}
}
