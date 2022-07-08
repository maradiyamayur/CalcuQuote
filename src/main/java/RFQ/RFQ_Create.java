package RFQ;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.BaseInitquoteCQ;
import utility.UtilityMethods;

public class RFQ_Create extends BaseInitRFQ {

	protected String Assembly_Number1;
	protected String[] read_Ids;
	private String quoteID, asmID, quoteid1, quoteid2, asmid1, asmid2;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "RFQ")) {
			// Code to check execution mode of Test Case
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "Create RFQ with Single Assembly")
	public void createSingle() throws Exception {
		log.info("~**  Create RFQ with Single Assembly");
		BaseInitquoteCQ.RFQ_list();
		waitforseconds(10);
		BaseInitquoteCQ.newRFQ();
		RFQ_Validations Rva = new RFQ_Validations();
		quoteInfo();
		customer_Info();
		log.info("~>>5. Adding Valid data for Assembly");
		add_Assembly(0, 1);
		assembly_Qtyturn(0, 1, "15", "5", "ARO");
		waitforseconds(5);
		log.info("~>> Click Submit Button");
		safeJavaScriptClick(CheckXpath("RFQ_SUBMIT"));
		Rva.checklistBuilder();
		log.info("RESULT-6 :: RFQ created with Assembly Name :: " + Add_Asm_name);
		quoteIdexist();
		verify_saveddata();
		Rva.edit_Validation();
	}

	@Test(priority = 2, description = "Verify the presence of Quote package feature")
	public void createPackage() throws InterruptedException {
		// LoginMaster LTC03 = new LoginMaster();
		// LTC03.frontLogin();
		log.info("~**  Verify the presence of Quote package feature");
		CheckXpath("FEATURE_NAME").clear();
		CheckXpath("FEATURE_NAME").sendKeys("EnableQuotePackages");
		verifyPackge();
	}

	@Test(priority = 3, description = "Create RFQ with Multiple Assembly")
	public void createMultiple() throws Exception {
		log.info("~**  Create RFQ with Multiple  Assembly");
		BaseInitquoteCQ.RFQ_list();
		waitforseconds(10);
		BaseInitquoteCQ.newRFQ();
		RFQ_Validations Rva = new RFQ_Validations();
		quoteInfo();
		waitforseconds(3);
		customer_Info();
		waitforseconds(3);
		WebElement myelement = CheckXpath("ADD_ASSEMBLY");
		JavascriptExecutor jse2 = (JavascriptExecutor) driver;
		jse2.executeScript("arguments[0].scrollIntoView()", myelement);
		myelement.click();

		// element_visibility(0, "QUOTE_ID", Add_QuoteID, "Quote Id", "");
		String[] asm_values = add_Assembly(0, 1);
		String assembly_Name1 = asm_values[0];
		String assembly_N01 = asm_values[1];
		assembly_Qtyturn(0, 1, "15", "5", "ARO");
		waitforseconds(5);

		asm_values = add_Assembly(1, 2);
		String assembly_Name2 = asm_values[0];
		String assembly_N02 = asm_values[1];

		assembly_Qtyturn(1, 2, "20", "10", "ARM");
		log.info("~>> Click Submit Button");
		safeJavaScriptClick(CheckXpath("RFQ_SUBMIT"));
		Rva.checklistBuilder();
		quoteIdexist();
		log.info("RESULT :: RFQ created with Assembly Name :: " + assembly_Name1);
		log.info("RESULT :: RFQ created with Assembly Name :: " + assembly_Name2);
		log.info("RESULT :: RFQ created with Assembly Number :: " + assembly_N01);
		log.info("RESULT :: RFQ created with Assembly Number :: " + assembly_N02);
		String[] asm_Ids = Verify_savedata(assembly_Name1, assembly_N01);
		quoteid1 = asm_Ids[0];
		asmid1 = asm_Ids[1];
		asm_Ids = Verify_savedata(assembly_Name2, assembly_N02);
		quoteid2 = asm_Ids[0];
		asmid2 = asm_Ids[1];
		verify_ReadID();
		Rva.edit_Validation();
	}

	// verify Assembly Number,Job type, Order type
	public void verify_saveddata() throws InterruptedException {
		waitforseconds(8);
		log.info("** Verifying the added data after save");
		log.info("~>> Searching with Assembly Name");
		CheckXpath("ASMNAME_FILER").clear();
		CheckXpath("ASMNAME_FILER").sendKeys(Add_Asm_name);
		waitforseconds(5);
		Assembly_Number1 = Add_Asm_no + " " + asm_Revision + ":1.1";
		log.info("~>> Verifying  Assembly Number,Job type, Order type");
		String getid = driver
				.findElement(
						By.xpath(".//div[text()='" + Add_Asm_name + "']//parent::div//parent::div[@role=\"gridcell\"]"))
				.getAttribute("id");
		String getid1 = getid.substring(0, Math.min(getid.length(), 16));
		Thread.sleep(2000);
		// List<WebElement> Arraylist = new ArrayList<>();
		List<WebElement> data = driver.findElements(By.xpath(
				".//div[starts-with(@id,'" + getid1 + "')]//div[@class='ui-grid-cell-contents ng-binding ng-scope']"));
		String[] saveArray = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			saveArray[i] = data.get(i).getText();
		}
		List<String> saveArrayList = Arrays.asList(saveArray);
		String[] addArray = { Assembly_Number1, "Consigned", "New" };

		for (int j = 0; j < addArray.length; j++) {
			if (saveArrayList.contains(addArray[j])) {
				log.info("RESULT-7 - Matched :: " + addArray[j]);
				Assert.assertTrue(true);

			} else {
				log.info("Not Macthed :: 	" + addArray[j]);
				Assert.assertTrue(false);
			}
		}
		log.info("----------------------------------------------------------------------------");
		waitforseconds(3);
	}

	public void verifyPackge() throws InterruptedException {
		waitforseconds(3);
		if (CheckXpath("PLUS_ICON").isDisplayed()) {
			CheckXpath("PLUS_ICON").click();
			waitforseconds(3);
			log.info("RESULT ::  FEATURE NAME :: " + CheckXpath("GET_PKG_QUOTE_NAME").getText() + " STATUS IS - "
					+ CheckXpath("GET_PKG_QUOTE_STATUS").getText());
			Assert.assertTrue(true);
		} else {
			log.info("RESULT ::   No Package Quote feature found");
			Assert.assertTrue(false);
		}

	}

	public String[] Verify_savedata(String name, String number) throws InterruptedException {
		log.info("** Verifying the added data after save");
		log.info("~>> Searching with Assembly Name");

		CheckXpath("ASMNAME_FILER").clear();
		CheckXpath("ASMNAME_FILER").sendKeys(name);
		waitforseconds(5);
		String Assembly_Number = number + " " + asm_Revision + ":1.1";
		log.info("~>> Verifying  Assembly Number,Job type, Order type");
		String getid = driver
				.findElement(By.xpath(".//div[text()='" + name + "']//parent::div//parent::div[@role=\"gridcell\"]"))
				.getAttribute("id");
		String getid1 = getid.substring(0, Math.min(getid.length(), 16));
		waitforseconds(2);

		// get quote id and assembly id of searched assembly name
		read_Ids = new String[2];
		quoteID = driver.findElement(By.xpath("(.//div[starts-with(@id,'" + getid1
				+ "')]//div[@class='ui-grid-cell-contents ng-scope']//a[@class='ui-grid-cell-contents ng-binding'])[1]"))
				.getText();
		log.info("Quote ID is :" + quoteID);
		read_Ids[0] = quoteID;
		asmID = driver.findElement(By.xpath("(.//div[starts-with(@id,'" + getid1
				+ "')]//div[@class='ui-grid-cell-contents ng-scope']//a[@class='ui-grid-cell-contents ng-binding'])[2]"))
				.getText();
		log.info("Assembly ID is :" + asmID);
		read_Ids[1] = asmID;

		// List<WebElement> Arraylist = new ArrayList<>();
		List<WebElement> data = driver.findElements(By.xpath(
				".//div[starts-with(@id,'" + getid1 + "')]//div[@class='ui-grid-cell-contents ng-binding ng-scope']"));

		String[] saveArray = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			saveArray[i] = data.get(i).getText();
			// System.out.println(data.get(i).getText());
		}

		List<String> saveArrayList = Arrays.asList(saveArray);
		String[] addArray = { Assembly_Number, "Consigned", "New" };

		for (int j = 0; j < addArray.length; j++) {
			if (saveArrayList.contains(addArray[j])) {
				log.info("Matched :: " + addArray[j]);
				Assert.assertTrue(true);
			} else {
				log.info("Not Macthed :: 	" + addArray[j]);
				Assert.assertTrue(false);
			}
		}
		return read_Ids;
	}

	public void verify_ReadID() {
		log.info("~>> Comparing Quote id for both assembly");
		if (quoteid1.equals(quoteid2)) {
			log.info("RESULT ::  Quote ID is same for both assembly");
			Assert.assertTrue(true);
		} else {
			log.info("RESULT ::  Quote ID is not same for both assembly");
			Assert.assertTrue(false);
		}
		log.info("~>> Comparing Assembly id for both assembly");
		if (asmid1.equals(asmid2)) {
			log.info("RESULT ::  Assembly ID is same for both assembly");
			Assert.assertTrue(false);
		} else {
			log.info("RESULT ::  Assembly ID is not same for both assembly");
			Assert.assertTrue(true);
		}
	}

}
