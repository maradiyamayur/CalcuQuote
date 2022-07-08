package ImportWidget;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import QuoteCQ.BaseInitquoteCQ;
import RFQ.RFQ_Validations;

public class IW_PKGRFQ extends BaseInitImportWidget {

	protected String Assembly_Number1;

	@Test(description = "#. HAPPY PATH :: QCQ - CREATE RFQ ")
	public void create(int pkg) throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("STEP 2 :  CREATE RFQ WITH SINGLE ASSEMBLY");
		BaseInitquoteCQ.RFQ_list();
		BaseInitquoteCQ.newRFQ();
		RFQ_Validations Rva = new RFQ_Validations();
		BaseInitquoteCQ BCQ = new BaseInitquoteCQ();
		BCQ.quoteInfo();
		BCQ.customer_Info();
		log.info("~>>5. Adding Valid data for Assembly");
		BCQ.add_Assembly(0, 1);
		BCQ.assembly_Qtyturn(0, 1, "15", "5", "ARO");
		waitforseconds(5);

		if (pkg == 1) {
			driver.findElement(By.xpath(".//*[@id='chkPkg_0']")).click();
			log.info("~>> Package quote Selected");
		}

		waitforseconds(3);
		log.info("~>> Click Submit Button");
		safeJavaScriptClick(CheckXpath("RFQ_SUBMIT"));
		BCQ.quoteIdexist();
		Rva.checklistBuilder();
		alertacceptpopup();
		BCQ.quoteIdexist();
		alertacceptpopup();
		log.info("~>> RESULT :: RFQ CREATED WITH ASSEMBLY NAME :: " + BaseInitquoteCQ.Add_Asm_name);
		System.out.println("~>> RESULT :: RFQ CREATED WITH ASSEMBLY NAME :: " + BaseInitquoteCQ.Add_Asm_name);
		log.info("================================================================================");
	}
}
