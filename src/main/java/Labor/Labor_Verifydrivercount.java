package Labor;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import BOM.BOM_Properties;
import BOM.BaseInitBOM;
import QuoteCQ.QuoteCQ_ImportBOM;
import utility.UtilityMethods;

public class Labor_Verifydrivercount extends BaseInitLabor {
	BOM_Properties BP = new BOM_Properties();
	int sum = 0;
	int drcount = 0;
	SoftAssert softAssertion = new SoftAssert();
	double oldDr, newDr, drcountPanel;
	private String partClass;
	private String oldpartClass;
	double total = 0;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "Labor")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "Verify driver count after changing Qty Per. ")
	public void driverCountQty() throws Exception {
		log.info("------------------------------------------------------------------------------------");
		log.info("** TESTCASE :: VERIFY DRIVER COUNT AFTER CHANGING QTY PER.");
		BaseInitBOM.quoteOpen();
		Thread.sleep(8000);
		boolean importBOM = false;
		try {
			importBOM = CheckXpath("IMPORT_BOM").isDisplayed();
		} catch (Exception e) {

		}
		if (importBOM) {
			QuoteCQ_ImportBOM IMB = new QuoteCQ_ImportBOM();
			IMB.import_Steps();
			IMB.import_upload("importBOM.xlsx");
		} else {
			waitforseconds(8);
			log.info("~>> BOM line items are already available");
		}
		safeJavaScriptClick(CheckXpath("LABOR_TAB"));
		log.info("~>> Labor Tab Clicked");
		waitforseconds(5);
		oldDr = driverCount("Old", 0, 1);
		waitforseconds(3);
		WebElement BOMtab = CheckXpath("BOM_TAB");
		safeJavaScriptClick(BOMtab);
		log.info("~>> Redirected to BOM Tab");
		if (isAlertPresent()) {
			driver.switchTo().alert().accept();
		}
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation in progress");
		} else {
			validation();
		}
	}

	@Test(priority = 2, description = "Verify driver count after changing part class")
	public void driverCountPartClass() throws Exception {
		log.info("TESTCASE :: VERIFY DRIVER COUNT AFTER CHANGING PART CLASS");
		waitforseconds(5);
		BaseInitBOM.quoteOpen();
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			submit(1);
			submit(2);
			cancel();
			uncheckeditbtn();
			uncheckinline();
			submitchange();
		}
	}

	@Test(priority = 3, description = "Verify Driver Count after changing Panel Count")
	public void driverCountPanelCount() throws Exception {
		log.info("TESTCASE :: VERIFY DRIVER COUNT AFTER CHANGING PANEL COUNT");
		waitforseconds(5);
		BaseInitBOM.quoteOpen();
		submit(1);
		submit(2);
		waitforseconds(8);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			pcbpartclass();
			waitforseconds(8);
		}
		waitforseconds(8);
		boolean panel = false;
		try {
			panel = CheckXpath("PRINTED_CIRCUIT").isDisplayed();
		} catch (Exception e) {

		}
		if (panel) {
			log.info("~>> PCB panel displayed");
			oldDr = LabordriverPanel();
			log.info("~>> Old driver count is :: " + oldDr);
			waitforseconds(8);
			safeJavaScriptClick(CheckXpath("BOM_TAB"));
			log.info("~>> Clicked on BOM Tab");
			waitforseconds(8);
			paneldigitvalidation();
			negativeValue();
			alphabetValue();
			SpecialChar();
			panelcount();
		} else {
			log.info("~>> PCB panel not displayed");
		}
	}

	@Test(priority = 4, description = "Verify driver count after changing No of lead.")
	public void addMain() throws Exception {
		log.info("TESTCASE :: VERIFY DRIVER COUNT AFTER CHANGING NO OF LEAD");
		waitforseconds(5);
		BaseInitBOM.quoteOpen();
		negativeupdate();
		alphanumeraicupdate();
		oldDr = LabordriverLead();
		log.info("~>> Old Driver count is :: " + oldDr);
		alertacceptpopup();
		updatesuccess();
		calculation();
		verifydriverLead();
	}

	@Test(priority = 5, description = " Verify driver count after changing Part Type.")
	public void driverCountPartType() throws Exception {
		log.info("TESTCASE :: VERIFY DRIVER COUNT AFTER CHANGING PART TYPE");
		waitforseconds(5);
		BaseInitBOM.quoteOpen();
		oldDr = LabordriverPartType();
	}

	public void validation() throws Exception {
		log.info("** CASE 1 : CHECK WHETHER DRIVER COUNT CHANGED OR NOT ?");
		qtyper();
		int total = qtyperTotal();
		log.info("~>> Qtyper Total is :: " + total);
		waitforseconds(10);
		int newDr = Labordriver();
		log.info("~>> New Driver count is : " + newDr);
		if (oldDr != newDr) {
			log.info("RESULT 1 :: Driver count changed after Qty Per Update - PASS");
			softAssertion.assertTrue(true);
		} else {
			log.info("RESULT 1 :: Driver count not changed after Qty Per Update - FAIL");
			softAssertion.assertTrue(false);
		}
		waitforseconds(20);
		log.info("** CASE 2 : VERIFY DRIVER COUNT CALCULATION (Sum of Qty per of all BOM items)");
		if (total == newDr) {
			log.info("RESULT 2 :: Driver count is same - PASS");
			softAssertion.assertTrue(true);
		} else {
			log.info("RESULT 2 :: Driver count is different - FAIL");
			softAssertion.assertTrue(false);
		}
	}

	public double driverCount(String name, int number, int Activity) throws Exception {
		Labor_AddActivities AddLb = new Labor_AddActivities();
		AddLb.add("Automation_Test(Do not Touch)*");
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		waitforseconds(8);
		String oldDC = CheckXpath("DRIVER_COUNT_" + number).getAttribute("value");
		double oldDr = Double.parseDouble(oldDC);
		log.info(
				name + " driver count for Activity >> "
						+ driver.findElement(
								By.xpath(".//*[@id='gridLabourActivity']/tbody/tr[" + Activity + "]/td[2]")).getText()
						+ " :: " + oldDC);
		WebElement Laborsubmit = CheckXpath("LABOR_SUBMIT");
		je.executeScript("arguments[0].scrollIntoView(true);", Laborsubmit);
		safeJavaScriptClick(Laborsubmit);
		log.info("~>> Labor submitted");
		waitforseconds(5);
		// alertacceptpopup();
		if (isAlertPresent()) {
			driver.switchTo().alert().accept();
		}
		waitforseconds(8);
		return oldDr;
	}

	public void qtyper() throws Exception {
		waitforseconds(8);
		BP.propertySelect(3, "Qty Per", 2);
		waitforseconds(5);
		CheckXpath("BOM_QTY_EDIT").sendKeys("12");
		log.info("~>> Qty Per updated");
		// BP.resetPricing();
		waitforseconds(5);
		WebElement Restart = CheckXpath("BOM_PROP_RESTART_WORKFLOW");
		safeJavaScriptClick(Restart);
		log.info("~>> Restart workflow selected");
		// Thread.sleep(5000);
		WebElement BOMpropsave = CheckXpath("BOM_PROP_SAVE");
		safeJavaScriptClick(BOMpropsave);
		log.info("~>> Save button clicked");
		waitforseconds(18);
		WebElement BOMsubmit = CheckXpath("BOM_SUBMIT");
		safeJavaScriptClick(BOMsubmit);
		log.info("~>> BOM Submited");
	}

	public int qtyperTotal() throws InterruptedException {
		waitforseconds(8);
		String id = CheckXpath("BOM_QTYPER_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.substring(id.indexOf("-"), id.length());
		List<WebElement> status = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		for (WebElement e : status) {
			String ele = e.getText();
			sum += Integer.parseInt(ele);
		}
		return sum;
	}

	public int Labordriver() throws Exception {
		waitforseconds(10);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			wait.until(ExpectedConditions
					.invisibilityOf(driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']"))));
		} else {
			log.info("~>> Redirect to Labor Tab");
		}
		safeJavaScriptClick(CheckXpath("LABOR_TAB"));
		log.info("~>> Redirected to Labor Tab ");
		waitforseconds(10);
		log.info("~>> Calculating driver count");
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		waitforseconds(8);
		String driverCount = driver.findElement(By.xpath(".//*[@id='Row0_DriverCount']")).getAttribute("value");
		log.info("~>> Updated driver count is " + driverCount);
		drcount = Integer.parseInt(driverCount);
		return drcount;
	}

	public void cancel() throws Exception {
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			log.info("----------------------------------------------------------------------------------");
			safeJavaScriptClick(CheckXpath("BOM_TAB"));
			log.info("CASE 1 : Changing partclass and click on cancel button");
			waitforseconds(3);
			partclassupdate(0);
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("LABOR_PROP_CANCEL"));
			log.info("~>> Cancel button clicked");
			waitforseconds(3);
			if (isAlertPresent()) {
				driver.switchTo().alert().accept();
			}

			String newpartClass = driver.findElement(By.xpath(".//*[@id='dvPartClass_0']")).getText();
			log.info("~>> New partclass :: " + newpartClass);
			if (oldpartClass.equals(newpartClass)) {
				log.info("RESULT 1 ::  Partclass not changed  ");
				Assert.assertTrue(true);
			} else {
				log.info("RESULT 1 ::  Partclass  changed");
				Assert.assertTrue(false);
			}
		}
	}

	public void uncheckeditbtn() throws Exception {
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			log.info(
					"CASE 2 :  Changing partclass ,uncheck Labor calcualtion update and  Check for submit button disable");
			waitforseconds(8);
			safeJavaScriptClick(CheckXpath("BOM_TAB"));
			log.info("----------------------------------------------------------------------------------");
			waitforseconds(3);
			partclassupdate(0);
			waitforseconds(10);
			safeJavaScriptClick(CheckXpath("MULTIEDIT_LABORUPDATE"));
			log.info("~>> Uncheck labor calculation update");
			waitforseconds(8);
			driver.findElement(By.xpath(".//*[@id='dvPartClass_0']")).getText();
			waitforseconds(8);
			if (CheckXpath("LABOR_PROP_SAVE").getAttribute("aria-disabled").equals("true")) {
				log.info("RESULT 2 ::  Save button is disabled - PASS");
				softAssertion.assertTrue(true);
				// Assert.assertTrue(true);
			} else {
				log.info("RESULT 2 ::  Save button is enabled - FAIL");
				softAssertion.assertTrue(false);
				// Assert.assertTrue(false);
			}
			waitforseconds(8);
			safeJavaScriptClick(CheckXpath("LABOR_PROP_CANCEL"));
			log.info("~>> Cancel button clicked");
			if (isAlertPresent()) {
				driver.switchTo().alert().accept();
			}
			waitforseconds(10);
			safeJavaScriptClick(CheckXpath("SELECT_LINE_1"));
			// safeJavaScriptClick(CheckXpath("BOM_SAVE"));
		}
	}

	public void uncheckinline() throws Exception {
		// safeJavaScriptClick(CheckXpath("BOM_TAB"));
		waitforseconds(8);
		log.info("CASE 3 : Changing partclass ,uncheck Labor calcualtion update and click on save button");

		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			partclassupdate(1);
			waitforseconds(8);
			boolean activity = false;
			try {
				activity = CheckXpath("LABOR_CALC_UPDATE").isDisplayed();
			} catch (Exception e) {

			}
			if (activity) {
				log.info("~>> Update Labor calculation popup displayed ");
				// alertacceptpopup();
				safeJavaScriptClick(CheckXpath("LABOR_CALC_UPDATE"));
				driver.findElement(By.xpath("//label")).click();
				log.info("~>> Uncheck labor calculation update");
				waitforseconds(5);
				safeJavaScriptClick(CheckXpath("LABOR_CALC_SUBMIT"));
				log.info("~>> Submit clicked");
				alertacceptpopup();
				String newpartClass = driver.findElement(By.xpath(".//*[@id='dvPartClass_1']")).getText();
				log.info("~>> Updated Part class  :: " + newpartClass);
				if (partClass.equals(newpartClass)) {
					log.info("RESULT :: Part class updated ");
					Assert.assertTrue(true);
				} else {
					log.info("RESULT :: Part class not updated ");
					Assert.assertTrue(false);
				}
			} else {
				log.info("~>> Update Labor calculation popup not displayed ");
			}
		}
	}

	public void submitchange() throws Exception {
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		log.info("~>> BOM Submitted");
		waitforseconds(5);
		log.info("CASE 4 : Changing partclass ,check Labor calcualtion update and  Submit");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("LABOR_TAB"));
		log.info("~>> Labor Tab Clicked");
		waitforseconds(8);
		Labor_Verifydrivercount VTc02 = new Labor_Verifydrivercount();
		double oldDr = VTc02.driverCount("Old", 0, 1);
		// log.info(oldDr);
		safeJavaScriptClick(CheckXpath("BOM_TAB"));
		log.info("~>> BOM tab clicked");
		waitforseconds(8);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			waitforseconds(5);
			BP.propertySelect(8, "Part Class", 2);
			waitforseconds(3);
			new Select(CheckXpath("BOM_PROP_PARTCLASS")).selectByIndex(1);
			waitforseconds(5);
			CheckXpath("BOM_PROP_SAVE").click();
			log.info("~>> Save button clicked");
			waitforseconds(8);
			safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
			waitforseconds(8);
			safeJavaScriptClick(CheckXpath("LABOR_TAB"));
			log.info("~>> Labor Tab Clicked");

			double newDr = VTc02.driverCount("New", 0, 1);
			if (oldDr != newDr) {
				log.info("RESULT :: Driver count changed");
				Assert.assertTrue(true);
			} else {
				log.info("RESULT :: Driver count not changed");
				Assert.assertTrue(false);
			}
			if (newDr == 0) {
				log.info("RESULT :: Driver count calcualted correctly");
				Assert.assertTrue(true);
			} else {
				log.info("RESULT :: Driver count not calculated correctly");
				Assert.assertTrue(false);
			}
		}
	}

	public void partclassupdate(int type) throws Exception {
		if (type == 0) {
			waitforseconds(8);
			oldpartClass = driver.findElement(By.xpath(".//*[@id='dvPartClass_0']")).getText();
			log.info("~>> Old Partclass :: " + oldpartClass);
			BP.propertySelect(1, "Part Class", 1);
			waitforseconds(5);
			new Select(CheckXpath("BOM_PROP_PARTCLASS")).selectByIndex(1);
			waitforseconds(3);
			partClass = new Select(CheckXpath("BOM_PROP_PARTCLASS")).getFirstSelectedOption().getText().trim();
			log.info("~>> Selected Partclass :: " + partClass);
			waitforseconds(8);
		}
		if (type == 1) {
			waitforseconds(10);
			WebElement partclass = driver.findElement(By.id("dvPartClass_1"));
			Actions actions = new Actions(driver);
			actions.moveToElement(partclass).doubleClick().perform();
			partClass = driver.findElement(By.xpath(
					"(.//div[@class='ui-grid-filter-container ng-scope']//select//option[@class='ng-binding ng-scope'])[3]"))
					.getText();
			log.info("~>> Selected Partclass :: " + partClass);
			waitforseconds(10);
			// driver.findElement(By.id("ddlPartClass_1")).click();
			new Select(driver.findElement(By.xpath(".//*[@id='ddlPartClass_1']"))).selectByVisibleText(partClass);
			driver.findElement(By.xpath(".//*[@id='ddlPartClass_1']")).click();
			driver.findElement(By.xpath("//body")).click();
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("BOM_SAVE"));
			log.info("~>> Clicked on BOM Save");
			waitforseconds(5);
		}
	}

	public double LabordriverPanel() throws Exception {
		waitforseconds(10);
		Labor_AddActivities AddLb = new Labor_AddActivities();
		AddLb.add("Automation_Test_Panel(Do not Touch)*");
		boolean close = false;
		try {
			close = driver.findElement(By.xpath(".//button[@ng-click='cancel()']")).isDisplayed();
		} catch (Exception e) {

		}
		if (close) {
			driver.findElement(By.xpath(".//button[@ng-click='cancel()']")).click();
			waitforseconds(5);
		} else {
			safeJavaScriptClick(CheckXpath("LABOR_SUBMIT"));
			JavascriptExecutor je = (JavascriptExecutor) driver;
			je.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			String driverCount = driver.findElement(By.xpath(".//*[@id='Row0_DriverCount']")).getAttribute("value");
			// log.info(driverCount);
			drcountPanel = Double.parseDouble(driverCount);
		}
		return drcountPanel;
	}

	public void paneldigitvalidation() throws Exception {
		log.info(
				"------------------------------------------------------------------------------------------------------------------");
		waitforseconds(10);
		log.info("CASE 1 :  Warning validation adding more than  digits number in panel count");
		safeJavaScriptClick(CheckXpath("PRINTED_CIRCUIT"));
		waitforseconds(10);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(35));
		wait.until(ExpectedConditions.elementToBeClickable(CheckXpath("PANEL_COUNT")));
		CheckXpath("PANEL_COUNT").clear();
		CheckXpath("PANEL_COUNT").sendKeys("123");
		waitforseconds(3);
		if (CheckXpath("PANEL_EXCLAIM").isDisplayed()) {
			log.info("RESULT :: Warning displayed");
			Assert.assertTrue(true);
		} else {
			log.info("RESULT :: Warning not displayed");
			Assert.assertTrue(false);
		}
	}

	public void negativeValue() throws Exception {
		log.info(
				"------------------------------------------------------------------------------------------------------------------");
		log.info("CASE 2 :  Change panel count with negative values");
		CheckXpath("PANEL_COUNT").clear();
		CheckXpath("PANEL_COUNT").sendKeys("-12");
		log.info("~>> Panel count entered as '-12'");
		if (CheckXpath("PANEL_COUNT").getText().equals("12")) {
			log.info("RESULT :: Not allowing to enter negative sign");
		} else if (CheckXpath("PANEL_COUNT").getText().equals("-12")) {
			log.info("RESULT :: allowing to enter negative sign");
		}
	}

	public void alphabetValue() throws InterruptedException {
		log.info(
				"------------------------------------------------------------------------------------------------------------------");
		waitforseconds(4);
		log.info("CASE 3 : Change panel count with alphabet");
		CheckXpath("PANEL_COUNT").clear();
		CheckXpath("PANEL_COUNT").sendKeys("abc");
		log.info("~>> Panel count entered as abc");
		if (CheckXpath("PANEL_COUNT").getText().equals("")) {
			log.info("RESULT :: Not allowing to enter alphabet value");
		} else if (CheckXpath("PANEL_COUNT").getText().equals("abc")) {
			log.info("RESULT ::  allowing to enter alphabet value");
		}
	}

	public void SpecialChar() {
		log.info(
				"------------------------------------------------------------------------------------------------------------------");
		log.info("CASE 4 : Change panel count with special character. ");
		CheckXpath("PANEL_COUNT").clear();
		CheckXpath("PANEL_COUNT").sendKeys("@#$");
		log.info("~>> Panel count entered as @#$");
		// log.info("panel count" + CheckXpath("PANEL_COUNT").getText());
		if (CheckXpath("PANEL_COUNT").getText().equals("")) {
			log.info("RESULT :: Not allowing to enter special character");
		} else if (CheckXpath("PANEL_COUNT").getText().equals("@#$")) {
			log.info("RESULT :: allowing to enter special character");
		}
	}

	public void pcbpartclass() throws Exception {
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BOM_TAB"));
		log.info("~>> Clicked on BOM Tab");
		waitforseconds(8);
		BP.propertySelect(1, "Part Class", 1);
		waitforseconds(5);
		WebElement partclass = driver
				.findElement(By.xpath("(.//*[@id='SinglePartClassId']//option[contains(text(),'PCB')])[1]"));
		String Pcb = partclass.getAttribute("innerText").trim();
		log.info("~>> Selected PartClass :: " + Pcb);
		new Select(CheckXpath("BOM_PROP_PARTCLASS")).selectByVisibleText(Pcb);
		waitforseconds(10);
		WebElement save = driver.findElement(By.xpath(".//*[@id='editProperty']"));
		safeJavaScriptClick(save);
		// driver.findElement(By.xpath(".//*[@id='editProperty']")).click();
		log.info("~>> Save button clicked");
		log.info("~>> PartClass Changed");
		waitforseconds(8);
	}

	public void panelcount() throws Exception {
		log.info("CASE 5 : Updated panel count to verify driver count ");
		CheckXpath("PANEL_COUNT").clear();
		CheckXpath("PANEL_COUNT").sendKeys("2");
		log.info("~>> Panel count entered as 2");
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		log.info("~>> BOM Submitted");
		newDr = Labordriver();
		log.info("~>> New driver count is  :: " + newDr);
		if (oldDr != newDr) {
			log.info("~>> Driver count changed ");
		} else {
			log.info("~>> Driver count not changed ");
		}
		if (newDr == 0.5) {
			log.info("~>> Driver count calcualtion is correct");
		} else {
			log.info("~>> Driver count calcualtion is wrong");
		}
	}

	public int LabordriverLead() throws Exception {
		Labor_AddActivities AddLb = new Labor_AddActivities();
		AddLb.add("Automation_Test_Leads(Do not Touch)*");
		boolean close = false;
		try {
			close = driver.findElement(By.xpath(".//button[@ng-click='cancel()']")).isDisplayed();
		} catch (Exception e) {

		}
		if (close) {
			driver.findElement(By.xpath(".//button[@ng-click='cancel()']")).click();
			Thread.sleep(5000);
		} else {
			JavascriptExecutor je = (JavascriptExecutor) driver;
			je.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			alertacceptpopup();
			String driverCount = driver.findElement(By.xpath(
					"(.//*[@id='gridLabourActivity']/tbody/tr/td[contains(text(),'MOH Labor(Do not Touch)')]//following::td//ng-form[@name='DriverCount']//input)[1]"))
					.getAttribute("value");
			drcount = Integer.parseInt(driverCount);
		}
		return drcount;
	}

	public void updatesuccess() throws Exception {
		waitforseconds(10);
		log.info("3. UPDATE NO OF LEADS  > SAVE >  VERIFY DRIVER COUNT CHANGED OR NOT AND CALCUALTION");
		WebElement BOMtab = CheckXpath("BOM_TAB");
		safeJavaScriptClick(BOMtab);
		log.info("~>> Redirected to BOM Tab");
		waitforseconds(8);
		leads();
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		log.info("~>> BOM Submitted");
		waitforseconds(5);
	}

	public void leads() throws Exception {
		BOM_Properties BP = new BOM_Properties();
		BP.propertySelect(7, "No. Of Leads", 2);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			CheckXpath("BOM_PROP_LEADS").clear();
			CheckXpath("BOM_PROP_LEADS").sendKeys("20");
			log.info("~>> No. Of Leads added");
			safeJavaScriptClick(CheckXpath("BOM_PROP_RESTART_WORKFLOW"));
			log.info("~>> Restart workflow selected");
			waitforseconds(4);
			CheckXpath("BOM_PROP_SAVE").click();
			log.info("~>> Save button clicked");
			waitforseconds(5);
		}
	}

	public void updatedecline() throws Exception {
		BP.NoLeads(2);
	}

	public void calculation() throws Exception {
		waitforseconds(8);
		log.info("** Calculating driver count for Lead");
		waitforseconds(5);
		String qtyid = CheckXpath("BOM_QTYPER_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String qtyid1 = qtyid.substring(qtyid.indexOf("-"), qtyid.length());
		String leadid = CheckXpath("BOM_LEADS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String leadid1 = leadid.substring(leadid.indexOf("-"), leadid.length());

		List<WebElement> listqty = driver.findElements(By.xpath(".//div[contains(@id,'" + qtyid1 + "')]//div"));
		List<WebElement> listlead = driver.findElements(By.xpath(".//div[contains(@id,'" + leadid1 + "')]//div"));

		double qty = 0;
		double[] qtys = new double[listqty.size()];

		for (int i = 0; i < listqty.size(); i++) {
			String qtyele = listqty.get(i).getText();
			qty = Double.parseDouble(qtyele);
			qtys[i] = qty;
			// log.info(qty);
		}
		double lead = 0;
		double[] leads = new double[listlead.size()];
		for (int i = 0; i < listlead.size(); i++) {
			String leadele = listlead.get(i).getText();
			lead = Double.parseDouble(leadele);
			// log.info(lead);
			leads[i] = lead;
		}
		// System.out.println(qtys.length + " :::::: " + leads.length);
		for (int i = 0; i < leads.length; i++) {
			// System.out.println("" + i + " " + qtys[i] + "," + leads[i]);
			sumDriver(qtys[i], leads[i]);
		}
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BOM_SUBMIT"));
		log.info("~>> BOM Submitted");
	}

	private void sumDriver(double qty, double lead) {
		// System.out.println("B : " + total);
		total = total + (qty * lead);
		// System.out.println("A : " + total);
		log.info("~>> Driver count calculation after updating no of leads is :: " + total);
	}

	public void verifydriverLead() throws Exception {
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("LABOR_TAB"));
		log.info("~>> Redirected to Labor Tab ");
		waitforseconds(8);
		newDr = LabordriverLead();
		log.info("~>> New Driver count is :: " + newDr);
		if (total == newDr) {
			log.info("~>> Driver count calculation is correct");
			softAssertion.assertTrue(true);
		} else {
			log.info("~>> Driver count  calculation is different");
			softAssertion.assertTrue(false);
		}
	}

	public void negativeupdate() throws Exception {
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			log.info("**1.  UPDATE NEGATIVE VALUE IN NO OF LEADS");
			safeJavaScriptClick(CheckXpath("BOM_TAB"));
			log.info("~>> Clicked on BOM Tab");
			waitforseconds(5);
			BP.propertySelect(7, "No. Of Leads", 1);
			CheckXpath("BOM_PROP_LEADS").clear();
			CheckXpath("BOM_PROP_LEADS").sendKeys("-120");
			log.info("~>> No. Of Leads added :: -120");
			waitforseconds(8);
			CheckXpath("BOM_PROP_SAVE").click();
			log.info("~>> Save button clicked");
			BP.actionPopup();
			waitforseconds(5);
			BP.fieldstatus("No. Of Leads");
			BP.fieldCheck("No. Of Leads", "", "BOM_LEADS_LABEL", "120", "-0-");
		}
	}

	public void alphanumeraicupdate() throws Exception {
		waitforseconds(4);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			log.info("** 2. UPDATE ALPHA-NUMERIC  VALUE IN NO OF LEADS");
			BP.propertySelect(7, "No. Of Leads", 1);
			CheckXpath("BOM_PROP_LEADS").clear();
			CheckXpath("BOM_PROP_LEADS").sendKeys("@!@#$%");
			log.info("~>> No. Of Leads added :: @!@#$%");
			waitforseconds(5);
			CheckXpath("BOM_PROP_SAVE").click();
			log.info("~>> Save button clicked");
			waitforseconds(8);
			if (driver.findElement(By.xpath(".//*[@id='validationsummary']")).isDisplayed()) {
				log.info("~>> " + driver.findElement(By.xpath(".//*[@id='validationsummary']//p")).getText()
						+ "Displayed");
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
			waitforseconds(5);
			CheckXpath("BOM_PROP_LEADS").clear();
			CheckXpath("BOM_PROP_LEADS").sendKeys("@!@#$%150");
			log.info("~>> No. Of Leads added :: @!@#$%12 ");
			waitforseconds(5);
			CheckXpath("BOM_PROP_SAVE").click();
			log.info("~>> Save button clicked");
			BP.actionPopup();
			waitforseconds(5);
			BP.fieldCheck("No. Of Leads", "", "BOM_LEADS_LABEL", "150", "-0-");
		}
	}

	public int LabordriverPartType() throws Exception {
		Labor_AddActivities AddLb = new Labor_AddActivities();
		AddLb.add("Automation_PartType_Lines(Do not Touch)*");
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		alertacceptpopup();
		String driverCount = driver.findElement(By.xpath(
				"(.//*[@id='gridLabourActivity']/tbody/tr/td[contains(text(),'Indirect Labor(Do not Touch)')]//following::td//ng-form[@name='DriverCount']//input)[1]"))
				.getAttribute("value");
		drcount = Integer.parseInt(driverCount);
		return drcount;
	}

}
