package RFQ;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitRFQ extends BaseInit {

	public static String Add_QuoteID, Add_Asm_name, Add_Asm_no, Add_job, Add_order, Add_Customer, asm_Revision;

	@BeforeSuite
	public void checkExecutionModeTestSuite() throws IOException {
		startup();
		// Code to check execution mode of Test Suite
		if (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "RFQ", "TestSuite")) {
			throw new SkipException("Execution mode of the test suite is set to NO");
		}
	}

	// RFQ list open
	/*
	 * public static void RFQ_list() throws Exception {
	 * log.info("~>> Click on RFQ List icon"); WebElement RFQ_List =
	 * CheckXpath("RFQ_LIST"); safeJavaScriptClick(RFQ_List); waitforseconds(5);
	 * CheckXpath("RFQ_INPROGRESS").click(); Thread.sleep(3000); }
	 */

	// new RFQ form open
	/*
	 * public static void newRFQ() throws Exception { waitforseconds(10); WebElement
	 * new_RFQ = CheckXpath("NEW_RFQ"); safeJavaScriptClick(new_RFQ);
	 * log.info("~>> Click on new RFQ button"); }
	 */

	// element visibility check
	public void element_visibility(int type, String Path, String sendkey, String name, String visibletext)
			throws InterruptedException {
		boolean element = false;
		try {
			element = CheckXpath(Path).isDisplayed();
		} catch (Exception e) {

		}
		if (element) {

			// For general
			if (type == 0) {
				CheckXpath(Path).clear();
				CheckXpath(Path).sendKeys(sendkey);
				log.info("~>> " + name + " Added");
			}

			// For general
			else if (type == 1) {
				int getsize = new Select(CheckXpath(Path)).getOptions().size();
				if (getsize == 1) {
					log.info("~>> No option to select for " + name);
				} else {
					new Select(CheckXpath(Path)).selectByIndex(getsize - 1);
					log.info("~>> " + name + " Selected");
				}
			}

			// on deal selection , alert validation
			else if (type == 2) {
				int getsize = new Select(CheckXpath(Path)).getOptions().size();
				if (getsize == 1) {
					log.info("~>> No option to select for " + name);
				} else {
					new Select(CheckXpath(Path)).selectByIndex(getsize - 1);
					log.info("~>> " + name + " Selected");
					// RFQValidations Rva = new RFQValidations();
					// Rva.customer_validation();
				}
			}

			// For visible text selection
			else if (type == 3) {
				new Select(CheckXpath(Path)).selectByVisibleText(visibletext);
				log.info("~>> " + name + " Selected");
			}

			// company selection for USD currency
			else if (type == 4) {
				List<WebElement> options = driver.findElements(
						By.xpath(".//*[@id='UserCompanyDetailID']//option[@class='ng-binding ng-scope is-active']"));
				for (WebElement e : options) {
					String value = e.getAttribute("value");
					new Select(CheckXpath(Path)).selectByValue(value);
					waitforseconds(5);
					if (driver.findElement(By.xpath(".//span[@ng-click='openCurrencyConversionRatesPopup()']"))
							.getText().contains("USD")) {
						log.info("~>> Company Currency is USD");
						break;
					} else {
						log.info("continue");
						continue;
					}
				}
			} else

			{
				log.info("~>> " + name + " : : Not Available");
			}
		}
	}

	// add quote information
	public void quoteInfo() throws InterruptedException {
		Random randomGenerator = new Random();
		int Quote_ID = randomGenerator.nextInt(1000);
		Add_QuoteID = String.valueOf(Quote_ID);
		element_visibility(0, "QUOTE_ID", Add_QuoteID, "Quote Id", "");
		waitforseconds(2);
		element_visibility(3, "QI_COMPAY", "", "Company Profile", "Test_Payal");
		element_visibility(3, "QI_JOBTYPE", "", "Job Type", "Consigned");
		log.info("~>> Job Type selected ::  Consigned");
		// RFQValidations Rva = new RFQValidations();
		// Rva.defaultBlankDueDate();
		waitforseconds(2);
		new Select(CheckXpath("QI_ORDERTYPE")).selectByVisibleText("New");
		log.info("~>> Order Type selected :: New");
		CheckXpath("QI_EXPIRE_DAYS").clear();
		CheckXpath("QI_EXPIRE_DAYS").sendKeys("30");
		element_visibility(0, "QI_CUSTOMER_RFQ", "CR1234", "Customer RFQ", "");
		CheckXpath("QI_NOTES").sendKeys("Quote Notes");
		element_visibility(1, "QI_QUOTETYPE", "", "Quote Type", "");
		element_visibility(2, "QI_DEAL", "", "Deal", "");
		log.info("----------------------------------------------------------------------------");
	}

	// add customer details
	public void customer_Info() throws Exception {
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("CUST_NAME"));
		CheckXpath("CUST_NAME").clear();
		CheckXpath("CUST_NAME").sendKeys("triveni-tech.in");
		waitforseconds(5);
		WebElement link = driver.findElement(By.linkText("triveni-tech.in"));
		safeJavaScriptClick(link);
		// driver.findElement(By.linkText("triveni-tech.in")).click();
		log.info("~>> Customer Name selected :: triveni-tech.in");
		waitforseconds(4);
		int getsize = new Select(CheckXpath("CUST_CONTACT")).getOptions().size();
		new Select(CheckXpath("CUST_CONTACT")).selectByIndex(getsize - 1);
		// waitforseconds(5);
		log.info("~>> Customer Contact selected :: " + CheckXpath("CUST_SELECTED").getText().replaceAll("\\s", ""));
		log.info("----------------------------------------------------------------------------");
	}

	// add assembly details
	public String[] add_Assembly(int i, int no) throws InterruptedException {
		log.info("~>> Adding value for Assembly :: " + no);
		waitforseconds(5);
		String[] asm_values = new String[2];
		CheckXpath("ASSEMBLY_NAME_" + i).clear();
		CheckXpath("ASSEMBLY_NAME_" + i).sendKeys(autoId());
		Add_Asm_name = CheckXpath("ASSEMBLY_NAME_" + i).getAttribute("value");
		asm_values[0] = Add_Asm_name;
		log.info("~>> Assembly Name  Added :: " + Add_Asm_name);
		CheckXpath("ASSEMBLY_NO_" + i).clear();
		CheckXpath("ASSEMBLY_NO_" + i).sendKeys(autoId());
		Add_Asm_no = CheckXpath("ASSEMBLY_NO_" + i).getAttribute("value");
		asm_values[1] = Add_Asm_no;
		log.info("~>> Assembly Number Added :: " + Add_Asm_no);
		element_visibility(0, "ASSEMBLY_ID_" + i, autoId(), "Assembly ID", "");
		asm_Revision = CheckXpath("ASSEMLY_REVISION_CAPTION").getText();
		CheckXpath("ASSEMBLY_REVISION_" + i).clear();
		CheckXpath("ASSEMBLY_REVISION_" + i).sendKeys("1.1");
		log.info("~>> Assembly Revision Added ");
		CheckXpath("ASSEMBLY_NOTE_" + i).clear();
		CheckXpath("ASSEMBLY_NOTE_" + i).sendKeys("Assembly note");
		log.info("~>> Assembly Notes Added");
		return asm_values;
	}

	public void assembly_Qtyturn(int i, int no, String qty, String turntime, String turntimeref)
			throws InterruptedException {
		waitforseconds(3);
		log.info("~>> Enter  Quantity for Assembly " + no);
		CheckXpath("ASSEMBLY_QTY01_" + i).clear();
		waitforseconds(3);
		CheckXpath("ASSEMBLY_QTY01_" + i).sendKeys(qty);
		waitforseconds(3);
		log.info("~>> Quantity added  :: " + qty);
		CheckXpath("ASSEMBLY_TURNTIME01_" + i).clear();
		CheckXpath("ASSEMBLY_TURNTIME01_" + i).sendKeys(turntime);
		log.info("~>> Turntime Added :: " + turntime);
		element_visibility(3, "ASSEMBLY_TURNTIMEREF01_" + i, "", "Turntime Reference", turntimeref);
		log.info("----------------------------------------------------------------------------");
	}

	// generate random autoid for assembly name and number
	public String autoId() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String autoid = salt.toString();
		return autoid;
	}

	// upload document in RFQ
	public static void uploadFile(String fileLocation) throws Exception {
		waitforseconds(3);
		log.info("~>> Select file to upload");
		File a = new File(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\" + fileLocation);
		String absolute = a.getCanonicalPath();
		WebElement addfile = driver.findElement(By.xpath(".//input[@id='oeFileUpload'][2]"));
		addfile.sendKeys(absolute);
		waitforseconds(10);
	}

	public void quoteIdexist() throws Exception {
		boolean quoteid = false;
		try {
			quoteid = driver.findElement(By.xpath(".//div[@class='swal2-modal swal2-show']")).isDisplayed();
		} catch (Exception e) {
		}
		log.info("~>> Quote ID exist :: " + quoteid);
		waitforseconds(8);
		while (quoteid) {
			log.info(
					"While - " + driver.findElement(By.xpath(".//div[@class='swal2-modal swal2-show']")).isDisplayed());
			log.info("~>> Alert present");
			if (driver.findElement(By.xpath(".//div[@id='swal2-content']")).getText()
					.contains("Quote Id already exists")) {
				driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
				log.info("~>> Quote ID exists");
				waitforseconds(3);
				CheckXpath("QUOTE_ID").sendKeys("*");
				log.info("~>> Updating Quote ID");
				waitforseconds(5);
				safeJavaScriptClick(CheckXpath("RFQ_SUBMIT"));
				waitforseconds(3);
				WebElement tempDriver = null;
				try {
					tempDriver = driver.findElement(By.xpath(".//div[@class='swal2-modal swal2-show']"));
				} catch (Exception e) {

				}
				if (tempDriver != null && tempDriver.isDisplayed()) {
					continue;
				} else {
					break;
				}
			}
		}
	}

	public void getResult(String id) throws InterruptedException {
		String getid = driver
				.findElement(By.xpath(".//a[text()='" + id + "']//parent::div//parent::div[@role=\"gridcell\"]"))
				.getAttribute("id");
		String getid1 = getid.substring(0, Math.min(getid.length(), 16));
		// driver.findElement(By.xpath(".//div[starts-with(@id,'" + getid1 +
		// "')]//button//i")).click();
		System.out.println(getid1);
		waitforseconds(2);
		driver.findElement(By.xpath("//ul[2]/li/a/i")).click();
	}

	public boolean isAlertPresent() throws InterruptedException {
		try {
			driver.switchTo().alert();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		} // catch
	}

	public static void first_RFQ() throws Exception {
		Thread.sleep(5000);
		log.info("~>> Select RFQ");
		WebElement first_Quote = driver
				.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope']//a)[3]"));
		log.info("~>> Assembly Id  :: " + first_Quote.getAttribute("innerText"));
		Thread.sleep(10000);
		driver.findElement(By.linkText(first_Quote.getAttribute("innerText"))).click();
		WebElement first = driver.findElement(By.linkText(first_Quote.getAttribute("innerText")));
		safeJavaScriptClick(first);
	}

}
