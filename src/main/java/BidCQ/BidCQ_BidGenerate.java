package BidCQ;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import QuoteCQ.BaseInitquoteCQ;
import QuoteCQ.QuoteCQ_ImportBOM;
import utility.UtilityMethods;

public class BidCQ_BidGenerate extends BaseInitbidCQ {
	String filename;
	static String quoteId;
	protected static List<WebElement> liunitPr;
	static String[] exparr;
	static String expMPNele;
	protected static String supplierName;

	QuoteCQ_ImportBOM IB = new QuoteCQ_ImportBOM();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. HAPPY PATH :: BCQ - BID GENERATION AND SUBMIT")
	public void generate() throws Exception {
		headerFormat("CASE : BID GENERATION AND SUBMIT");
		checkstatus("SELECT_ALL_LINES");
		addtobid(1);
	}

//	@Test(priority = 2, description = "#. HAPPY PATH :: BCQ - VERIFY ALTERNATE PART ADDED FROM BIDCQ")
//	public void altenatePart() throws Exception {
//		headerFormat("#.CASE : VERIFY ALTERNATE PART ADDED FROM BIDCQ");
//		log.info("~>> Verifying Alternate Mfgr and MPN");
//		waitforseconds(5);
//		String id = CheckXpath("MATERIAL_MPN_LABEL").getAttribute("id").replaceAll("-header-text", "");
//		String id1 = id.substring(id.indexOf("-"), id.length());
//		WebElement alternatePart = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1
//				+ "')]//div//div[contains(@class,'ng-binding ng-scope alternatePart')])[1]"));
//		if (BidCQ_BidGenerate.expMPNele.equals(alternatePart.getText())) {
//			log.info("~>> RESULT :: MATCHED");
//			System.out.println("~>> RESULT ::ALTERNATE PART MATCHED");
//		} else {
//			log.info("~>> RESULT :: NOT MATCHED");
//			System.out.println("~>> RESULT :: ALTERNATE PART NOT MATCHED");
//		}
//	}

