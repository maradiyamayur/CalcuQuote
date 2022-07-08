package ShopCQ;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ShopCQ_POFilterList extends BaseInitshopCQ {

	WebElement eleFilter;
	private String pattern = "MM/dd/yyyy";
	String todaydate = new SimpleDateFormat(pattern).format(new Date());

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Parameters({ "correctusername", "correctpassword", })
	@Test(description = "#. HAPPY PATH :: SCQ - REVIEW FILTER")
	public void poFilter() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : PURCHASE ORDER FILTER LIST");
		System.out.println("CASE : PURCHASE ORDER FILTER LIST");
		removeFilter();
		waitforseconds(5);
//		demandList();
//		ShopCQ_POSent Ps = new ShopCQ_POSent();
//		Ps.poList();
		//driver.navigate().back();
		//waitforseconds(8);
		//filter("Date", "Status Date", 0, todaydate);
		// filter("Date", "PO Created Date", 0, todaydate);
		// filter("Date", "PO Sent Date", 0, todaydate);
		filter("Other", "Status", 0, "PO Sent");
		// filter("Other", "Status", 0, "Order Cancelled");
		// filter("Other", "Status", 0, "Order Acknowledged");
		// filter("Other", "Supplier", 0, "Mouser");
		// filter("Other", "Source", 0, "Manual");
		// filter("Other", "Supplier Contact", 0, "Payal");
		// filter("Other", "Lines", 0, "2");
		// filter("Other", "Total Cost", 0, "10000");
		// filter("Other", "PO Number", 0, CreatePO.PurchaseID);
	}

	public void filter(String type, String filtername, int filtertype, String input) throws InterruptedException {
		waitforseconds(8);
		log.info("..............................................................................................");
		System.out.println(
				"..............................................................................................");
		log.info("** FILTER :: " + filtername + " - " + input);
		System.out.println("** FILTER :: " + filtername + " - " + input);
		boolean filter = false;
		try {
			filter = driver
					.findElement(By.xpath(".//span[@class='ui-grid-header-cell-label ng-binding' and text()='"
							+ filtername + "']//following::div[@class='ui-grid-filter-container ng-scope'][1]//input"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (filter) {
			log.info("~>> Filter Present");
			waitforseconds(8);
			eleFilter = driver.findElement(By.xpath(".//span[@class='ui-grid-header-cell-label ng-binding' and text()='"
					+ filtername + "']//following::div[@class='ui-grid-filter-container ng-scope'][1]//input"));
			if (type.equals("Date")) {
				waitforseconds(5);
				eleFilter.clear();
				waitforseconds(3);
				eleFilter.sendKeys(todaydate);
				waitforseconds(10);
				filterVerification(filtertype, filtername, input);
				eleFilter.clear();
			} else if (type.equals("Other")) {
				waitforseconds(5);
				eleFilter.clear();
				eleFilter.sendKeys(input);
				waitforseconds(8);
				filterVerification(filtertype, filtername, input);
				eleFilter.clear();
			}
		} else {
			log.info("~>> Filter Absent");
		}
	}

	public void filterVerification(int filtertype, String filtername, String input) throws InterruptedException {
		boolean norecord = false;
		try {
			norecord = driver.findElement(By.xpath("(.//i[@class='ng-scope ui-grid-icon-plus-squared'])[1]"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (norecord) {
			if (filtertype == 0) {
				WebElement filterHeader = driver.findElement(By.xpath(
						".//span[@class='ui-grid-header-cell-label ng-binding' and text()='" + filtername + "']"));
				String id = filterHeader.getAttribute("id").replaceAll("-header-text", "");
				String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
				waitforseconds(5);
				// log.info(id);
				// log.info(id1);
				List<WebElement> lstresult = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
				log.info("~>> Total Records found :: " + lstresult.size());
				System.out.println("~>> Total Records found :: " + lstresult.size());
				for (WebElement result : lstresult) {
					if (result.getAttribute("innerText").replaceAll("[-+.^:,]", "").contains(input)) {
						log.info("~ >> RESULT ::  FILTERED RECORD IS CORRECT");
						System.out.println("~>> RESULT ::  FILTERED RECORD IS CORRECT");
					} else {
						log.info("~>> RESULT ::  FILTERED RECORD IS NOT CORRECT");
						System.out.println("~>> RESULT ::  FILTERED RECORD IS NOT CORRECT");
					}
					waitforseconds(5);
				}
			}
		} else {
			log.info("~>> RESULT :: NO DETAILS TO DISPLAY FOR THE CURRENT SELECTION");
			System.out.println("~>> RESULT :: NO DETAILS TO DISPLAY FOR THE CURRENT SELECTION");
		}
	}
}
