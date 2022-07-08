package BidCQ;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BidCQ_ShareBid extends BaseInitbidCQ {

	String email = "tgs.payal@outlook.com";
	WebElement filter;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. SHARE BID FROM BID MANAGEMENT")
	public void shareBid1() throws Exception {
		headerFormat("#. CASE : SHARE BID FROM BID MANAGEMENT");
		windowHandle();
		if (getHost()[0].contains("app.calcuquote.com")) {
			driver.get("https://trial.calcuquote.com/#/Dashboard");
		} else {
			driver.get("https://" + getHost()[0] + "/BidCQIdentity/#/");
		}
		boolean isQuote = false;
		try {
			isQuote = driver.findElement(By.xpath(".//div[@class='ui-grid-row ng-scope']")).isDisplayed();
		} catch (Exception e) {

		}
		if (isQuote) {
			BidCQ_Filter.existFilter();
			openBid(2);
			safeJavaScriptClick(CheckXpath("BID_MGMT_MENU"));
			waitforseconds(2);
			safeJavaScriptClick(CheckXpath("BID_MGMT_MENU_SHARE"));
			shareBid();
			waitforseconds(10);
			safeJavaScriptClick(CheckXpath("BID_SUMMARY_HEADER"));
			waitforseconds(5);
			WebElement element = driver.findElement(By.xpath(".//a[@href='#/Dashboard']"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			// driver.findElement(By.xpath(".//a[@href='#/Dashboard']")).click();
			VerifyShared(asmId);
		} else {
			System.out.println("~>> No Quotes Available for operation");
		}
		System.out.println(
				".....................................................................................................................");
		log.info(
				".....................................................................................................................");
	}

	@Test(priority = 2, description = "#. SHARE BID FROM FROM BID SUMMARY")
	public void shareBid2() throws Exception {
		log.info("** SHARE BID FROM FROM BID SUMMARY");
		System.out.println("** SHARE BID FROM FROM BID SUMMARY");
		bidInfoPopup();
		boolean isQuote = false;
		try {
			isQuote = driver.findElement(By.xpath(".//div[@class='ui-grid-row ng-scope']")).isDisplayed();
		} catch (Exception e) {

		}
		if (isQuote) {
			BidCQ_Filter.existFilter();
			waitforseconds(10);
			String strasmid = null;
			String id = CheckXpath("BID_STATUS_LABEL").getAttribute("id").replaceAll("-header-text", "");
			String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
			boolean eleOpen = false;
			try {
				eleOpen = driver.findElement(By.xpath(".//div[contains(@id,'" + id1
						+ "')]//div[contains(@class,'bidsheet-status') or contains(@class,'shared-Status')]//span[contains(text(),'Open')]"))
						.isDisplayed();
			} catch (Exception e) {

			}
			if (eleOpen) {
				log.info("~>>  Open Bid available to share");
				System.out.println("~>> Open Bid available to share");
				WebElement statusOpen = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1
						+ "')]//div[contains(@class,'bidsheet-status') or contains(@class,'shared-Status')]//span[contains(text(),'Open')]//preceding::div[@role='gridcell']//label//i)[1]"));
				statusOpen.click();
				String strquoteasmid = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1
						+ "')]//div[contains(@class,'bidsheet-status') or contains(@class,'shared-Status')]//span[contains(text(),'Open')]//preceding::div[@role='gridcell']//following::span[@ng-if='row.entity.DisplayAssemblyId'])[1]"))
						.getText();
				strasmid = strquoteasmid.substring(strquoteasmid.indexOf("-") + 1);
				System.out.println("~>> Assembly Id :: " + strasmid);
				safeJavaScriptClick(CheckXpath("BID_SUMMARY_SHARE"));
				shareBid();
				VerifyShared(strasmid);
			} else {
				log.info("~>> No Open Bid to share");
				System.out.println("~>> No Open Bid to share");
			}
		} else {
			System.out.println("~>> No Quotes Available for operation");
		}
		System.out.println(
				"....................................................................................................");
		log.info(
				"....................................................................................................");
	}

	public void shareBid() throws Exception {
		waitforseconds(5);
		CheckXpath("BID_SHARE_EMAIL").clear();
		CheckXpath("BID_SHARE_EMAIL").sendKeys(email);
		CheckXpath("BID_SHARE_EMAIL").click();
		waitforseconds(5);
		Actions action = new Actions(driver);
		action.moveToElement(CheckXpath("BID_SHARE_BUTTON")).click().perform();
		action.moveToElement(CheckXpath("BID_SHARE_BUTTON")).click().perform();
		boolean popup = false;
		try {
			popup = driver.findElement(By.xpath(".//div[@class='swal2-modal swal2-show']")).isDisplayed();
		} catch (Exception e) {

		}
		if (popup) {
			log.info("~>> Bid is already shared with this user.");
			System.out.println("~>> Bid is already shared with this user.");
			driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
			safeJavaScriptClick(CheckXpath("BID_SHARE_CLOSE"));
		} else {
			waitforseconds(40);
		}

	}

	public void VerifyShared(String asmId) throws Exception {
		waitforseconds(5);
		searchFilter("BID_QUOTEASM_LABEL", asmId);
		boolean shared = false;
		try {
			shared = driver.findElement(By.xpath("(.//div[contains(@class,'shared')])[1]")).isDisplayed();
		} catch (Exception e) {

		}
		if (shared) {
			log.info("~>> Shared button displayed");
			WebElement bid = driver.findElement(By.xpath("(.//div[contains(@class,'shared')])[1]"));
			Actions action = new Actions(driver);
			action.moveToElement(bid).perform();
			waitforseconds(5);
			boolean tooltip = false;
			try {
				tooltip = driver.findElement(By.xpath(".//div[@class='tooltip-inner']")).isDisplayed();
			} catch (Exception e) {

			}
			if (tooltip) {
				log.info("~>> Tool tip displayed");
				List<WebElement> lstbid = driver.findElements(
						By.xpath(".//div[@class='tooltip-inner']//div[@ng-if='grid.appScope.UserEmail!= item']"));
				ArrayList<String> arrbid = new ArrayList<String>();
				for (WebElement e : lstbid) {
					arrbid.add(e.getText());
				}
				if (arrbid.contains(email.toLowerCase())) {
					log.info("~>> RESULT :: Correct shared email address displayed");
					System.out.println("~>> RESULT ::  Correct shared email address displayed");
					Assert.assertTrue(true);
				} else {
					log.info("~>> RESULT :: Shared email address not displayed");
					System.out.println("~>> RESULT :: Shared email address not displayed");
					Assert.assertTrue(false);
				}
			}
		} else {
			log.info("~>> Shared button  not displayed");
		}
		System.out.println(
				"........................................................................................................................................");
		System.out.println("");
	}

	public void searchFilter(String fieldID, String input) {
		String id = CheckXpath(fieldID).getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		WebElement eleFilter = driver.findElement(
				By.xpath(".//div[contains(@id,'" + id1 + "')]//following-sibling::div//div[@class='ng-scope']//input"));
		eleFilter.clear();
		eleFilter.sendKeys(input);
		waitforseconds(10);
	}
}