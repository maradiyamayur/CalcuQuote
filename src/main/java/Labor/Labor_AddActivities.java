package Labor;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import BOM.BaseInitBOM;
import QuoteCQ.QuoteCQ_ImportBOM;
import utility.UtilityMethods;

public class Labor_AddActivities extends BaseInitLabor {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "Labor")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "Add Labor activities")
	public void addActivities() throws Exception {
		BaseInitBOM.quoteOpen();
		boolean add_line = false;
		try {
			add_line = driver.findElement(By.xpath(".//i[@ng-click='addLineItem();']")).isDisplayed();
		} catch (Exception e) {

		}
		if (add_line) {
			log.info("~>> No line items are available");
			QuoteCQ_ImportBOM IMB = new QuoteCQ_ImportBOM();
			IMB.import_Steps();
			IMB.import_upload("importBOM.xlsx");
			// installcheck();
			add("Automation_Test(Do not Touch)*");
		} else {
			// installcheck();
			log.info("~>> Line items are available");
			add("Automation_Test(Do not Touch)*");
		}
	}

	public void add(String activity) throws Exception {
		log.info(" ** TESTCASE :: ADD LABOR ACTIVITIES");
		waitforseconds(5);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
			waitforseconds(15);
			safeJavaScriptClick(CheckXpath("LABOR_TAB"));
			log.info("~>> Redirected to Labor Tab ");
			waitforseconds(10);
			boolean selected = false;
			try {
				selected = CheckXpath("SELECTED_LABOR_ACTIVITIES").isDisplayed();
			} catch (Exception e) {

			}
			while (selected) {
				waitforseconds(8);
				safeJavaScriptClick(CheckXpath("REMOVE_ACTIVITES"));
				log.info("~>> Removed Selected activites");
				waitforseconds(5);
				List<WebElement> elements = driver
						.findElements(By.xpath(".//*[@id='bootstrap-duallistbox-selected-list_']//option"));
				// log.info(elements.size());
				if (elements.size() == 0) {
					break;
				}
			}
			waitforseconds(5);
			new Select(driver.findElement(By.xpath(".//select[@ng-model='LaborGroupActivityId']")))
					.selectByVisibleText(activity);
			log.info("~>> Labor Activity group selected ::  " + activity);
			boolean laborOp = false;
			try {
				laborOp = CheckXpath("LABOR_OPTIONS").isDisplayed();
			} catch (Exception e) {

			}
			if (laborOp) {
				log.info("~>> Labor Activites available for selected company currency ");
				boolean selectedact = false;
				try {
					selectedact = CheckXpath("LABOR_SELECTED_ACT").isDisplayed();
				} catch (Exception e) {
				}
				if (selectedact) {
					log.info("~>> Labor Activites are already selected ");
				} else {

					waitforseconds(8);
					if (CheckXpath("LABOR_ACT_MOVE").isDisplayed()) {
						safeJavaScriptClick(CheckXpath("LABOR_ACT_MOVE"));
						log.info("~>> Move all Labor acitivites ");
					} else {
						log.info("~>> Labor acitivites are disabled");
					}
					waitforseconds(5);
					WebElement laboractsubmit = CheckXpath("LABOR_ACT_SUBMIT");
					safeJavaScriptClick(laboractsubmit);
					log.info("~>> Submit all Labor activities ");
					waitforseconds(8);
					alertacceptpopup();
					safeJavaScriptClick(CheckXpath("LABOR_SUBMIT"));
					log.info("RESULT  :: ~>> Labor  Submitted");
					alertacceptpopup();
					safeJavaScriptClick(CheckXpath("LABOR_SUBMIT"));
					waitforseconds(8);
				}
			} else {
				log.info("~>> Labor Activites are not available for selected company currency");
			}
		}
	}
}
