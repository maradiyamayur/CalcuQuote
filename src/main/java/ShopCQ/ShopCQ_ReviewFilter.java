package ShopCQ;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ShopCQ_ReviewFilter extends BaseInitshopCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Parameters({ "correctusername", "correctpassword", })
	@Test(description = "#. HAPPY PATH :: SCQ - REVIEW FILTER")
	public void reviewFilter() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : REVIEW FILTER");
		System.out.println("CASE : REVIEW FILTER");
		demandList();
		waitforseconds(15);
		gridmenu();
		removeFilter();
		negtiveTargetprice();
		// crossMatchedPart();
		// excessMaterial();
		// packaging();
		// supplier();
		log.info("----------------------------------------------------------------------------");
	}

	public void checkfilteropen() {
		boolean filteropen = false;
		try {
			filteropen = driver
					.findElement(By.xpath(("(.//div[@class='dropdown-menu dropdown-menu-form custdropdown-Div'])[1]")))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (filteropen) {
			log.info("~>> Filter is already opened");
		} else {
			CheckXpath("REVIEW_FILTER").click();
			log.info("~>> Review Filter Clicked");
			waitforseconds(3);
			boolean isSelected = false;
			try {
				isSelected = CheckXpath("RF_SELECTED_FILTER").isDisplayed();
			} catch (Exception e) {

			}
			if (isSelected) {
				List<WebElement> lstselected = driver.findElements(By.xpath(
						"(.//ul[@class='custdropdown-menu'])[1]//li[@role='presentation']//a[@role='menuitem']//span[@class='glyphicon glyphicon-ok']"));
				for (WebElement ele : lstselected) {
					ele.click();
				}
			} else {
				log.info("~>> No filter selected");
			}
		}
	}

	public void Filter() {
		Boolean filter = false;
		try {
			filter = driver.findElement(By.xpath(
					".//div[@class='headerFilter reviewFilter ng-isolate-scope']//div[@class='dropdown-menu dropdown-menu-form custdropdown-Div']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (filter) {
			CheckXpath("REVIEW_FILTER").click();
			log.info("~>> Review Filter Clicked");
		} else {
			log.info("~>> Review filter is alredy open");
		}
	}

	public void negtiveTargetprice() throws Exception {
		log.info("............................................................................................");
		System.out.println(
				"............................................................................................");
		waitforseconds(8);
		log.info("** FILTER :  NEGATIVE TARGET PRICE VARIANCE");
		System.out.println("** FILTER :  NEGATIVE TARGET PRICE VARIANCE");
		CheckXpath("REVIEW_FILTER").click();
		log.info("~>> Review Filter Clicked");
		CheckXpath("RF_NEGATIVE").click();
		log.info("~>> Negative target price filter selected");
		// waitforseconds(3);
		// driver.findElement(By.xpath(".//*[@id='summary1']")).click();
		waitforseconds(8);
		resultVerification(0, "RF_RESULT_NEGATIVE_UNIT", "", "");
		waitforseconds(5);
		// CheckXpath("REVIEW_FILTER").click();
		// log.info("~>> Review Filter Clicked");
		safeJavaScriptClick(CheckXpath("RF_NEGATIVE"));
		log.info("----------------------------------------------------------------------------");
	}

	public void crossMatchedPart() throws Exception {
		log.info("............................................................................................");
		System.out.println(
				"............................................................................................");
		waitforseconds(8);
		log.info("** FILTER :  CROSS MATCHED PART");
		System.out.println("** FILTER :  CROSS MATCHED PART");
		// Filter();
		checkfilteropen();
		CheckXpath("RF_CROSS").click();
		log.info("~>> Cross matched parts filter selected");
		// waitforseconds(3);
		// driver.findElement(By.xpath(".//*[@id='summary1']")).click();
		waitforseconds(8);
		resultVerification(0, "RF_RESULT_CROSS_MATCHED", "", "");
		waitforseconds(5);
		// CheckXpath("REVIEW_FILTER").click();
		// log.info("~>> Review Filter Clicked");
		safeJavaScriptClick(CheckXpath("RF_CROSS"));
		log.info("----------------------------------------------------------------------------");
	}

	public void excessMaterial() throws Exception {
		log.info("............................................................................................");
		System.out.println(
				"............................................................................................");
		waitforseconds(5);
		log.info("** FILTER :  EXCESS MATERIAL");
		System.out.println("** FILTER :  EXCESS MATERIAL");
		checkfilteropen();
		CheckXpath("RF_EXCESS").click();
		log.info("~>> Excess material filter selected");
		// waitforseconds(3);
		// driver.findElement(By.xpath(".//*[@id='summary1']")).click();
		waitforseconds(5);
		resultVerification(1, "", "DL_EXCESS_LABEL", "");
		waitforseconds(5);
		// CheckXpath("REVIEW_FILTER").click();
		// log.info("~>> Review Filter Clicked");
		safeJavaScriptClick(CheckXpath("RF_EXCESS"));
		log.info("----------------------------------------------------------------------------");
	}

	public void packaging() throws Exception {
		log.info("............................................................................................");
		System.out.println(
				"............................................................................................");
		waitforseconds(5);
		log.info("** FILTER : PACKAGING");
		System.out.println("** FILTER : PACKAGING");
		checkfilteropen();
		Actions actions = new Actions(driver);
		WebElement packaging = CheckXpath("RF_PACKAGING");
		actions.moveToElement(packaging).perform();
		WebElement packageoption = CheckXpath("RF_PACKAGING_SUB");
		packageoption.click();
		String Packingname = packageoption.getText();
		log.info("~>> Selected packaging name is :: " + Packingname);
		log.info("~>> Packaging filter selected");
		// waitforseconds(3);
		// driver.findElement(By.xpath(".//*[@id='summary1']")).click();
		waitforseconds(5);
		resultVerification(2, "", "DL_PACKAGING_LABEL", Packingname);
		waitforseconds(5);
		// CheckXpath("REVIEW_FILTER").click();
		// log.info("~>> Review Filter Clicked");

		safeJavaScriptClick(packaging);
		log.info("----------------------------------------------------------------------------");
	}

	public void supplier() throws Exception {
		log.info("............................................................................................");
		System.out.println(
				"............................................................................................");
		waitforseconds(8);
		log.info("** FILTER : SUPPLIER");
		System.out.println("** FILTER : SUPPLIER");
		checkfilteropen();
		Actions actions = new Actions(driver);
		WebElement supplier = CheckXpath("RF_SUPPLIER");
		actions.moveToElement(supplier).perform();
		WebElement supplieroption = CheckXpath("RF_SUPPLIER_SUB");
		supplieroption.click();
		String Suppliername = supplieroption.getText();
		log.info("~>> Selected Suplier  :: " + Suppliername);
		log.info("~>> Supplier  filter selected");
		// waitforseconds(3);
		// driver.findElement(By.xpath(".//*[@id='summary1']")).click();
		waitforseconds(5);
		resultVerification(2, "", "DL_SUPPLIER_LABEL", Suppliername);
		waitforseconds(5);
		// CheckXpath("REVIEW_FILTER").click();
		// log.info("~>> Review Filter Clicked");
		safeJavaScriptClick(supplier);
		log.info("----------------------------------------------------------------------------");
	}

	public void resultVerification(int type, String actuallist, String headerId, String input)
			throws InterruptedException {
		boolean norecord = false;
		try {
			norecord = CheckXpath("DL_CLEAR_SELECTION").isDisplayed();
		} catch (Exception e) {

		}
		if (norecord) {
			log.info("RESULT :: NO DETAILS TO DISPLAY FOR THE CURRENT SELECTION");
			System.out.println("RESULT :: NO DETAILS TO DISPLAY FOR THE CURRENT SELECTION");
		} else {
			if (type == 0) {
				boolean list = false;
				try {
					list = CheckXpath(actuallist).isDisplayed();
				} catch (Exception e) {

				}
				if (list) {
					Assert.assertTrue(true);
					log.info("RESULT-1 :: FILTRATION DONE SUCCESSFULLY");
					System.out.println("RESULT-1 :: FILTRATION DONE SUCCESSFULLY");
					List<WebElement> listactual = CheckXpathlist(actuallist);
					int actualTotal = listactual.size();
					List<WebElement> Result = driver.findElements(By.xpath(".//div[@class='ui-grid-cell-contents']"));
					int expectedTotal = Result.size() - 1;
					log.info("~>> Expected total records :: " + expectedTotal);
					log.info("~>> Actual total records :: " + actualTotal);
					if (expectedTotal == actualTotal) {
						log.info("RESULT-2 ::  ALL FILTERED RECORDS ARE CORRECT");
						System.out.println("RESULT-2 :: ALL FILTERED RECORDS ARE CORRECT");
					} else {
						log.info("RESULT-2 :: ALL FILTERED RECORDS ARE NOT CORRECT");
						System.out.println("RESULT-2 ::  ALL FILTERED RECORDS ARE NOT CORRECT");
					}
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT-1 :: FILTRATION NOT DONE SUCCESSFULLY");
					System.out.println("~>> RESULT-1 :: FILTRATION NOT DONE SUCCESSFULLY");
				}

			} else if (type == 1) {
				String id = CheckXpath(headerId).getAttribute("id").replaceAll("-header-text", "");
				String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
				waitforseconds(3);
				log.info(id);
				log.info(id1);
				/*
				 * String totalcost = driver.findElement(By.xpath(
				 * ".//*[@id='summary1']/div/table/tbody/tr[1]/td[3]/span"))
				 * .getAttribute("uib-tooltip"); String currency = totalcost.replaceAll("[0-9]",
				 * "").replace(",", "").replace(".", "").concat("0");
				 */
				List<WebElement> lstrecord = driver.findElements(
						By.xpath(".//div[contains(@id,'" + id1 + "') and not(contains(@role,'button'))]//div"));
				for (WebElement e : lstrecord) {
					double excess = Double
							.valueOf(e.getAttribute("innerText").replaceAll("[^\\d]", "").replace(",", ""));
					// if (e.getAttribute("innerText").equals(currency))
					log.info(excess);
					if (excess > 0.0) {
						Assert.assertTrue(true);
						log.info("~>>Value ::" + e.getAttribute("innerText") + " - Result Passed");
					} else {
						Assert.assertTrue(false);
						log.info("~>>Value ::" + e.getAttribute("innerText") + " - Result Failed");
					}
				}
				waitforseconds(3);
				log.info("RESULT :: EXCESS MATERIAL FILTER VERIFICATION DONE");
				System.out.println("RESULT :: EXCESS MATERIAL FILTER VERIFICATION DONE");
			} else if (type == 2) {
				String id2 = CheckXpath(headerId).getAttribute("id").replaceAll("-header-text", "");
				String id3 = id2.split("-")[1].concat("-").concat(id2.split("-")[2]);
				List<WebElement> lstresult = driver.findElements(By.xpath(".//div[contains(@id,'" + id3 + "')]//div"));
				for (WebElement result : lstresult) {
					if (result.getAttribute("innerText").equals(input)) {
						Assert.assertTrue(true);
						log.info("RESULT ::  ALL FILTERED RECORDS ARE CORRECT");
					} else {
						Assert.assertTrue(false);
						log.info("RESULT ::  ALL FILTERED RECORDS  ARE NOT CORRECT");
					}
				}
				System.out.println("RESULT ::  FILTER VERIFICATION DONE");
			}
		}
	}
}