	public void checkstatus(String lines) throws Exception {
		waitforseconds(25);
		System.out.println("~>> IMPORT BOM");
		log.info("~>> IMPORT BOM");
		BaseInitquoteCQ.RFQ_list();
		IB.openQuote();
		BaseInitquoteCQ.first_RFQ();
		importMain();
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		waitforseconds(50);
		
		//waitforseconds(12);
		RFQ_ID = CheckXpath("BID_RFQ_ID").getText();
		ASM_ID = CheckXpath("BID_ASM_ID").getText();
		ASM_NUMBER = CheckXpath("BID_ASM_NUMBER").getText();
		log.info("~>> RFQ ID :: " + RFQ_ID);
		log.info("~>> Assembly ID :: " + ASM_ID);
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath(lines));
		log.info("~>> Lines selected");
	}

	public void importMain() throws Exception {
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("BOM_TAB"));
		boolean importBOM = false;
		try {
			importBOM = CheckXpath("IMPORT_BOM").isDisplayed();
		} catch (Exception e) {

		}
		if (importBOM) {
			IB.import_Steps();
			IB.import_upload("5LineBOM.xlsx");
		} else {
			waitforseconds(8);
			log.info("~>> BOM line items are already available");
			safeJavaScriptClick(CheckXpath("BOM_ACTIONS_BUTTON"));
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("DELETE_BOM"));
			waitforseconds(8);
			alertacceptpopup();
			waitforseconds(5);
			log.info("~>> BOM Deleted Successfully");
			driver.navigate().refresh();
			waitforseconds(8);
			boolean spinner = false;
			try {
				spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
			} catch (Exception e) {

			}
			if (spinner) {
				driver.navigate().refresh();
				waitforseconds(15);
			}
			IB.import_Steps();
			IB.import_upload("5LineBOM.xlsx");
		}
		waitforseconds(30);
		IB.submitBOM();
		IB.chkLaborupdate();
		waitforseconds(15);
	}

	public void addtobid(int type) throws Exception {
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MC_EDIT_BTN"));
		log.info("~>> Edit button clicked");
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("ADD_TO_BIDSHEET"));
		log.info("~>> Add to Bidsheet clicked");
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("BID_SUPPLIER_CONTACT"));
		waitforseconds(10);
		CheckXpath("BID_SELECT_SUPPLIER").sendKeys(UtilityMethods.Username);
		waitforseconds(8);
		boolean supplier = false;
		try {
			supplier = CheckXpath("SELECT_SUPPLIER").isDisplayed();
		} catch (Exception e) {

		}
		if (supplier) {
			bidgen(type);
		} else {
			log.info("~>> No supplier to select for selected User");
			System.out.println("~>> No supplier to select for selected User");
			addSupplier();
			bidgen(type);
		}
	}

	public void bidgen(int type) throws Exception {
		log.info(CheckXpath("SELECT_SUPPLIER_NAME").getText());
		String StrsupplierName = CheckXpath("SELECT_SUPPLIER_NAME").getText();
		supplierName = StrsupplierName.substring(StrsupplierName.indexOf("-", 1) + 1);
		// supplierName = StrsupplierName.split("-")[1];
		CheckXpath("SELECT_SUPPLIER").click();
		log.info("~>> Selected Supplier Name :: " + supplierName);
		log.info("~>> Supplier Selected  :: " + CheckXpath("SELECT_SUPPLIER").getText());
		driver.findElement(By.xpath(".//*[@id='bidSupplierContact']/label[1]/strong")).click();
		safeJavaScriptClick(CheckXpath("BID_REQUEST_SAVE"));
		log.info("~>> RESULT :: BID CREATED SUCCESSFULLY");
		System.out.println("~>> RESULT :: BID CREATED SUCCESSFULLY");
		waitforseconds(8);
		getstatus("Send", "rgb(240, 102, 115)", "RED", "SEND");
		generatelink();
		if (type == 1) {
			generatelink();
			bidsheet(100, 5020, "Panasonic");
			verifyStatus(1);
			waitforseconds(5);
			switchToNewtabWithUrl(driver, 0);
			waitforseconds(5);
			driver.navigate().refresh();
			waitforseconds(5);
			getstatus("Send", "rgb(0, 150, 136)", "GREEN", "SUBMITTED");
			waitforseconds(5);
		}
	}

	public void getstatus(String status, String color, String colorname, String statusName) {
		boolean suppliercarousel = false;
		try {
			suppliercarousel = CheckXpath("SUPPLIER_CAROUSEL").isDisplayed();
		} catch (Exception e) {

		}
		if (suppliercarousel) {
			log.info("~>> Supplier Displayed");
			String actstatus = CheckXpath("SUPPLIER_CAROUSEL").getText();
			String actcolor = CheckXpath("SUPPLIER_CAROUSEL_COLOR").getAttribute("style");
			if (actstatus.contains(status) && actcolor.contains(color)) {
				log.info("~>> SUPPLIER STATUS  COLOR CHANGED TO :: " + colorname + " & STATUS CHANGED TO :: "
						+ statusName);
				System.out.println("~>> SUPPLIER STATUS COLOR CHANGED TO :: " + colorname + " & STATUS CHANGED TO :: "
						+ statusName);

			} else {
				log.info("~>> SUPPLIER STATUS  COLOR :: " + colorname + " & STATUS NOT CHANGED TO :: " + statusName);
				System.out.println(
						"~>> SUPPLIER STATUS  COLOR :: " + colorname + " & STATUS NOT CHANGED TO :: " + statusName);

			}
		} else {
			log.info("~>> No Supplier Displayed");
		}
	}

	public void generatelink() throws Exception {
		waitforseconds(8);
		boolean suppliercarousel = false;
		try {
			suppliercarousel = CheckXpath("SUPPLIER_CAROUSEL").isDisplayed();
		} catch (Exception e) {

		}
		if (suppliercarousel) {
			safeJavaScriptClick(CheckXpath("SUPPLIER_DROPDOWN"));
			safeJavaScriptClick(CheckXpath("SUPPPLIER_GENERATE_LINK"));
			log.info("~>> Link Generated");
			waitforseconds(3);
			log.info("~>> Link Copied");
			waitforseconds(15);
			WebElement link1 = CheckXpath("COPY_BIDCQ_LINK");
			link1.click();
			link1.sendKeys(Keys.CONTROL + "c");
			waitforseconds(15);
			String URL = getSysClipboardText();
			waitforseconds(5);
			log.info("~>> URL : " + URL);
			safeJavaScriptClick(CheckXpath("BIDCQ_CLOSE_BTN"));
			waitforseconds(5);
			getstatus("Send", "rgb(89, 168, 255)", "BLUE", "SEND");
			waitforseconds(5);
			switchToNewtabWithUrl(driver, 1);
			driver.get(URL);
			log.info("~>> Redirected to BidSheet");
			System.out.println("~>> Redirected to BidSheet");
		}
	}

	public static String getSysClipboardText() {
		String ret = "";
		java.awt.datatransfer.Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipTf = sysClip.getContents(null);
		if (clipTf != null) {
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public int getIndex(String headername) {
		int index = 0;
		List<WebElement> columns = driver.findElements(By
				.xpath(".//div[@class='ht_clone_top handsontable']//div[@class='relative']//span[@class='colHeader']"));
		try {
			for (int i = 0; i <= columns.size(); i++) {
				if (columns.get(i).getText().equals(headername)) {
					index = i + 1;
					// System.out.println(index);
				}
			}

		} catch (IndexOutOfBoundsException error) {
			// System.out.println(error.getMessage());
		}
		return index;
	}

	public void bidsheet(int intprice, int intMPN, String strMfgr) throws Exception {
		waitforseconds(10);
		driver.navigate().refresh();
		alertacceptpopup();
		waitforseconds(10);
		// wait("visible", "BID_INFO");
		boolean bidinfo = false;
		try {
			bidinfo = driver
					.findElement(By.xpath(".//div[@class='modal-header HeaderText']//span[@ng-click='closeDrower()']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (bidinfo) {
			log.info("~>> Bid Information popup displayed");
			System.out.println("~>> Bid Information popup displayed");
			safeJavaScriptClick(CheckXpath("BID_INFO"));
			// CheckXpath("BID_INFO").click();
		} else {
			log.info("~>> Bid Information popup not displayed");
			System.out.println("~>> Bid Information popup not displayed");
		}
		// waitforseconds(20);
		waitforseconds(10);
		liunitPr = driver.findElements(By.xpath(".//table[@class='htCore']//tbody//tr//td[1]"));
		log.info("~>> Total Line Items :: " + liunitPr.size());
		System.out.println("~>> Total Line Items :: " + liunitPr.size());
		exparr = new String[liunitPr.size() - 1];
		waitforseconds(5);

		// code for update
		// WebElement lst =
		// driver.findElement(By.xpath(".//*[@id='sub-header']/div[1]/row/div[1]/a/u"));
		// System.out.println("1 - " + lst.getText());
		driver.findElement(By.xpath(".//*[@id='header']//span[@id='logo']")).click();
		waitforseconds(3);

		String strprice = String.valueOf(intprice);
		String strMPN = String.valueOf(intMPN);
		lineItem = driver.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[1]")).getText();
		// int intMin = getIndex("Min");
		// WebElement Min =
		// driver.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[" +
		// intMin + "]"));
		// Min.click();
		waitforseconds(3);
		int intunit = getIndex("Unit Price");
		WebElement Unitprice = driver
				.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[" + intunit + "]"));
		// updatebid(Unitprice, strprice);
		updatebid(Unitprice, strprice);
		waitforseconds(5);
		savedraft();
		int intAltMPN = getIndex("Cross Match MPN");
		WebElement MPN = driver.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[" + intAltMPN + "]"));
		// updatebid(MPN, strMPN);
		updatebid(MPN, strMPN);
		int intmfgr = getIndex("Cross Match Mfgr");
		WebElement Mfrgr = driver.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[" + intmfgr + "]"));
		// updatebid(Mfrgr, strMfgr);
		updatebid(Mfrgr, strMfgr);

		expMPNele = "Panasonic | " + strMPN;
		System.out.println("~>> Updated MPN :: " + expMPNele);

		waitforseconds(8);

		safeJavaScriptClick(CheckXpath("BID_SHEET_SUBMIT"));
		log.info("~>> Clicked on Submit button");
		System.out.println("~>> Bid Submitted");
		waitforseconds(10);
		driver.switchTo().activeElement();
		CheckXpath("BID_SHEET_AUTHORIZED_BY").sendKeys("Payal Nanavati");
		quoteId = autoId();
		log.info("~>> Quote ID :: " + quoteId);
		CheckXpath("BID_SHEET_QUOTE_NO").sendKeys(quoteId);
		safeJavaScriptClick(CheckXpath("BID_SHEET_SUBMIT_BTN"));
		log.info("~>> Your BidSheet Submitted");
		System.out.println("~>> Your BidSheet Submitted");
		alertacceptpopup();
	}

	public void updatebid(WebElement locator, String input) {
		Actions actions = new Actions(driver);
		waitforseconds(2);
		// System.out.println("before Input Locator - " + locator.getText());
		actions.moveToElement(locator).doubleClick().perform();
		waitforseconds(3);
		for (int i = 1; i <= 15; i++) {
			actions.sendKeys(Keys.BACK_SPACE);
		}
		actions.sendKeys(input).perform();
		waitforseconds(2);
		driver.findElement(By.xpath(".//*[@id='header']")).click();
		waitforseconds(3);
		// System.out.println("WebElement Name : " + locator);
		// System.out.println("After Input Locator - " + locator.getText() + "=" +
		// input);
	}

	public void savedraft() {
		boolean saveDraft = false;
		try {
			saveDraft = driver.findElement(By.xpath(".//*[@id='sub-header']//a[contains(text(),'Save As Draft')]"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (saveDraft) {
			driver.findElement(By.xpath(".//*[@id='sub-header']//a[contains(text(),'Save As Draft')]")).click();
			waitforseconds(5);
			System.out.println("~>> BID SAVED AS DRAFT");
		} else {
			System.out.println("~>> BID NOT SAVED AS DRAFT");
		}
	}

	public void verifyStatus(int type) throws Exception {
		waitforseconds(20);
		System.out.println("~>> Verifing Bid Status");
		waitforseconds(5);
		if (type == 1) {
			safeJavaScriptClick(CheckXpath("BID_SUMMARY_CLOSED"));
			log.info("~>> Clicked on Closed Bid");
			waitforseconds(5);
		}
		String id = CheckXpath("BID_QUOTENO_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		// log.info(id);
		// log.info(id1);
		WebElement quoteidfilter = driver.findElement(
				By.xpath(".//div[contains(@id,'" + id1 + "')]//following-sibling::div//div[@class='ng-scope']//input"));
		quoteidfilter.clear();
		waitforseconds(5);
		log.info("~>> Searching with QuoteID");
		quoteidfilter.sendKeys(quoteId);
		waitforseconds(5);
		boolean status = false;
		try {
			status = CheckXpath("BID_STATUS_TAG").isDisplayed();
		} catch (Exception e) {

		}
		if (status) {
			if (CheckXpath("BID_STATUS_TAG").getText().contains("Submitted")) {
				log.info(CheckXpath("BID_STATUS_TAG").getText());
				System.out.println("~>> RESULT :: BID SUBMITTED");
				log.info("~>> RESULT :: BID SUBMITTED");
			} else {
				log.info(CheckXpath("BID_STATUS_TAG").getText());
				System.out.println("~>> RESULT :: BID NOT SUBMITTED");
				log.info("~>> RESULT :: BID NOT SUBMITTED");
			}
		}
	}

	public void addSupplier() throws Exception {
		safeJavaScriptClick(CheckXpath("BID_ADD_CONTACT"));
		log.info("~>> Add Contact Clicked");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BID_ADD_SUP_BTN"));
		log.info("~>> Add Supplier Clicked");
		String supName = UtilityMethods.Username.split("@")[0];
		CheckXpath("BID_SUP_NAME").sendKeys("Test-" + supName);
		safeJavaScriptClick(CheckXpath("BID_SUP_SAVE_BTN"));
		log.info("~>> Save button clicked");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_SUP_CONTACT"));
		log.info("~>> Bid Supplier contact cliked");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_SELECT_COMPANY"));
		log.info("~>> Compay selected");
		safeJavaScriptClick(CheckXpath("BID_SELECT_COMPANY_ALL"));
		log.info("~>> Checked ALl");
		waitforseconds(3);
		CheckXpath("BID_COMPANY_CONTACT_NAME").sendKeys(supName);
		log.info("Company contact name added");
		CheckXpath("BID_COMPANY_CONTACT_EMAIL").sendKeys(UtilityMethods.Username);
		log.info("~>> Company email added");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_SUP_SAVE_BTN"));
		log.info("~>> Save button clicked");
		waitforseconds(5);
		driver.findElement(By.xpath("(.//button[@ng-click='cancel()'])[1]")).click();
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("BID_SUPPLIER_CONTACT"));
		waitforseconds(3);
	}

}