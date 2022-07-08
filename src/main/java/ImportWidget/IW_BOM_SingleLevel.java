package ImportWidget;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class IW_BOM_SingleLevel extends BaseInitImportWidget {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ImportWidget")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	// TASK : 53163
	@Test(description = "#. CHECK VALIDATION WITH BLANK BOM FILE IMPORT")
	public void blankfile() throws Exception {
		headerFormat("#. CASE : CHECK VALIDATION WITH BLANK BOM FILE IMPORT");
		quoteChecker();
		importfile("BOM", "BOM_Blank.xlsx");
		validateBlankAlert();
		// ImportVerification.clear();
	}

	// TASK : 53159
	@Test(description = "#. VERIFY TO ALLOW IMPORT WIDGET WITH NULL MPN.")
	public void nullMPN() throws Exception {
		headerFormat("#. CASE : VERIFY TO ALLOW IMPORT WIDGET WITH NULL MPN.");
		quoteChecker();
		importfile("BOM", "BOM_NullMPN.xlsx");
		mappingScreen();
		validateimport();
		validatesubmiterror();
	}

	// TASK : 53173
	@Test(description = "#. CHECK VALIDATIONS FOR SINGLE LEVEL BOM")
	public void sameQtyMain() throws Exception {
		driver.switchTo().defaultContent();
		headerFormat("#. CASE : CHECK VALIDATIONS FOR SINGLE LEVEL BOM");
		quoteChecker();
		IW_ImportValidation.getAsmNo();
		checkLineItems(); 
		importfile("BOM", "BOM_ColumnMapping.xlsx");
		validateAlert(); 
		closeWindow(); 
		checkLineItems();
		importfile("BOM", "BOM_SingleLevel.xlsx");
		mappingScreen();
		addAsmNo();
		IW_ImportValidation.inputData(6, "", "ca");
		IW_ImportValidation.validateError("BOM");
		waitforseconds(5);
		IW_ImportValidation.gotoError();

		boolean validate = false;
		boolean goToError = false;
		try {
			validate = driver.findElement(By.xpath(".//*[@id='btnValidateAndImport']")).isDisplayed();
			goToError = driver.findElement(By.xpath(".//*[@id='btnNavigateToError']")).isDisplayed();
		} catch (Exception e) {

		}

		if (goToError || validate) {
			System.out.println("*** SECOND IF");
			IW_ImportVerification.validationclear();
			IW_ImportValidation.validateError("BOM");
		}
		IW_ImportVerification.verifyImportedItem();
		IW_ImportVerification.verifyImportedPartClass();
	}

	public void addAsmNo() {
		waitforseconds(8);
		String parentcolNumber = driver
				.findElement(
						By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'Parent Part')]"))
				.getAttribute("data-x");

		String MPNcolNumber = driver
				.findElement(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[contains(@title,'MPN')]"))
				.getAttribute("data-x");

		WebElement eleparent = driver.findElement(
				By.xpath("(.//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + parentcolNumber + "'])[2]"));
		WebElement eleMPN = driver.findElement(
				By.xpath("(.//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + MPNcolNumber + "'])[3]"));

		waitforseconds(3);
		Actions actions = new Actions(driver);
		actions.moveToElement(eleparent).doubleClick().perform();
		waitforseconds(3);
		actions.sendKeys(Keys.BACK_SPACE);
		for (int i = 1; i <= 15; i++) {
			actions.sendKeys(Keys.BACK_SPACE);
		}
		actions.sendKeys(IW_ImportValidation.StrasmNum).perform();
		waitforseconds(2);
		driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
		waitforseconds(3);
		actions.moveToElement(eleMPN).doubleClick().perform();
		waitforseconds(3);
		actions.sendKeys(Keys.BACK_SPACE);
		for (int i = 1; i <= 15; i++) {
			actions.sendKeys(Keys.BACK_SPACE);
		}
		actions.sendKeys(IW_ImportValidation.StrasmNum).perform();
		driver.findElement(By.xpath(".//input[@class='jexcel_search']")).click();
	}

	public static void validateAlert() {
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
			System.out.println("~>> Alert Displayed");
			if (driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText()
					.contains("Please map the following required column(s) and try again")) {
				Assert.assertTrue(true);
				log.info("~>> RESULT :: ALERT DISPLAYED ::  "
						+ driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText());
				System.out.println("~>> RESULT :: ALERT DISPLAYED :: "
						+ driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText());
			} else {
				Assert.assertTrue(false);
				log.info("~>> RESULT :: ALERT  NOT DISPLAYED ");
				System.out.println("~>> RESULT :: ALERT NOT DISPLAYED");
			}
		}
	}

	public static void closeWindow() throws Exception {
		driver.switchTo().defaultContent();
		safeJavaScriptClick(CheckXpath("MAPPING_WINDOW_CLOSE"));
		log.info("~>> Close Button Clicked");
		boolean popup = false;
		try {
			popup = CheckXpath("MAPPING_WINDOW_POPUP_LEAVE").isDisplayed();
		} catch (Exception e) {

		}
		if (popup) {
			log.info("~>>Popup displayed");
			CheckXpath("MAPPING_WINDOW_POPUP_LEAVE").click();
			log.info("~>> Import Screen Leaved");
		} else {
			log.info("~>> No popup displayed");
		}
	}

	public void validatesubmiterror() throws Exception {
		waitforseconds(5);
		driver.switchTo().defaultContent();
		WebElement BOMsubmit = CheckXpath("BOM_SUBMIT");
		safeJavaScriptClick(BOMsubmit);
		log.info("~>> Clicked on submit");
		boolean alert = false;
		try {
			alert = driver.findElement(By.xpath(".//div[@class='swal2-modal swal2-show']")).isDisplayed();
		} catch (Exception e) {

		}
		if (alert) {
			log.info("~>> Alert Displayed");
			String actMsg = driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText();
			if (actMsg.contains("You must have at least one MPN associated with each line.")) {
				softAssertion.assertTrue(true);
				log.info("~>> RESULT ::  CORRECT ERROR MESSAGE DISPLAYED - " + actMsg);
				System.out.println("~>> RESULT ::  CORRECT ERROR MESSAGE DISPLAYED - " + actMsg);
			} else {
				softAssertion.assertTrue(false);
				log.info("~>> RESULT ::  WRONG ERROR MESSAGE DISPLAYED - " + actMsg);
				System.out.println("~>> RESULT ::  WRONG ERROR MESSAGE DISPLAYED - " + actMsg);
			}
			driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
		} else {
			log.info("~>> No Alert Displayed");
		}
	}

}
