package ImportWidget;

import java.time.Duration;


import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class IW_DemandLine extends BaseInitImportWidget {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ImportWidget")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	// TASK : 55516
	@Test(priority = 1, description = "#. CHECK VALIDATION WITH BLANK DEMANDLINE FILE IMPORT")
	public void blankfileMain() throws Exception {
		headerFormat("#. CASE : CHECK VALIDATION WITH BLANK DEMANDLINE FILE IMPORT");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("SHOP_CQ"));
		log.info("~>> Redirected to ShopCQ");
		System.out.println("~>> Redirected to ShopCQ");
		log.info("----------------------------------------------------------------------------");
		waitforseconds(8);
		importfile("DemandLine", "BOM_Blank.xlsx");
		validateBlankAlert();
	}

	// TASK : 55523
	@Test(priority = 2, description = "#. CHECK VALIDATIONS OF DEMANDLINE IMPORT))")
	public void demandLineMain() throws Exception {
		headerFormat("#. CASE : CHECK VALIDATIONS OF DEMANDLINE IMPORT");
		safeJavaScriptClick(CheckXpath("SHOP_CQ"));
		log.info("~>> Redirected to ShopCQ");
		System.out.println("~>> Redirected to ShopCQ");
		log.info("----------------------------------------------------------------------------");
		waitforseconds(8);
		importfile("DemandLine", "DL_ColumnMapping.xlsx");
		validateAlert();
		closeWindow();
		importfile("DemandLine", "DL_Main.xlsx");
		mappingScreen();
		addTags();
		IW_ImportValidation.validateError("DemandLine");
		waitforseconds(5);
		while (true) {
			boolean errormsg = false;
			try {
				errormsg = driver.findElement(By.xpath(".//div[@id='errorMessage']/ul/li")).isDisplayed();
				// CheckXpath("IMPORT_WIDGET_ERROR_MSG").isDisplayed();
			} catch (Exception e) {

			}
			if (errormsg) {
				log.info("SECOND IF");
				validationClear();
				IW_ImportValidation.validateError("DemandLine");
			} else {
				log.info("~>> Errors Cleared");
				break;
			}
		}
		IW_ImportVerificationDL.verifyImportedItem();
		// ImportVerification.clear();
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
		IW_DemandLine.closeWindow();
	}

	public void validateAlert() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		CheckXpath("IMPORT_UPLOAD_NEXT").click();
		log.info("~>> Clicked on NEXT button for mapping Screen");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		boolean alert = false;
		try {
			alert = driver.findElement(By.xpath(".//*[@id='swal2-content']")).isDisplayed();
		} catch (Exception e) {

		}
		if (alert) {
			if (driver.findElement(By.xpath(".//*[@id='swal2-content']")).getText()
					.contains("Please map the following required column(s) and try again: Mfgr,MPN,Requested Qty.")) {
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
			log.info("~>> Import Screen Closed");
		} else {
			log.info("~>> No popup displayed");
		}
	}

	public static void validationClear() {
		try {
			IW_ImportValidation.duplicateData.clear();
			IW_ImportValidation.hashlineitem.clear();
			IW_ImportValidation.hashMPN.clear();
		} catch (NullPointerException e) {
			log.info(e.getMessage());
		}
	}
}
