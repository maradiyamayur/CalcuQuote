package RFQ;

import java.text.DecimalFormat;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.BaseInitquoteCQ;
import utility.UtilityMethods;

public class RFQ_DocUpload extends BaseInitRFQ {

	public static double start;
	public static double finish;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "RFQ")) {
			// Code to check execution mode of Test Case
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "[TC: 13355] - Attach document of SMALL size in RFQ")
	public void documentUploadSmall() throws Exception {
		headerFormat("#. CASE : DOCUMENT ATTACHMENT IN RFQ");
		log.info("~**  Attach small size of document in RFQ");
		System.out.println("~** ATTACH SMALL SIZE DOCUMENT");
		waitforseconds(5);
		BaseInitquoteCQ.RFQ_list();
		WebElement first_Quote = driver
				.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope']//a)[1]"));
		log.info("~>> Quote Id  :: " + first_Quote.getAttribute("innerText"));
		waitforseconds(5);
		driver.findElement(By.linkText(first_Quote.getAttribute("innerText"))).click();
		add_Document();
		waitforseconds(5);
		uploadFile("35LineBOM.xlsx");
		validation("Small");
		System.out.println("~>> File uploaded successfully");
		footerFormat();
	}

	@Test(priority = 2, description = "[TC:13356] - Attach document of BIG  size in RFQ")
	public void documentUpload() throws Exception {
		waitforseconds(3);
		System.out.println("~** ATTACH BIG SIZE DOCUMENT");
		log.info("~**  Attach big size of document in RFQ");
		waitforseconds(3);
		uploadFile("dummy.txt");
		validation("Big");
		System.out.println("~>> File uploaded successfully");
		footerFormat();
	}

	@Test(priority = 3, description = "[TC:NA] - Attach ZIP file document in RFQ")
	public void documentUploadZip() throws Exception {
		waitforseconds(3);
		log.info("~**  Attach Zip file document in RFQ");
		System.out.println("~** ATTACH ZIP FILE AS DOCUMENT");
		waitforseconds(3);
		uploadFile("BOM ManEx Data.zip");
		validation("Small");
		System.out.println("~>> File uploaded successfully");
		footerFormat();
	}

	@Test(priority = 4, description = "[TC:13876] - Attach INVALID document in RFQ")
	public void documentUploadInvalid() throws Exception {
		waitforseconds(3);
		log.info("~**  Attach Invalid document in RFQ");
		System.out.println("~** ATTACH INVALID FILE AS DOCUMENT");
		waitforseconds(3);
		uploadFile("chromedriver.exe");
		validation("Invalid");
		waitforseconds(2);
		CheckXpath("UPLOAD_CANCEL").click();
		footerFormat();
	}

	public void add_Document() throws InterruptedException {
		waitforseconds(8);
		log.info("~>> Click to Add Documents");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.linkText("Add Documents")));
		waitforseconds(6);
		WebElement add_document = CheckXpath("ADD_DOCUMENT");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click()", add_document);
		waitforseconds(5);
	}

	public static void validation(String type) throws InterruptedException {
		waitforseconds(3);
		if (type.equals("Big")) {
			boolean element = false;
			try {
				element = CheckXpath("UPLOAD_POPUP").isDisplayed();
			} catch (Exception e) {

			}
			if (element) {
				Assert.assertTrue(true);
				log.info("RESULT-1  :: Upload popup displayed");
				CheckXpath("UPLOAD_CLICK").click();
			} else {
				Assert.assertTrue(false);
				log.info("RESULT-1  :: Upload popup was not displayed");
			}
			waitforseconds(3);
			boolean progress_bar = false;
			try {
				progress_bar = driver.findElement(By.xpath("//div[@value='items.Progress']")).isDisplayed();
			} catch (Exception e) {

			}
			if (progress_bar) {
				start = System.currentTimeMillis();
				System.out.println("~>> Time started :: " + String.valueOf(start));
				log.info("~>> Progress Bar started");
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(950));
			
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")));
				log.info("~>> Progress Bar finished");
				if (driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).getText()
						.equals("File Uploaded successfully")) {
					Assert.assertTrue(true);
					log.info("RESULT-2 :: Toast Alert message displayed for upload file ");
					driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).click();
				} else {
					Assert.assertTrue(false);
					log.info("RESULT-2  ::  Toast Alert message was not displayed for upload file");
				}
			}
			log.info("progress bar  is inivisible now");
			finish = System.currentTimeMillis();
			System.out.println("~>> Time ended :: " + String.valueOf(finish));
			double totalTime = finish - start;
			double minutetime = (totalTime / 1000) / 60;
			System.out.println(">>> Total Time to upload file ::  " + new DecimalFormat("#.####").format(minutetime)
					+ " minute(s)");
			log.info("RESULT-3 :: Total Time to upload file  ::  " + new DecimalFormat("#.####").format(minutetime)
					+ " minute(s)");
			waitforseconds(3);
		} else if (type.equals("Small")) {
			waitforseconds(3);
			boolean element = false;
			try {
				element = CheckXpath("UPLOAD_POPUP").isDisplayed();
			} catch (Exception e) {

			}
			if (element) {
				Assert.assertTrue(true);
				log.info("RESULT-1  :: Upload popup displayed");
				CheckXpath("UPLOAD_CLICK").click();
			} else {
				Assert.assertTrue(false);
				log.info("RESULT-1  :: Upload popup was not displayed");
			}
			waitforseconds(3);
			if (driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).getText()
					.equals("File Uploaded successfully")) {
				Assert.assertTrue(true);
				log.info("RESULT-3 :: Toast Alert message displayed for upload file ");
				driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).click();
			} else {
				Assert.assertTrue(false);
				log.info("RESULT-3  ::  Toast Alert message was not displayed for upload file");
			}
			waitforseconds(3);
			log.info("~>> File uploaded successfully");
			waitforseconds(3);
		} else if (type.equals("Invalid")) {
			waitforseconds(8);
			if (CheckXpath("UPLOAD_POPUP").isDisplayed()) {
				log.info("RESULT-1  :: Upload popup displayed");
				Assert.assertTrue(true);
				CheckXpath("UPLOAD_CLICK").click();
			} else {
				log.info("RESULT-1  :: Upload popup was not displayed");
				Assert.assertTrue(false);
			}
			waitforseconds(5);
			log.info("~>> Check for valid file alert");
			if (driver.findElement(By.xpath(".//div[@id='swal2-content']")).getText()
					.contains("Please upload valid file(s) !")) {
				log.info("~>> RESULT :: " + driver.findElement(By.xpath(".//div[@id='swal2-content']")).getText());
				System.out.println(
						"~>> RESULT :: " + driver.findElement(By.xpath(".//div[@id='swal2-content']")).getText());
				Assert.assertTrue(true);
				waitforseconds(2);
				CheckXpath("INVALID_ALERT").click();
			} else {
				log.info("~>> RESULT ::  Invalid file alert was not displayed");
				System.out.println("~>> RESULT ::  Invalid file alert was not displayed");
				Assert.assertTrue(false);
			}
		}
	}
}
