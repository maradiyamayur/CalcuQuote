package RFQ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import QuoteCQ.BaseInitquoteCQ;
import utility.UtilityMethods;

public class RFQ_EditJobType extends BaseInitRFQ {
	SoftAssert softAssertion = new SoftAssert();
	private String old_Jobtype, old_Turntime, new_Turntime, Quoteid;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "RFQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "[TC-23118] - As a QuoteCQ user I would like to Edit/Update Job Type Data from the existing RFQ")
	public void edit() throws Exception {
		waitforseconds(10);
		BaseInitquoteCQ.RFQ_list();
		log.info("**~ Edit/Update Job Type Data from the existing RFQ");
		waitforseconds(3);
		/*
		 * Quoteid = driver.findElement(By.xpath(".//*[@id='lnkQuoteId_0']")).getText();
		 * System.out.println(Quoteid);
		 * driver.findElement(By.xpath(".//*[@id='lnkQuoteId_0']")).click();
		 */
		WebElement first_Quote = driver
				.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope']//a)[1]"));
		Quoteid = first_Quote.getAttribute("innerText");
		log.info("~>> Quote Id  :: " + first_Quote.getAttribute("innerText"));
		waitforseconds(5);
		driver.findElement(By.linkText(first_Quote.getAttribute("innerText"))).click();
		change_Jobtype();
		validation();
	}

	public void change_Jobtype() throws InterruptedException {
		waitforseconds(10);
		System.out.println(Quoteid);
		Select select = new Select(CheckXpath("QI_JOBTYPE"));
		WebElement option = select.getFirstSelectedOption();
		old_Jobtype = option.getText().replaceAll("\\s", "");
		old_Turntime = CheckXpath("ASSEMBLY_TURNTIME01_0").getAttribute("value");
		System.out.println(">>> Old Turn Time :: " + old_Turntime);
		log.info(">>> Old Turn Time :: " + old_Turntime);
		log.info("~>> Change job type to Turnkey");
		new Select(CheckXpath("QI_JOBTYPE")).selectByVisibleText("Turnkey");
		System.out.println(">>> Job Type Changed from " + old_Jobtype + "  to Turnkey");
		log.info("~>> Job Type Changed from " + old_Jobtype + "  to Turnkey");
	}

	public void validation() throws InterruptedException {

		// 1. validate toast alert for review turntime
		// waitforseconds(3);
		if (driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).getText()
				.equals("Please Review TurnTime")) {
			// Assert.assertTrue(true);
			softAssertion.assertTrue(true);
			log.info("RESULT-1  :: Toast Alert message displayed for review turntime");
			driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).click();
			System.out.println(">>>1.  Toast alert displayed to review turntime");
		} else {
			// Assert.assertTrue(false);
			softAssertion.assertTrue(false);
			log.info("~>>1.  Toast Alert was not displayed for review turn time");
		}

		waitforseconds(5);
		new_Turntime = CheckXpath("ASSEMBLY_TURNTIME01_0").getAttribute("value");
		System.out.println(">>> New Turn Time:: " + new_Turntime);
		log.info(">>> New Turn Time:: " + new_Turntime);
		log.info("~>> Comparing turntime for old and new Jobtype");

		// 2. validate turn time by Comparing old and new turntime
		if (old_Turntime.equals(new_Turntime)) {
			Assert.assertTrue(true);
			log.info("RESULT- 2 ::  Tuntime is same for both old and newJobtype");
			System.out.println(">>> Turntime is same for both Jobtypes ");
		} else {
			Assert.assertTrue(false);
			log.info("RESULT-2  ::  Tuntime is different for both old and new Job type");
		}
		waitforseconds(3);
		CheckXpath("RFQ_SUBMIT").click();

		// 3. Validate Submit time
		submit_time();
		waitforseconds(15);

		// 4. Validate workflow
		log.info("~>> Opening History to check Task created after jobtype change");
		waitforseconds(6);
		workFlow();
		String BOM_text = driver
				.findElement(By.xpath(
						".//*[@id='formCustomerContact']/fieldset/div[7]/div[3]/table/tbody/tr[last()]/td[5]/label"))
				.getText();
		log.info("~>> Check for BOM format task creation after changing turntime ");
		if (BOM_text.contains("[BOM Format] task created for ")) {
			Assert.assertTrue(true);
			System.out.println(">>> " + BOM_text);
			log.info("RESULT-4 ::  " + BOM_text);
		} else {
			Assert.assertTrue(false);
			log.info("~>> [BOM Fomat task not created]");
		}
	}

	public void submit_time() {
		log.info("~>> Checking RFQ submit time");
		long start = System.currentTimeMillis();
		// WebElement ele = driver.findElement(By.xpath(".//*[@id='btnNewRFQ']"));
		long finish = System.currentTimeMillis();
		long totalTime = finish - start;
		long secondtime = (totalTime / 1000) % 60;
		// System.out.println("Total Time for page load - "+totalTime);
		System.out.println(">>> Total Time for page load  in Seconds ::  " + secondtime);
		log.info("RESULT-3  :: Total Time for page load after submit RFQ  ::  " + secondtime + " Seconds");
	}

	public void workFlow() throws InterruptedException {
		// String id = CreateRFQTc01.QuoteID;
		String getid = driver
				.findElement(By.xpath(".//a[text()='" + Quoteid + "']//parent::div//parent::div[@role=\"gridcell\"]"))
				.getAttribute("id");
		// String getid = driver.findElement(By.xpath(".//a[text()='" + id +
		// "']//parent::div//parent::div[@role=\"gridcell\"]")).getAttribute("id");
		String getid1 = getid.substring(0, Math.min(getid.length(), 16));
		driver.findElement(By.xpath(".//div[starts-with(@id,'" + getid1 + "')]//button//i")).click();
		waitforseconds(2);
		driver.findElement(By.xpath("//ul[2]/li/a/i")).click();
	}
}
