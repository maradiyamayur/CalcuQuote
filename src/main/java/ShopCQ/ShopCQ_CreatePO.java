package ShopCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ShopCQ_CreatePO extends BaseInitshopCQ {

	public static String PurchaseID;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Parameters({ "correctusername", "correctpassword", })
	@Test(description = "#. HAPPY PATH :: SCQ - CREATE PO")
	public void createPO() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : CREATE PO");
		System.out.println("CASE : CREATE PO");
		waitforseconds(8);
		//demandList();
		removeFilter();
		waitforseconds(5);
		checkRecord(0, "", 0);
		log.info("----------------------------------------------------------------------------");
	}

	public void checkRecord(int type, String supplier, int number) throws Exception {
		waitforseconds(8);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft -=2000", scrollbar);
		waitforseconds(5);
		if (type == 0) {
			CheckXpath("DL_STATUS_FILTER").sendKeys("Supplier Selected");
			log.info("~>> Supplier selected filter applied");
			waitforseconds(5);
			CheckXpath("DL_TAGS_FILTER").sendKeys("shreyas");
		}
		if (type == 1) {
			CheckXpath("DL_STATUS_FILTER").sendKeys("Supplier Selected");
			log.info("~>> Supplier selected filter applied");
			waitforseconds(5);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft +=1000", scrollbar);
			waitforseconds(5);
			CheckXpath("DL_SUPPLIER_FILTER").sendKeys(supplier);
			waitforseconds(8);
		}
		waitforseconds(10);
		Boolean records = false;
		try {
			records = driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).isDisplayed();
		} catch (Exception e) {

		}
		if (records) {
			addPO(number);
		} else {
			driver.findElement(
					By.xpath("(.//div[@class='ui-grid-filter-button ng-scope']//i[@class='ui-grid-icon-cancel'])[2]"))
					.click();
			waitforseconds(8);
			addPO(number);
		}
	}

	public String addPO(int number) throws Exception {
		if (number == 0) {
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).click();
		}
		if (number == 1) {
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[2]")).click();
			waitforseconds(3);
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents']//div)[3]")).click();
		}
		log.info("~>> Record selected");
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("DL_EDIT_BTN"));
		log.info("~>> Edit button clicked");
		safeJavaScriptClick(CheckXpath("DL_EDIT_ADD_PO"));
		log.info("~>> Add to PO clicked");
		alertacceptpopup();
		waitforseconds(5);
		try {
			PurchaseID = CheckXpath("PO_ID").getText();
		} catch (NullPointerException e) {
			log.info(e.getMessage());
		}
		safeJavaScriptClick(CheckXpath("PO_SUBMIT"));
		log.info("~>> Submit Button clicked");
		log.info("~>> RESULT :: PO CREATED SUCCESSFULLY - " + PurchaseID);
		System.out.println("~>> RESULT :: PO CREATED SUCCESSFULLY - " + PurchaseID);
		waitforseconds(5);
		return PurchaseID;
	}
}
