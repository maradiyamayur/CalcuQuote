package SmokeTest;

import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ShopCQ.ShopCQ_ExportPO;
import utility.UtilityMethods;

public class Reports extends BaseInitsmokeTest {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. CASE : GENERATE COSTED BOM REPORT - CONSOLIDATED COSTED BOM REPORT")
	public void costedBOM() throws Exception {
		chkOpenQuote();
		materialReport("Costed BOM", "Consolidated Costed BOM", "Consolidated Costed BOM", "Excel");
	}

	@Test(priority = 2, description = "#. CASE : GENERATE COSTED BOM REPORT - MULTI-LEVEL COSTED BOM REPORT")
	public void costedBOMMultiLevel() throws Exception {
		materialReport("Costed BOM", "Multi-level Costed BOM", "Multi-level Costed BOM", "Excel");
	}

	@Test(priority = 3, description = "#. CASE : GENERATE COSTED BOM REPORT - CONSOLIDATED COSTED BOM REPORT - ORIGINAL CURRENCY")
	public void costedBOMOrgCurrency() throws Exception {
		materialReport("Costed BOM", "Consolidated Costed BOM - Original Currency",
				"Consolidated Costed BOM - Original Currency", "Excel");
	}

	@Test(priority = 4, description = "#. CASE : GENERATE COSTED BOM REPORT - CONSOLIDATED COSTED BOM REPORT - ORIGINAL CURRENCY")
	public void costedBOMMultiOrgCurrency() throws Exception {
		materialReport("Costed BOM", "Multi-level Costed BOM - Original Currency",
				"Multi-Level Costed BOM  - Original Currency", "Excel");
	}

	@Test(priority = 5, description = "#. CASE : GENERATE COSTED BOM REPORT - CONSOLIDATED COSTED BOM REPORT - ALL")
	public void costedBOMAll() throws Exception {
		materialReport("Costed BOM", "All", "Consolidated Costed BOM", "PDF");
	}

	public void materialReport(String report, String subReport, String reportname, String format) throws Exception {
		headerFormat("#. CASE : CHECK REPORT DOWNLOAD ::  " + report + " - " + subReport);
		boolean bolReport = false;
		try {
			bolReport = CheckXpath("MATERIAL_REPORT_FORM").isDisplayed();
		} catch (Exception e) {

		}
		if (bolReport) {
			System.out.println("~>> Report form is already open");
			log.info("~>> Report form is already open");
		} else {
			safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
			log.info("~>> Redirected to Material Tab ");
			waitforseconds(10);
			safeJavaScriptClick(CheckXpath("MATERIAL_ACTION_BUTTON"));
			log.info("~>> Actions button clicked");
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("MATERIAL_REPORTS"));
			log.info("~>> Report Option Clicked");
			waitforseconds(5);
		}
		waitforseconds(5);
		new Select(CheckXpath("REPORT_NAME")).selectByVisibleText(report);
		log.info("~>> Report Selected :: " + report);
		waitforseconds(3);
		new Select(CheckXpath("REPORT_SUB_NAME")).selectByVisibleText(subReport);
		log.info("~>> Sub Report  Selected :: " + subReport);
		waitforseconds(3);
		new Select(CheckXpath("REPORT_FORMAT")).selectByVisibleText(format);
		log.info("~>> Report Format Selected :: " + format);
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("REPORT_DOWNLOAD"));
		System.out.println("~>> Report Download Button Clicked");
		log.info("~>> Report Download Button Clicked");
		waitforseconds(15);
		ShopCQ_ExportPO.VerifyDownloadWithFileName(reportname, ".xlsx");
	}
}