package BidCQ;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class BidCQ_Franchised_UnFranchised extends BaseInitbidCQ {

	BidCQ_BidGenerate BG = new BidCQ_BidGenerate();
	ArrayList<String> arrMfgr = new ArrayList<String>();
	ArrayList<String> arrFranchised = new ArrayList<String>();
	static String strMgr;
	static String oldstrFranchised, newstrFranchised;
	WebElement quoteidfilter;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. FRANCHISED FROM LINECARD")
	public void linecardFranchised() throws Exception {
		headerFormat("#. CASE : FRANCHISED/UNFRANCHISED FROM LINECARD MANAGEMENT");
		if (getHost()[0].contains("app.calcuquote.com")) {
			driver.get("https://trial.calcuquote.com/#/Dashboard");
		} else {
			driver.get("https://" + getHost()[0] + "/BidCQIdentity/#/");
		}
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("BID_SUMMARY_OPEN"));
		openBid(2);
		strMgr = getName("Mfgr").getText();
		oldstrFranchised = getName("Franchised").getText();
		System.out.println("~>> Current Franchised status for Mfgr :: " + strMgr + "  is ::  " + oldstrFranchised);
		lineCard(1);
		if (getName("Franchised").getText().equals(newstrFranchised)) {
			System.out.println("~>> Franchised status updated successfully");
			Assert.assertTrue(true);
		} else {
			System.out.println("~>> Franchised status not updated successfully");
			Assert.assertTrue(false);
		}
		System.out.println(
				"..................................................................................................");
	}

	@Test(priority = 2, description = "#. FRANCHISED/UNFRANCHISED FROM BIDSHEET")
	public void linecardUnFranchised() throws Exception {
		log.info("** FRANCHISED/UNFRANCHISED FROM BIDSHEET");
		System.out.println("** FRANCHISED/UNFRANCHISED FROM BIDSHEET");
		strMgr = getName("Mfgr").getText();
		oldstrFranchised = getName("Franchised").getText();
		System.out.println("~>> Current Franchised status for Mfgr :: " + strMgr + "  is ::  " + oldstrFranchised);
		waitforseconds(5);
		Actions action = new Actions(driver);
		action.moveToElement(getName("Franchised")).doubleClick().perform();
		waitforseconds(5);
		alertacceptpopup();
		waitforseconds(5);
		String strUpdatedFranchised = getName("Franchised").getText();
		lineCard(2);
		if (strUpdatedFranchised.equals(newstrFranchised)) {
			System.out.println("~>> Franchised status updated successfully");
			Assert.assertTrue(true);
		} else {
			System.out.println("~>> Franchised status not updated successfully");
			Assert.assertTrue(false);
		}
		System.out.println(
				"..................................................................................................");
	}

	public WebElement getName(String label) {
		int intlabel = BG.getIndex(label);
		WebElement ele = driver.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[1]//td[" + intlabel + "]"));
		// String name = ele.getText();
		return ele;
	}

	public ArrayList<String> getNameList(String label) {
		int intlabel = BG.getIndex(label);
		List<WebElement> lst = driver
				.findElements(By.xpath(".//table[@class='htCore']//tbody//tr//td[" + intlabel + "]"));
		ArrayList<String> arrlst = new ArrayList<String>();
		for (int i = 0; i < lst.size(); i++) {
			arrlst.add(lst.get(i).getText());
		}
		return arrlst;
	}

	public void lineCard(int type) throws Exception {
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_HEADER_TOGGLE"));
		// waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_MENU_LINECARD"));
		waitforseconds(10);
		CheckXpath("BID_LINECARD_MFGR_SEARCH").clear();
		CheckXpath("BID_LINECARD_MFGR_SEARCH").sendKeys(strMgr);
		waitforseconds(8);
		String id = CheckXpath("BID_LINECARD_FRANCHISED").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		quoteidfilter = driver.findElement(By.xpath(
				"(.//div[contains(@id,'" + id1 + "')]//div[@class='ui-grid-cell-contents ng-binding ng-scope'])[1]"));
		if (type == 1) {
			updateFranchised();
		} else if (type == 2) {
			newstrFranchised = quoteidfilter.getText();
			System.out.println("~>> Updated Franchised status for Mfgr :: " + strMgr + "  is ::  " + newstrFranchised);
		}
	}

	public void updateFranchised() throws Exception {
		Actions action = new Actions(driver);
		action.moveToElement(quoteidfilter).doubleClick().perform();
		waitforseconds(5);
		driver.findElement(By.xpath(".//input[@type='checkbox']")).click();
		waitforseconds(3);
		action.sendKeys(Keys.ENTER);
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_LINECARD_FRANCHISED_SAVE"));
		waitforseconds(30);
		newstrFranchised = quoteidfilter.getText();
		System.out.println("~>> Updated Franchised status for Mfgr :: " + strMgr + "  is ::  " + newstrFranchised);
		driver.navigate().back();
		waitforseconds(15);
		bidInfoPopup();
	}
}