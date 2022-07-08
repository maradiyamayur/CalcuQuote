package ShopCQ;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import utility.UtilityMethods;

public class ShopCQ_ExportPO extends BaseInitshopCQ {
	// static String filename;

	static SoftAssert softAssertion = new SoftAssert();
	public static String fileName;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - EXPORT PO")
	public void exportPO() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : EXPORT PO");
		System.out.println("CASE : EXPORT PO");
		demandList();
		ShopCQ_POSent Ps = new ShopCQ_POSent();
		Ps.poList();
		CheckXpath("PO_STATUS_FILTER").clear();
		CheckXpath("PO_STATUS_FILTER").sendKeys("PO Sent");
		waitforseconds(8);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 3000", scrollbar);
		waitforseconds(5);
		boolean export = false;
		try {
			export = CheckXpath("PO_EXPORT_BUTTON").isDisplayed();
		} catch (Exception e) {

		}
		if (export) {
			getPO();
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("PO_EXPORT_BUTTON"));
			// CheckXpath("PO_EXPORT_BUTTON").click();
			log.info("~>> Export button clicked");
			waitforseconds(8);
			VerifyDownloadWithFileName(getPO(), ".xlsx");
		} else {
			log.info("~>> Export button is not available");
			System.out.println("~>> Export button is not available");
		}
	}

	public String getPO() throws InterruptedException {
		WebElement filterHeader = driver
				.findElement(By.xpath(".//span[@class='ui-grid-header-cell-label ng-binding' and text()='PO Number']"));
		String id = filterHeader.getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		waitforseconds(3);
		// log.info(id);
		// log.info(id1);
		WebElement POnumber = driver.findElement(By.xpath("(.//div[contains(@id,'" + id1 + "')]//div//span)[1]"));
		String filename = "Purchase Order_".concat(POnumber.getText());
		log.info("~>> Export file name :: " + filename);
		return filename;
	}

	public static void VerifyDownloadWithFileName(String filename, String extension) {
		File getLatestFile = getLatestFilefromDir();
		try {
			fileName = getLatestFile.getName();
			System.out.println("~>> Actual file name :: " + filename.toLowerCase());
			System.out.println("~>> Downloaded file name :: " + fileName.toLowerCase());
			// & fileName.contains(extension)
			if (fileName.toLowerCase().contains(filename.toLowerCase())) {
				softAssertion.assertTrue(true);
				log.info("~>> RESULT :: DOWNLOADED FILE NAME IS  MATCHING WITH EXPECTED FILE NAME");
				System.out.println("~>> RESULT :: DOWNLOADED FILE NAME IS MATCHING WITH EXPECTED FILE NAME");
				Assert.assertTrue(true);
			} else {
				softAssertion.assertTrue(false);
				log.info("~>> RESULT :: DOWNLOADED FILE NAME IS NOT MATCHING WITH EXPECTED FILE NAME");
				System.out.println("~>> RESULT ::  DOWNLOADED FILE NAME IS NOT MATCHING WITH EXPECTED FILE NAME");
				Assert.assertTrue(false);
			}
		} catch (NullPointerException e) {
			System.out.print(e.getMessage());
		}
	}

	private static File getLatestFilefromDir() {
		String home = System.getProperty("user.home");
		File dir = new File(home + "/Downloads/");
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}
}
