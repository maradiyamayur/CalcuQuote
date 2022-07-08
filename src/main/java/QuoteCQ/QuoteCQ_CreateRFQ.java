package QuoteCQ;

import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import RFQ.RFQ_Validations;
import utility.UtilityMethods;

public class QuoteCQ_CreateRFQ extends BaseInitquoteCQ {

	protected String Assembly_Number1;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			// Code to check execution mode of Test Case
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ - CREATE RFQ ")
	public void create() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE  :  CREATE RFQ WITH SINGLE ASSEMBLY");
		RFQ_list();
		newRFQ();
		RFQ_Validations Rva = new RFQ_Validations();
		quoteInfo();
		customer_Info();
		log.info("~>>5. Adding Valid data for Assembly");
		add_Assembly(0, 1);
		assembly_Qtyturn(0, 1, "15", "5", "ARO");
		waitforseconds(5);
		/*
		 * if (pkg == 1) { driver.findElement(By.xpath(".//*[@id='chkPkg_0']")).click();
		 * log.info("~>> Package quote Selected"); }
		 */
		waitforseconds(3);
		log.info("~>> Click Submit Button");
		safeJavaScriptClick(CheckXpath("RFQ_SUBMIT"));
		quoteIdexist();
		Rva.checklistBuilder();
		alertacceptpopup();
		quoteIdexist();
		alertacceptpopup();
		log.info("~>> RESULT :: RFQ CREATED WITH ASSEMBLY NAME :: " + Add_Asm_name);
		System.out.println("~>> RESULT :: RFQ CREATED WITH ASSEMBLY NAME :: " + Add_Asm_name);
		log.info("================================================================================");
	}
}
