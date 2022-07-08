package ImportWidget;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import QuoteCQ.BaseInitquoteCQ;
import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitImportWidget extends BaseInit {

	public static String strTags;
	static SoftAssert softAssertion = new SoftAssert();

	@BeforeSuite
	public void checkExecutionModeTestSuite() throws IOException {
		startup(); // Code to check execution mode of Test Suite
		if (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "ImportWidget", "TestSuite")) {
			throw new SkipException("Execution mode of the test suite is set to NO");
		}
	}

	public void quoteChecker() throws Exception {
		openQuote();
		checkLineItems();
	}

	public void openQuote() throws Exception {
		boolean BOMtab = false;
		try {
			BOMtab = driver.findElement(By.xpath(".//*[@id='tabbom']")).isDisplayed();
		} catch (Exception e) {

		}
		if (BOMtab) {
			log.info("~>> Quote is already Opened");
		} else {
			log.info("~>> Opening Quote");
			BaseInitquoteCQ.RFQ_list();
			BaseInitquoteCQ.first_RFQ();
			waitforseconds(8);
		}
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
			log.info("~>> BOM Deleted Successfully");
			driver.navigate().refresh();
			waitforseconds(12);
		}
	}

	public void importfile(String type, String filename) throws Exception {
		waitforseconds(12);
		if (type == "BOM") {
			log.info("~** Import BOM Using Widget");
			safeJavaScriptClick(CheckXpath("IMPORT_BOM"));
			log.info("~>> Clicked on Import BOM");
		}
		if (type == "DemandLine") {
			log.info("~** Upload Demand Using Widget");
			safeJavaScriptClick(CheckXpath("UPLOAD_DEMAND"));
			log.info("~>> Clicked on upload demand");
		}
		waitforseconds(5);
		driver.switchTo().frame("widgetImportButton");
		waitforseconds(5);
		/*
		 * CheckXpath("IMPORT_SELECT_FILE").click();
		 * log.info("~>> Select file button clicked");
		 * log.info("~>> Select file to upload");
		 * uploadFile(System.getProperty("user.dir") +
		 * "\\src\\main\\java\\Resources\\Import_Widget\\" + filename);
		 */
		File a = new File(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\Import_Widget\\" + filename);
		String absolute = a.getCanonicalPath();
		WebElement addfile = driver.findElement(By.xpath(".//input[@id='upfile']"));
		addfile.sendKeys(absolute);
		waitforseconds(10);
		log.info("~>> File uploaded");
		System.out.println("~>> File uploaded");
	}

	public void mappingScreen() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		CheckXpath("IMPORT_UPLOAD_NEXT").click();
		log.info("~>> Clicked on NEXT button for mapping Screen");
		System.out.println("~>> Clicked on NEXT button for mapping Screen");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		boolean alert = false;
		try {
			alert = driver.findElement(By.xpath(".//*[@id='swal2-content']")).isDisplayed();
		} catch (Exception e) {

		}
		if (alert) {
			if (driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText()
					.contains("Please map the following required column(s) and try again:")) {
				log.info("~>> RESULT :: ALERT DISPLAYED ::  "
						+ driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText());
				System.out.println("~>> RESULT :: ALERT DISPLAYED :: "
						+ driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText());
				List<WebElement> lstmandatory = driver.findElements(
						By.xpath(".//select[@class='ng-valid ng-empty ng-dirty ng-valid-parse ng-touched']"));
				for (WebElement elemandatory : lstmandatory) {
					String Strtitle = elemandatory.findElement(By.xpath(".//following::label[2]"))
							.getAttribute("title");
					if (Strtitle.contains("Part Number") || Strtitle.contains("MPN")) {
						waitforseconds(3);
						new Select(elemandatory).selectByVisibleText("MPN*");
						log.info("~>> MPN mapped");
					} else if (Strtitle.contains("Qty")) {
						waitforseconds(3);
						new Select(elemandatory).selectByVisibleText("Qty Per*");
						log.info("~>> Qty Per mapped");
					}
					CheckXpath("IMPORT_UPLOAD_NEXT").click();
					log.info("~>> Clicked on NEXT button for mapping Screen");
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
				}
			} else {
				log.info("~>> RESULT :: ALERT  NOT DISPLAYED ");
				System.out.println("~>> RESULT :: ALERT NOT DISPLAYED");
			}
		}
	}

	public static void validateimport() throws Exception {
		waitforseconds(3);
		boolean validate = false;
		try {
			validate = CheckXpath("VALIDATE_AND_IMPORT").isDisplayed();
		} catch (Exception e) {

		}
		if (validate) {
			safeJavaScriptClick(CheckXpath("VALIDATE_AND_IMPORT"));
			log.info("~>> Clicked on VALIDATE AND  IMPORT button");
			waitforseconds(5);
		}
	}

	public void validateBlankAlert() throws Exception {
		boolean popup = false;
		try {
			popup = driver.findElement(By.xpath(".//div[@role='dialog']")).isDisplayed();
		} catch (Exception e) {

		}
		if (popup) {
			String validationMsg = driver
					.findElement(By.xpath(".//div[@role='dialog']//div[contains(@class,'content')]")).getText();
			if (validationMsg.equals("Your file is empty.Please upload with proper data.")) {
				log.info("~>> RESULT ::  CORRECT VALIDATON MSG DISPLAYED :: " + validationMsg);
				System.out.println("~>>  RESULT :: CORRECT VALIDATON MSG DISPLAYED :: " + validationMsg);
				Assert.assertTrue(true);
			} else {
				log.info("~>>  RESULT ::  WRONG VALIDATON MSG DISPLAYED ::" + validationMsg);
				System.out.println("~>>  RESULT :: WRONG VALIDATON MSG DISPLAYED ::" + validationMsg);
				Assert.assertTrue(false);
			}
		} else {
			log.info("~>> There is no popup");
		}
		waitforseconds(3);
		IW_BOM_SingleLevel.closeWindow();
	}

	public void submitBOM() throws Exception {
		log.info("================================================================================");
		waitforseconds(15);
		log.info("STEP 4 :  SUBMIT BOM");
		WebElement BOMsubmit = CheckXpath("BOM_SUBMIT");
		safeJavaScriptClick(BOMsubmit);
		log.info("~>> RESULT :: BOM SUBMITTED");
		System.out.println("~>> RESULT :: BOM SUBMITTED");
		waitforseconds(8);
		log.info("================================================================================");
	}

	public static void gridmenu() throws Exception {
		log.info("~>> Checking Grid Menu");
		System.out.println("~>> Checking Grid Menu");
		waitforseconds(20);

		WebElement bomfilter = CheckXpath("BOM_FILTER");
		Actions actions = new Actions(driver);
		actions.moveToElement(bomfilter).click().perform();

		// safeJavaScriptClick(CheckXpath("BOM_FILTER"));
		// CheckXpath("BOM_FILTER").click();
		log.info("~>> Grid Menu filter clicked");
		// waitforseconds(8);
		boolean filtermenu = false;
		try {
			filtermenu = driver.findElement(By.xpath(".//div[@ng-show='shownMid']")).isDisplayed();
		} catch (Exception e) {

		}
		if (filtermenu) {
			List<WebElement> lstmenu = driver.findElements(By.xpath(
					".//ul[@class='ui-grid-menu-items']//li//button[@class='ui-grid-menu-item ng-binding']//i[contains(@class,'ui-grid')]"));
			log.info("~>> Total fields :: " + lstmenu.size());
			waitforseconds(3);
			for (int i = 0; i < lstmenu.size(); i++) {
				if (lstmenu.get(i).getAttribute("class").equals("ui-grid-icon-cancel")) {
					log.info(">> " + i + lstmenu.get(i).getText() + " :: Disabled");
					safeJavaScriptClick(lstmenu.get(i));
					log.info(">> " + i + lstmenu.get(i).getText() + " ::  Enabled now");
				} else {
					log.info(">> " + i + lstmenu.get(i).getText() + " :: Already Enabled");
				}
				waitforseconds(5);
			}
		} else {
			log.info("~>> Filter is not available");
			System.out.println("~>> Filter is not available");
		}
	}

	public static void demandList() throws Exception {
		waitforseconds(8);
		Boolean list = false;
		try {
			list = CheckXpath("DEMAND_LIST_HEADER").isDisplayed();
		} catch (Exception e) {

		}
		if (list) {
			log.info("~>> Demand List is open already");
			waitforseconds(5);
		} else {
			safeJavaScriptClick(CheckXpath("SHOP_CQ"));
			waitforseconds(8);
			safeJavaScriptClick(CheckXpath("DEMAND_LIST"));
			// CheckXpath("DEMAND_LIST").click();
			log.info("~>> Clicked on Demand list");
			waitforseconds(5);
			String demandList = driver.getCurrentUrl().replace("shopcqhome", "demandlist");
			driver.get(demandList);
			waitforseconds(10);
		}
	}

	public void removeFilter() throws InterruptedException {
		waitforseconds(5);
		log.info("~>> Checking for existing filter");
		List<WebElement> listfilter = driver.findElements(
				By.xpath(".//div[@class='ui-grid-filter-button ng-scope']//i[@class='ui-grid-icon-cancel']"));
		log.info("~>> No of Filter :: " + listfilter.size());
		for (WebElement filter : listfilter) {
			JavascriptExecutor je = (JavascriptExecutor) driver;
			je.executeScript("arguments[0].scrollIntoView(true);", filter);
			waitforseconds(5);
			je.executeScript("arguments[0].click()", filter);
			// filter.click();
			log.info("~>> filter removed");
			waitforseconds(5);
		}
	}

	public static void addTags() throws Exception {
		log.info("~>> Adding Tag Line");
		waitforseconds(5);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
				CheckXpath("IW_DL_TAGS_LABEL"));
		waitforseconds(3);
		String colNumber = CheckXpath("IW_DL_TAGS_LABEL").getAttribute("data-x");
		WebElement eleTag = driver.findElement(By.xpath("(.//*[@id='validationSpreadsheet']//table//tr//td[@data-x='"
				+ colNumber + "' and contains(@name,'col-')])[1]"));
		waitforseconds(3);
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HH-mm-ss");
		Date date = new Date();
		// strTags = String.valueOf(System.currentTimeMillis());
		strTags = formatter.format(date);
		Actions actions = new Actions(driver);
		actions.moveToElement(eleTag).doubleClick().perform();
		waitforseconds(3);
		for (int i = 1; i <= 15; i++) {
			actions.sendKeys(Keys.BACK_SPACE);
		}
		actions.sendKeys(strTags).build().perform();
		// waitforseconds(2);
		driver.findElement(By.xpath(".//*[@id='progressbar']")).click();
		// driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
		waitforseconds(2);
		CheckXpath("DL_POPUP_YES").click();
		log.info("~>> Tag Line Added :: " + strTags);
		System.out.println("~>> Tag Line Added :: " + strTags);
		waitforseconds(3);
	}

}
