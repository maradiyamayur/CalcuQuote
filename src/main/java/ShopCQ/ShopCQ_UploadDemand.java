package ShopCQ;

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

public class ShopCQ_UploadDemand extends BaseInitshopCQ {

	int i;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			// Code to check execution mode of Test Case
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - UPLOAD DEMAND")
	public void importMain() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		waitforseconds(8);
		log.info("CASE :  UPLOAD DEMAND");
		System.out.println("CASE :  UPLOAD DEMAND");
		waitforseconds(5);
		driver.findElement(By.xpath(".//a[@class='dropdown-toggle no-margin userdropdown']")).click();
		waitforseconds(5);
		driver.findElement(By.xpath(".//a[@ng-click='RedirectToHome()']")).click();
		// safeJavaScriptClick(CheckXpath("SHOP_CQ"));
		log.info("~>> Redirected to SHOPCQ");
		System.out.println("~>> Redirected to SHOPCQ");
		waitforseconds(5);
		boolean upload = false;
		try {
			upload = CheckXpath("UPLOAD_DEMAND").isDisplayed();
		} catch (Exception e) {

		}
		if (upload) {
			import_Steps();
			import_upload();
		} else {

		}
		waitforseconds(5);
	}

	public void import_Steps() throws Exception {
		waitforseconds(6);
		log.info("~** Upload Demand Using Widget");
		safeJavaScriptClick(CheckXpath("UPLOAD_DEMAND"));
		waitforseconds(8);
		log.info("~>> Clicked on upload demand");
	}

	public void import_upload() throws Exception {
		waitforseconds(5);
		driver.switchTo().frame("widgetImportButton");
		File file = new File(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\demandline.xlsx");
		String absolute = file.getCanonicalPath();
		WebElement addfile = driver.findElement(By.xpath(".//input[@id='upfile']"));
		addfile.sendKeys(absolute);
		waitforseconds(10);
		log.info("~>> File uploaded");
		System.out.println("~>> File uploaded");
		log.info("~>> File uploaded");
		waitforseconds(8);
		CheckXpath("IMPORT_UPLOAD_NEXT").click();
		log.info("~>> Clicked on NEXT button");
		waitforseconds(5);
		CheckXpath("VALIDATE_AND_IMPORT").click();
		log.info("~>> Clicked on VALIDATE AND  IMPORT button");
		log.info("~>> RESULT :: DEMAND LIST UPLOADED SUCCESSFULLY");
		System.out.println("~>> RESULT :: DEMAND LIST UPLOADED SUCCESSFULLY");
		waitforseconds(15);
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
			waitforseconds(1);
			robot.keyPress(KeyEvent.VK_ENTER);
			waitforseconds(1);
			// robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
