package QuoteCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_AddLabor extends BaseInitquoteCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: QCQ - ADD LABOR ACTIVITIES AND SUBMIT")
	public void addMain() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE :  ADD LABOR ACTIVITIES");
		System.out.println("CASE :  ADD LABOR ACTIVITIES");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("LABOR_TAB"));
		log.info("~>> Redirected to Labor Tab ");
		add();
		laborsubmit();
	}

	public void add() throws Exception {

		waitforseconds(8);
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
					driver.findElement(By.xpath(".//*[@id='sourcelabourdriver']/option[1]")).click();
					waitforseconds(5);
					safeJavaScriptClick(CheckXpath("LABOR_MOVE_SELECTED"));
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
				log.info("~>> RESULT :: LABOR ACTIVITES ADDED");
				System.out.println("~>> RESULT :: LABOR ACTIVITES ADDED");
				checkact();
			}
		} else {
			log.info("~>> Labor Activites are not available for selected company currency");
		}

	}

	public void checkact() throws Exception {
		boolean selected = false;
		try {
			selected = driver.findElement(By.xpath(".//*[@id='bootstrap-duallistbox-selected-list_']/option"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (selected) {
			log.info("~>> Labor Activities are  already selected");
		} else {
			add();
		}
	}

	public void laborsubmit() throws Exception {
		log.info("================================================================================");
		log.info("#. CASE :  SUBMIT LABOR");
		System.out.println("#. CASE :  SUBMIT LABOR");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("LABOR_SUBMIT"));
		log.info("~>> RESULT  :: LABOR SUBMITTED");
		System.out.println("~>> RESULT  :: LABOR SUBMITTED");
		alertacceptpopup();
		waitforseconds(5);
		if (CheckXpath("LABOR_TAB").getAttribute("class").contains("ActivityStatusPending")) {
			safeJavaScriptClick(CheckXpath("LABOR_SUBMIT"));
			log.info("~>> RESULT  :: LABOR SUBMITTED");
			System.out.println("~>> RESULT  :: LABOR SUBMITTED");
			alertacceptpopup();
		}
		waitforseconds(8);
		log.info("================================================================================");
	}

}
