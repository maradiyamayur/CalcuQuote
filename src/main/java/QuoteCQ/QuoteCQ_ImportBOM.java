package QuoteCQ;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_ImportBOM extends BaseInitquoteCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			// Code to check execution mode of Test Case
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ - IMPORT BOM AND SUBMIT")
	public void importMain() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		waitforseconds(5);
		System.out.println("CASE :  IMPORT BOM");
		log.info("CASE :  IMPORT BOM");
		RFQ_list();
		// openQuote();
		first_RFQ();
		waitforseconds(5);
		boolean importBOM = false;
		try {
			importBOM = CheckXpath("IMPORT_BOM").isDisplayed();
		} catch (Exception e) {

		}
		if (importBOM) {
			import_Steps();
			import_upload("importBOM.xlsx");
		} else {
			log.info("~>> BOM line items are already available");
			safeJavaScriptClick(CheckXpath("BOM_ACTIONS_BUTTON"));
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("DELETE_BOM"));
			waitforseconds(8);
			alertacceptpopup();
			waitforseconds(5);
			log.info("~>> BOM Deleted Successfully");
			driver.navigate().refresh();
			waitforseconds(10);
			boolean spinner = false;
			try {
				spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
			} catch (Exception e) {

			}
			if (spinner) {
				driver.navigate().refresh();
				waitforseconds(15);
			}
			import_Steps();
			import_upload("importBOM.xlsx");
		}
		waitforseconds(30);
		submitBOM();
		waitforseconds(15);
	}

	public void import_Steps() throws Exception {
		waitforseconds(10);
		log.info("~** Import BOM Using Widget");
		CheckXpath("IMPORT_BOM").click();
		log.info("~>> Clicked on Import BOM");
	}

	public void import_upload(String filename) throws Exception {
		waitforseconds(8);
		driver.switchTo().frame("widgetImportButton");
		File file = new File(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\" + filename);
		String absolute = file.getCanonicalPath();
		WebElement addfile = driver.findElement(By.xpath(".//input[@id='upfile']"));
		waitforseconds(5);
		addfile.sendKeys(absolute);
		waitforseconds(10);
		log.info("~>> File uploaded");
		waitforseconds(20);
		
		CheckXpath("IMPORT_UPLOAD_NEXT").click();
		log.info("~>> Clicked on NEXT button");
		waitforseconds(25);
		
		CheckXpath("VALIDATE_AND_IMPORT").click();
		log.info("~>> Clicked on VALIDATE AND  IMPORT button");
		// toastValidation("BOM is imported successfully");
		log.info("~>> RESULT :: BOM IMPORTED SUCCESSFULLY");
		System.out.println("~>> RESULT :: BOM IMPORTED SUCCESSFULLY");
		waitforseconds(8);
	}

	public void toastValidation(String msg) {
		if (CheckXpath("TOAST_ALERT").getText().contains(msg)) {
			Assert.assertTrue(true);
			log.info("RESULT-1 [VALIDATION] :: " + CheckXpath("TOAST_ALERT").getText());
		} else {
			Assert.assertTrue(false);
			log.info("RESULT-1 [VALIDATION] ::  Alert was not displayed");
		}
	}

	public static void uploadFile(String fileLocation) {
		try {
			StringSelection stringSelection = new StringSelection(fileLocation);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			Robot robot = new Robot();
			robot.delay(2000);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			waitforseconds(10);
			robot.keyPress(KeyEvent.VK_ENTER);
			waitforseconds(10);
			// robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public void submitBOM() throws Exception {
		log.info("================================================================================");
		driver.switchTo().defaultContent();
		waitforseconds(10);
		log.info("CASE  :  SUBMIT BOM");
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		log.info("~>> Submit button clicked");
		// waitforseconds(8);
		chkLaborupdate();
		alertacceptpopup();
		log.info("~>> RESULT :: BOM SUBMITTED");
		System.out.println("~>> RESULT :: BOM SUBMITTED");
		waitforseconds(10);
		driver.navigate().refresh();
		waitforseconds(8);
		log.info("================================================================================");
	}

	public void openQuote() throws InterruptedException {
		String id = CheckXpath("ASMNAME_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat("-0-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		waitforseconds(5);
		log.info("~>>" + id1);
	}

	public void chkLaborupdate() {
		boolean labor = false;
		try {

			labor = driver
					.findElement(By.xpath(".//div[@class='modal-header ng-scope']//h3[contains(text(),'Actions')]"))
					.isDisplayed();

			// labor = driver.findElement(By.xpath(".//label[@ng-repeat='Action in
			// ActionList']//input")).isDisplayed();
		} catch (Exception e) {

		}
		if (labor) {
			driver.findElement(By.xpath(".//button[@ng-click='submit()']")).click();
			log.info("~>> Labor Update Popup Accepted");
			waitforseconds(8);
		} else {
			log.info("~>> No Labor Update  Popup Displayed");
		}
	}
}
