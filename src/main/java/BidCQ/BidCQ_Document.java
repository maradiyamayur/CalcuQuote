package BidCQ;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import RFQ.BaseInitRFQ;
import RFQ.RFQ_DocUpload;
import ShopCQ.ShopCQ_ExportPO;
import utility.UtilityMethods;

public class BidCQ_Document extends BaseInitbidCQ {

	ArrayList<String> arrBid = new ArrayList<String>();

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "BidCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. BIDCQ DOCUMENT UPLOAD")
	public void documentUpload() throws Exception {
		headerFormat("#. CASE : DOCUMENTS UPLOAD ON BID");
		windowHandle();
		if (getHost()[0].contains("app.calcuquote.com")) {
			driver.get("https://trial.calcuquote.com/#/Dashboard");
		} else {
			driver.get("https://" + getHost()[0] + "/BidCQIdentity/#/");
		}
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("BID_SUMMARY_OPEN"));
		openBid(1);
	}

	@Test(priority = 2, description = "#. BIDCQ DOCUMENT UPLOAD : SMALL")
	public void docUpload1() throws Exception {
		System.out.println(
				".................................................................................................................");
		boolean documents = false;
		try {
			documents = CheckXpath("BID_DOC_WINDOW").isDisplayed();
		} catch (Exception e) {

		}
		if (documents) {
			log.info("**  Attach Small size document");
			System.out.println("**  Attach Small size document");
			waitforseconds(3);
			BaseInitRFQ.uploadFile("35LineBOM.xlsx");
			RFQ_DocUpload.validation("Small");
		} else {
			System.out.println("~>> No Quote Available for document upload");
		}
	}

	@Test(priority = 3, description = "#. BIDCQ DOCUMENT UPLOAD : ZIP")
	public void docUpload2() throws Exception {
		System.out.println(
				".................................................................................................................");
		boolean documents = false;
		try {
			documents = CheckXpath("BID_DOC_WINDOW").isDisplayed();
		} catch (Exception e) {

		}
		if (documents) {
			log.info("** Attach Zip file");
			System.out.println("** Attach Zip file");
			waitforseconds(3);
			BaseInitRFQ.uploadFile("BOM ManEx Data.zip");
			RFQ_DocUpload.validation("Small");
		}
	}

	@Test(priority = 4, description = "#. BIDCQ DOCUMENT UPLOAD : BIG")
	public void docUpload3() throws Exception {
		System.out.println(
				".................................................................................................................");
		boolean documents = false;
		try {
			documents = CheckXpath("BID_DOC_WINDOW").isDisplayed();
		} catch (Exception e) {

		}
		if (documents) {
			log.info("**> Attach Big size document");
			System.out.println("** Attach Big size document");
			waitforseconds(3);
			BaseInitRFQ.uploadFile("dummy.txt");
			RFQ_DocUpload.validation("Small");
		}
	}

	@Test(priority = 5, description = "#. BIDCQ DOCUMENT UPLOAD : INVALID")
	public void docUpload4() throws Exception {
		System.out.println(
				".................................................................................................................");
		boolean documents = false;
		try {
			documents = CheckXpath("BID_DOC_WINDOW").isDisplayed();
		} catch (Exception e) {

		}
		if (documents) {
			log.info("**  Attach Invalid Document");
			System.out.println("**  Attach Invalid Document");
			waitforseconds(3);
			BaseInitRFQ.uploadFile("chromedriver.exe");
			RFQ_DocUpload.validation("Invalid");
			waitforseconds(5);
			CheckXpath("UPLOAD_CANCEL").click();
		}
	}

	@Test(priority = 6, description = "#. DOWNLOAD UPLOADED FILE AND VERIFY IT")
	public void downloadFiles() throws Exception {
		waitforseconds(10);
		System.out.println(
				".................................................................................................................");
		log.info("**  Download attached files");
		System.out.println("**  Download attached files");
		boolean uploads = false;
		try {
			uploads = driver.findElement(By.xpath(".//ul[@class='jstree-children']")).isDisplayed();
		} catch (Exception e) {

		}
		if (uploads) {
			List<WebElement> lstfiles = driver.findElements(By.xpath(".//ul[@class='jstree-children']//li//a"));
			log.info("~>> Total numbers of file to download :: " + lstfiles.size());
			for (WebElement elefile : lstfiles) {
				safeJavaScriptClick(elefile);
				String dwlFile = elefile.getAttribute("innerText");
				System.out.println("~>> File name  :: " + dwlFile);
				arrBid.add(dwlFile);
				safeJavaScriptClick(CheckXpath("BID_DOC_MENU"));
				waitforseconds(5);
				driver.findElement(By.xpath(".//i[@class='fa fa-download']//parent::a")).click();
				waitforseconds(20);
				ShopCQ_ExportPO.VerifyDownloadWithFileName(dwlFile, "");
				waitforseconds(5);
				System.out.println("~>> Uploaded Documents on BidCQ :: " + arrBid);
			}
			safeJavaScriptClick(CheckXpath("BID_DOC_CLOSE"));
			safeJavaScriptClick(CheckXpath("BID_SHEET_SUBMIT"));
			log.info("~>> Clicked on Submit button");
			System.out.println("~>> Bid Submitted");
			waitforseconds(10);
			driver.switchTo().activeElement();
			CheckXpath("BID_SHEET_AUTHORIZED_BY").sendKeys("Payal Nanavati");

			log.info("~>> Quote ID :: " + quoteId);
			CheckXpath("BID_SHEET_QUOTE_NO").sendKeys(quoteId);
			safeJavaScriptClick(CheckXpath("BID_SHEET_SUBMIT_BTN"));
			alertacceptpopup();
		} else {
			log.info("~>> No Quote to Upload document");
			System.out.println("~>> No Quote to Upload document");
		}

	}

	@Test(priority = 7, description = "#. VERIFY UPLOADED FILES FROM CQPS")
	public void verifyUploadedFiles() throws Exception {
		System.out.println(
				".................................................................................................................");
		log.info("**  Verifing file name in CQPS");
		System.out.println("**  Verifing file name in CQPS");
		((JavascriptExecutor) driver).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		String url = "https://" + getHost()[0] + getHost()[1];
		if (quoteId != null) {
			driver.get(url + "/#/RFQ/" + quoteId + "/Assembly/" + asmId + "/Purchase");
			waitforseconds(10);
			driver.get(url + "/#/RFQ/" + quoteId + "/Assembly/" + asmId + "/Purchase");
			// driver.get("https://qa.calcuquote.com/Staging2/#/RFQ/" + quoteId +
			// "/Assembly/" + asmId + "/Purchase");
			waitforseconds(10);
			driver.findElement(By.xpath("(.//input[contains(@class,'ui-grid-filter-input-0')])[1]")).sendKeys(lineItem);
			waitforseconds(35);
			if (driver.findElement(By.xpath(".//i[contains(@class,'paperclip padding')]")).isDisplayed()) {
				log.info("~>> Document is attached on MC screen");
				System.out.println("~>> Document is attached on MC screen");
				driver.findElement(By.xpath(".//i[contains(@class,'paperclip padding')]")).click();
				waitforseconds(5);
				List<WebElement> lstPurchase = driver.findElements(
						By.xpath(".//div[@class='popover-content']//ul[@class='jstree-children']//li//a"));
				ArrayList<String> arrPurchase = new ArrayList<String>();
				log.info(lstPurchase);
				for (WebElement ele : lstPurchase) {
					arrPurchase.add(ele.getText());
				}
				System.out.println("~>> Uploaded Documents on Material Costing  :: " + arrPurchase);
				if (arrBid.equals(arrPurchase)) {
					System.out.println("~>> RESULT :: Correct files displayed ");
					log.info("~>> RESULT :: Correct files displayed");
					Assert.assertTrue(true);
				} else {
					System.out.println("~>> RESULT :: Wrong files displayed");
					log.info("~>> RESULT :: Wrong files displayed");
					Assert.assertTrue(false);
				}
			} else {
				System.out.println("~>> Document is not attached on MC screen");
				log.info("~>> Document is not attached on MC screen");
				Assert.assertTrue(false);
			}
		} else {
			System.out.println("~>> No documents uploaded for quotes");
		}
	}
}