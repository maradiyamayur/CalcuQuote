package BidCQ;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BidCQ_Filter extends BaseInitbidCQ {

	WebElement filter;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. BIDCQ SUMMARY FILTER ")
	public void filter() throws Exception {
		headerFormat("#. BIDCQ SUMMARY FILTER");
		if (getHost()[0].contains("app.calcuquote.com")) {
			driver.get("https://trial.calcuquote.com/#/Dashboard");
		} else {
			driver.get("https://" + getHost()[0] + "/BidCQIdentity/#/");
		}
	}

	@Test(priority = 2, description = "#. BIDCQ SUMMARY FILTER FOR OPEN BID : ID")
	public void filterSummary1() throws Exception {
		existFilter();
		summaryFilter(1, "ID", "BID_ID_LABEL");
	}

	@Test(priority = 3, description = "#. BIDCQ SUMMARY FILTER FOR OPEN BID : QUOTE NO")
	public void filterSummary2() throws Exception {
		existFilter();
		summaryFilter(1, "QUOTE NO", "BID_QUOTENO_LABEL");
	}

	@Test(priority = 4, description = "#. BIDCQ SUMMARY FILTER FOR OPEN BID : COMPANY")
	public void filterSummary3() throws Exception {
		existFilter();
		summaryFilter(1, "Company", "BID_COMPANY_LABEL");
	}

	@Test(priority = 5, description = "#. BIDCQ SUMMARY FILTER FOR OPEN BID  : DUEDATE")
	public void filterSummary4() throws Exception {
		existFilter();
		summaryFilter(2, "Due Date", "BID_DUEDATE_LABEL");
		CheckXpath("BID_DATE_FILTER").clear();
	}

	@Test(priority = 6, description = "#. BIDCQ SUMMARY FILTER FOR OPEN BID : QUOTE-ASSEMBLY")
	public void filterSummary5() throws Exception {
		existFilter();
		summaryFilter(1, "Quote - Assembly", "BID_QUOTEASM_LABEL");
	}

	@Test(priority = 7, description = "#. BIDCQ FILTER FOR OPEN BID : STAUS")
	public void filterSide1() throws Exception {
		existFilter();
		sideFilterStatus(2, CheckXpath("BID_SUMMARY_OPEN"));
	}

	@Test(priority = 8, description = "#. BIDCQ  FILTER : NO OF BIDS  FOR OPEN BID")
	public void filterSide2() throws Exception {
		sideFilterSorting(2, CheckXpath("FILTER_BIDS"), ".//div[contains(@class,'col-sm-5')]");
	}

	@Test(priority = 9, description = "#. BIDCQ  FILTER : NO OF LINES  FOR OPEN BID")
	public void filterSide3() throws Exception {
		sideFilterSorting(3, CheckXpath("FILTER_LINES"), ".//div[contains(@class,'col-sm-5')]");
	}

	@Test(priority = 10, description = "#. BIDCQ  FILTER ALPHABETICAL FOR OPEN BID")
	public void filterSide4() throws Exception {
		sideFilterSorting(1, CheckXpath("FILTER_ALPHABETICAL"), ".//div[contains(@class,'sub-header-normal col')]");
	}

	@Test(priority = 11, description = "#. BIDCQ  FILTER FOR CLOSED BID : STATUS")
	public void filterSide5() throws Exception {
		existFilter();
		sideFilterStatus(1, CheckXpath("BID_SUMMARY_CLOSED"));
	}

	@Test(priority = 12, description = "#. BIDCQ SUMMARY FILTER FOR  CLOSED BID : ID")
	public void filterSummary6() throws Exception {
		existFilter();
		summaryFilter(1, "ID", "BID_ID_LABEL");
	}

	@Test(priority = 13, description = "#. BIDCQ SUMMARY FILTER FOR  CLOSED BID : QUOTE NO")
	public void filterSummary7() throws Exception {
		existFilter();
		summaryFilter(1, "QUOTE NO", "BID_QUOTENO_LABEL");
	}

	@Test(priority = 14, description = "#. BIDCQ SUMMARY FILTER FOR  CLOSED BID : COMPANY")
	public void filterSummary8() throws Exception {
		existFilter();
		summaryFilter(1, "Company", "BID_COMPANY_LABEL");
	}

	@Test(priority = 15, description = "#. BIDCQ SUMMARY FILTER FOR  CLOSED BID  : DUEDATE")
	public void filterSummary9() throws Exception {
		existFilter();
		summaryFilter(2, "Due Date", "BID_DUEDATE_LABEL");
		CheckXpath("BID_DATE_FILTER").clear();
	}

	@Test(priority = 16, description = "#. BIDCQ SUMMARY FILTER FOR  CLOSED BID : QUOTE-ASSEMBLY")
	public void filterSummary10() throws Exception {
		existFilter();
		summaryFilter(1, "Quote - Assembly", "BID_QUOTEASM_LABEL");
	}

	@Test(priority = 17, description = "#. BIDCQ FILTER : NO OF BIDS  FOR CLOSED BID")
	public void filterSide6() throws Exception {
		sideFilterSorting(2, CheckXpath("FILTER_BIDS"), ".//div[contains(@class,'col-sm-5')]");
	}

	@Test(priority = 18, description = "#. BIDCQ  FILTER : NO OF LINES  FOR CLOSED BID")
	public void filterSide7() throws Exception {
		sideFilterSorting(3, CheckXpath("FILTER_LINES"), ".//div[contains(@class,'col-sm-5')]");
	}

	@Test(priority = 19, description = "#. BIDCQ  FILTER : ALPHABETICAL FOR CLOSED BID")
	public void filterSide8() throws Exception {
		existFilter();
		sideFilterSorting(1, CheckXpath("FILTER_ALPHABETICAL"), ".//div[contains(@class,'sub-header-normal col')]");
	}

	public void summaryFilter(int type, String fieldName, String fieldID) throws InterruptedException {
		System.out.println("** Searching with :: " + fieldName);
		log.info("** Searching with :: " + fieldName);
		waitforseconds(3);
		try {
			String id = CheckXpath(fieldID).getAttribute("id").replaceAll("-header-text", "");
			String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
			if (type == 1) {
				filter = driver.findElement(By.xpath(
						".//div[contains(@id,'" + id1 + "')]//following-sibling::div//div[@class='ng-scope']//input"));
			} else if (type == 2) {
				filter = CheckXpath("BID_DATE_FILTER");
			}
			filter.clear();
			waitforseconds(3);

			String Record = driver
					.findElement(By.xpath(
							"(.//div[contains(@id,'" + id1 + "')]//div//span[contains(@class,'ng-binding')])[last()]"))
					.getText().replace("Due:", "");
			String searchRecord = Record.substring(Record.lastIndexOf("-") + 1);

			/*
			 * String searchRecord = driver .findElement(By.xpath( "(.//div[contains(@id,'"
			 * + id1 + "')]//div//span[contains(@class,'ng-binding')])[last()]"))
			 * .getText().replace("Due:", "");
			 */
			System.out.println("~>> Search Record :: " + searchRecord);
			if (searchRecord.isEmpty()) {
				log.info("~>> No Records for Search");
				System.out.println("~>> No Records for Search");
			} else {
				filter.sendKeys(searchRecord);
				waitforseconds(5);
				log.info("~>> Verify Searched Records");
				List<WebElement> lstrecord = driver.findElements(
						By.xpath(".//div[contains(@id,'" + id1 + "')]//div//span[contains(@class,'ng-binding')]"));
				if (lstrecord.size() == 0) {
					log.info("~>> RESULT :: No records found");
					System.out.println("~>> RESULT :: No records found");
					Assert.assertTrue(false);
				} else {
					for (WebElement ele : lstrecord) {
						if (ele.getText().replace("Due:", "").contains(searchRecord)) {
							log.info("~>> RESULT :: Searched Record is Correct");
							System.out.println("~>> RESULT :: Searched Record is Correct");
							Assert.assertTrue(true);
						} else {
							log.info("~>> RESULT :: Searched Record is Wrong");
							System.out.println("~>> RESULT :: Searched Record is Wrong");
							Assert.assertTrue(false);
						}
					}
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		System.out.println(
				"............................................................................................................");
		log.info(
				"............................................................................................................");
	}

	public void sideFilterStatus(int type, WebElement element) throws Exception {
		waitforseconds(3);
		safeJavaScriptClick(element);
		waitforseconds(3);
		String id = CheckXpath("BID_STATUS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		List<WebElement> lststatus = driver.findElements(By.xpath(".//div[contains(@id,'" + id1
				+ "')]//div[contains(@class,'bidsheet-status') or contains(@class,'shared-Status')]"));
		log.info("~>> Total Records :: " + lststatus.size());
		List<String> strStatus = new ArrayList<String>();
		for (WebElement e : lststatus) {
			strStatus.add(e.getText());
		}
		System.out.println("~>> Listed Status :: " + strStatus);
		if (type == 1) {
			for (WebElement ele : lststatus) {
				if (ele.getText().equals("New") || ele.getText().equals("Open")) {
					log.info("~>> RESULT :: Records are Wrong - Status = " + ele.getText());
					Assert.assertTrue(false);
				} else {
					log.info("~>> RESULT ::  Records are Correct  - Status =  " + ele.getText());
					Assert.assertTrue(true);
				}
			}
		} else if (type == 2) {
			for (WebElement ele : lststatus) {
				if (ele.getText().equals("New") || ele.getText().equals("Open") || ele.getText().equals("Shared")) {
					log.info("~>> RESULT ::  Records are Correct  - Status =  " + ele.getText());
					Assert.assertTrue(true);
				} else {
					log.info("~>> RESULT :: Records are Wrong  - Status =  " + ele.getText());
					Assert.assertTrue(false);
				}
			}
		}
		System.out.println(
				"............................................................................................................");
		log.info(
				"............................................................................................................");
	}

	public void sideFilterSorting(int type, WebElement filter, String path) throws Exception {
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("BID_SIDE_FILTER"));
		waitforseconds(3);
		safeJavaScriptClick(filter);
		waitforseconds(5);
		List<WebElement> lstType = driver.findElements(By.xpath(path));
		if (type == 1) {
			List<String> stralpha = new ArrayList<String>();
			for (WebElement e : lstType) {
				stralpha.add(e.getText());
			}
			System.out.println("~>> Alphabetical Order : " + stralpha);
			for (int i = 1; i < stralpha.size(); i++) {
				if (stralpha.get(i - 1).compareTo(stralpha.get(i)) > 0) {
					log.info("~>>Customer is not in Alphabetical Order");
					Assert.assertTrue(false);
					break;
				}
				log.info("~>> Customer is in Alphabetical Order");
				Assert.assertTrue(true);

			}
		} else if (type == 2) {
			List<Integer> intBids = new ArrayList<Integer>();
			for (WebElement e : lstType) {
				String strbid = e.getText().split(",")[0].replaceAll("[^0-9]", "");
				intBids.add(Integer.parseInt(strbid));
			}
			System.out.println("~>> Bids Sorting Order : " + intBids);
			for (int i = 0; i < intBids.size() - 1; i++) {
				if (intBids.get(i) < intBids.get(i + 1)) {
					Assert.assertTrue(false);
					log.info("~>> Bids sorting is Wrong");
					break;
				} else {
					log.info("~>> Bids sorting is Correct");
					Assert.assertTrue(true);
				}
			}
		} else if (type == 3) {
			List<Integer> intLines = new ArrayList<Integer>();
			for (WebElement e : lstType) {
				String strbid = e.getText().split(",")[1].replaceAll("[^0-9]", "");
				intLines.add(Integer.parseInt(strbid));
			}
			System.out.println("~>> Lines Sorting Order : " + intLines);
			for (int i = 0; i < intLines.size() - 1; i++) {
				if (intLines.get(i) < intLines.get(i + 1)) {
					Assert.assertTrue(false);
					log.info("~>> Lines sorting is Wrong");
					break;
				} else {
					log.info("~>> Lines sorting is Correct");
					Assert.assertTrue(true);
				}
			}
		}
		System.out.println(
				"............................................................................................................");
		log.info(
				"............................................................................................................");
	}

	public static void existFilter() throws InterruptedException {
		log.info("~>> Checking for existing filter");
		List<WebElement> listfilter = driver.findElements(
				By.xpath(".//div[@class='ui-grid-filter-button ng-scope']//i[@class='ui-grid-icon-cancel']"));
		log.info("~>> No of Filter :: " + listfilter.size());
		for (WebElement filter : listfilter) {
			JavascriptExecutor je = (JavascriptExecutor) driver;
			je.executeScript("arguments[0].click()", filter);
			// filter.click();
			log.info("~>> filter removed");
		}
	}
}