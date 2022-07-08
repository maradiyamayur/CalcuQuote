package ShopCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ShopCQ_POSent extends BaseInitshopCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - PURCHASE ORDER SENT")
	public void poSent() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		waitforseconds(5);
		log.info("CASE : PURCHASE ORDER SENT");
		System.out.println("CASE : PURCHASE ORDER SENT");
		poList();
		poEdit("SEND_PO_OPTIONS");
	}

	public void poList() throws Exception {
		boolean polist = false;
		try {
			polist = driver.findElement(By.xpath(".//*[@id='content']//span[contains(text(),'Purchase Order List')]"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (polist) {
			log.info("~>> PO List is open");
		} else {
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("DL_LEFT_MENU"));
			log.info("~>> Clicked on Left Menu");
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("DL_PO_LIST"));
			log.info("~>> Clicked on PO List");
			waitforseconds(8);
			//gridmenu();
			//waitforseconds(5);
			removeFilter();
			waitforseconds(5);
		}
	}

	public void poEdit(String Export) throws Exception {
		removeFilter();
		driver.findElement(By.xpath(".//input[@id='StatusDate']")).clear();
		waitforseconds(10);
		try {
			if (ShopCQ_CreatePO.PurchaseID.contains("New PO")) {
				CheckXpath("PO_STATUS_FILTER").clear();
				CheckXpath("PO_STATUS_FILTER").sendKeys("create");
				waitforseconds(10);
				safeJavaScriptClick(CheckXpath("PO_STATUS_DATE_FILTER"));
				driver.findElement(By.xpath("//ul[@role='presentation']//button[text()='Today']")).click();
				waitforseconds(8);
				String id = CheckXpath("PO_NUMBER_LABEL").getAttribute("id").replaceAll("-header-text", "");
				String id1 = (id.split("-")[1]).concat("-").concat(id.split("-")[2]);
				String PO = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div//span)[1]"))
						.getText();
				ShopCQ_CreatePO.PurchaseID = PO;
			} else {
				CheckXpath("PO_NUMBER_FILTER").clear();
				CheckXpath("PO_NUMBER_FILTER").sendKeys(ShopCQ_CreatePO.PurchaseID);
				log.info("~>> Searching done for Purchase Order Number :: " + ShopCQ_CreatePO.PurchaseID);
			}
			waitforseconds(20);
			boolean poEdit = false;
			try {
				poEdit = CheckXpath("PO_EDIT").isDisplayed();
			} catch (Exception e) {

			}
			if (poEdit) {
				log.info("~>> Searched PO found");
				safeJavaScriptClick(CheckXpath("PO_EDIT"));
				waitforseconds(5);
				boolean pobtn = false;
				try {
					pobtn = CheckXpath("SEND_PO_BTN").isDisplayed();
				} catch (Exception e) {

				}
				if (pobtn) {
					waitforseconds(8);
					safeJavaScriptClick(CheckXpath("SEND_PO_BTN"));
					log.info("~>> Send Purchase Order Button Clicked");
					updatePO(Export);
					verification();
				} else {
					log.info("~>> Send Purchase Order button not displayed");
				}
			} else {
				log.info("~>> Searched PO does not exits");
				System.out.println("~>> Searched PO does not exists");
			}
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updatePO(String Export) throws Exception {
		waitforseconds(5);
		if (driver.findElements(By.xpath(".//*[@id='SupplierContactId']//option")).size() > 1) {
			new Select(CheckXpath("UPOI_SUPPLIER_CONTACT")).selectByIndex(1);
		} else {
			log.info("~>> Adding new Supplier Contact");
			safeJavaScriptClick(CheckXpath("SCQ_SUPPLIER_NEW"));
			safeJavaScriptClick(CheckXpath("SCQ_SUPPLIER_NEW_COMPANY"));
			safeJavaScriptClick(CheckXpath("SCQ_SUPPLIER_NEW_COMPANY_SELECTALL"));
			CheckXpath("SCQ_SUPPLIER_NEW_EMAIL").sendKeys("shreyas.hadvani@triveniglobalsoft.com");
			CheckXpath("SCQ_SUPPLIER_NEW_COMPANY_NAME").sendKeys("System.currentTimeMillis()");
			safeJavaScriptClick(CheckXpath("SCQ_SUPPLIER_NEW_SAVE"));
			waitforseconds(5);
		}
		new Select(CheckXpath("UPOI_TERMS")).selectByIndex(1);
		new Select(CheckXpath("UPOI_SHIP_ADDRESS")).selectByIndex(1);
		CheckXpath("UPOI_ATTENTION").clear();
		CheckXpath("UPOI_ATTENTION").sendKeys("shreyas.hadvani@triveniglobalsoft.com");
		waitforseconds(3);
		new Select(CheckXpath("UPOI_SHIP_METHOD")).selectByIndex(1);
		CheckXpath("UPOI_FOB").clear();
		CheckXpath("UPOI_FOB").sendKeys("CASH");
		CheckXpath("UPOI_SUPPLIER_NOTE").sendKeys("Auto Supplier Notes");
		CheckXpath("UPOI_PO_NOTE").sendKeys("Auto Purchase Order Notes");
		waitforseconds(5);
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("UPOI_SEND_BTN"));
		log.info("~>> Update Purchase Order Information send button clicked");
		waitforseconds(5);
		WebElement export = CheckXpath(Export);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", export);
		log.info("~>> Export option clicked");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("SEND_PO_SUBMIT"));
		log.info("~>> Send PO Submitted");
		alertacceptpopup();
		log.info("~>> Update Purchase Order Information Submitted");
		System.out.println("~>> Update Purchase Order Information Submitted");
	}

	public void verification() throws Exception {
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("PURCHASE_ORDER_BACK"));
		log.info("~>> Back to Purchase Order List");
		waitforseconds(8);
		removeFilter();
		CheckXpath("PO_NUMBER_FILTER").clear();
		CheckXpath("PO_NUMBER_FILTER").sendKeys(ShopCQ_CreatePO.PurchaseID);
		waitforseconds(3);
		String id = CheckXpath("PO_STATUS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-0-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		waitforseconds(5);

		String Strstatus = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div")).getText();
		if (Strstatus.contains("PO Sent")) {
			log.info("~>> RESULT :: Purchase Order Sent Successfully");
			System.out.println("~>> RESULT :: Purchase Order Sent Successfully");
		} else {
			log.info("~>> RESULT :: Purchase Order not Sent Successfully");
			System.out.println("~>> RESULT :: Purchase Order not Sent Successfully");
		}
	}
}
