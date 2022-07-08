package SmokeTest;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class Smoke_NRE extends BaseInitsmokeTest {

	public String strCurrency;
	public static String StrStandard, StrOptional;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. CREATE NRE FROM NRE TAB")
	public void createNRE() throws Exception {
		System.out.println("");
		headerFormat("#. CASE : CREATE NRE FROM NRE TAB");
		chkOpenQuote();
		safeJavaScriptClick(CheckXpath("NRE_TAB"));
		StrStandard = create("Standard", "1000");
		StrOptional = create("Optional", "2000");
	}

	@Test(priority = 2, description = "#. ADD NRE FROM NRE TAB")
	public void addNRE() throws Exception {
		System.out.println("");
		headerFormat("#. CASE : ADD NRE FROM NRE TAB");
		chkOpenQuote();
		add("NRE_ADD_STD_BUTTON", StrStandard);
		add("NRE_ADD_OPT_BUTTON", StrStandard);
		verifyNRE("NRE_STD_LABEL", StrStandard);
		verifyNRE("NRE_OPT_LABEL", StrOptional);
	}

	@Test(priority = 3, description = "#. DELETE NRE FROM CONFIGURATION")
	public void deleteNRE() throws Exception {
		System.out.println("");
		System.out.println("** Deleting NRE from configuration");
		log.info("** Deleting NRE from configuration");
		waitforseconds(15);
		driver.get("https://" + getHost()[0] + getHost()[1] + "/#/Configuration/NRE");
		delete(StrStandard);
		delete(StrOptional);
	}

	public String create(String type, String value) throws Exception {
		System.out.println("");
		System.out.println("** Creating " + type + " NRE");
		log.info("** Creating " + type + " NRE");
		String strName = null;
		boolean isCreate = false;
		try {
			isCreate = CheckXpath("NRE_CREATE_BUTTON").isDisplayed();
		} catch (Exception e) {

		}
		if (isCreate) {
			safeJavaScriptClick(CheckXpath("NRE_CREATE_BUTTON"));
			waitforseconds(5);
			CheckXpath("NRE_NAME").clear();
			strName = "Auto NRE - " + System.currentTimeMillis();
			CheckXpath("NRE_NAME").sendKeys(strName);
			CheckXpath("NRE_DESCRIPTION").clear();
			CheckXpath("NRE_DESCRIPTION").sendKeys("Auto NRE Description for " + strName);
			new Select(CheckXpath("NRE_CHARGE_TYPE")).selectByVisibleText(type);
			CheckXpath("NRE_VALUE").clear();
			CheckXpath("NRE_VALUE").sendKeys(value);
			strCurrency = new Select(CheckXpath("NRE_CURRENCY")).getFirstSelectedOption().getText();
			System.out.println("~>> Currency :: " + strCurrency);
			safeJavaScriptClick(CheckXpath("NRE_SAVE"));
			System.out.println("~>> " + type + " NRE Created :: " + strName);
		} else {
			System.out.println("~>> Create NRE is Disabled");
		}
		return strName;
	}

	public void add(String type, String name) throws Exception {
		System.out.println("");
		System.out.println("** Adding " + type + " NRE");
		log.info("** Adding " + type + " NRE");
		waitforseconds(8);
		boolean isadd = false;
		try {
			isadd = CheckXpath(type).isDisplayed();
		} catch (Exception e) {

		}
		if (isadd) {
			safeJavaScriptClick(CheckXpath(type));
			log.info("~>> Add standard clicked");
			waitforseconds(3);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			waitforseconds(5);
			Actions action = new Actions(driver);
			action.moveToElement(CheckXpath("ADD_NRE_NAME")).doubleClick().perform();
			waitforseconds(2);
			if (!CheckXpath("SELECT_NRE_OPTIONS").isDisplayed()) {
				action.moveToElement(CheckXpath("ADD_NRE_NAME")).doubleClick().perform();
				waitforseconds(2);
			}
			CheckXpath("SELECT_NRE_OPTIONS").sendKeys(name);
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("NRE_SUBMIT"));
			System.out.println("~>> " + type + " NRE added successfully");
		} else {
			System.out.println("~>> Add NRE  option is Disabled");
		}
	}

	public void verifyNRE(String headerId, String name) throws InterruptedException {
		System.out.println("");
		System.out.println("** Verifying Added NRE");
		log.info("** Verifying Added NRE");
		waitforseconds(10);
		String id = CheckXpath(headerId).getAttribute("id").replaceAll("-header-text", "");
		// System.out.println(id);
		String id1 = (id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		// System.out.println(id1);
		waitforseconds(3);
		List<WebElement> desc = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		ArrayList<String> arrNRE = new ArrayList<String>();
		for (WebElement ele : desc) {
			arrNRE.add(ele.getText());
		}
		System.out.println("~>> Added NRE :: " + arrNRE);
		System.out.println("~>> Expected NRE :: " + name);
		if (arrNRE.contains(name + " (" + strCurrency + ")")) {
			System.out.println("~>> NRE Added  and Saved Succesfully");
		} else {
			System.out.println("~>> NRE not added");
		}
		waitforseconds(3);
		System.out.println("");
		System.out.println("** Removing  added NRE");
		WebElement delNRE = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div[contains(text(),'"
				+ name + "')]//preceding::a[@title='Delete']//i"));
		delNRE.click();
		alertacceptpopup();
		System.out.println("~>> NRE Deleted successfully");
	}

	public void delete(String name) throws Exception {
		waitforseconds(10);
		CheckXpath("CONFIG_NRE_NAME_SEARCH").clear();
		CheckXpath("CONFIG_NRE_NAME_SEARCH").sendKeys(name);
		waitforseconds(5);
		driver.findElement(By.xpath("(.//a[contains(@ng-click,'Delete')]//i)[1]")).click();
		alertacceptpopup();
		if (driver.findElement(By.xpath(".//div[@class='ng-isolate-scope' and @role='row']")).isDisplayed()) {
			System.out.println("~>> Record not deleted successfully");
			log.info("~>> Record not deleted successfully");
			Assert.assertFalse(false);
		} else {
			System.out.println("~>> Record deleted successfully");
			log.info("~>> Record deleted successfully");
			Assert.assertFalse(true);
		}
	}
}