package BOM;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class BOM_Properties extends BaseInitBOM {

	SoftAssert softAssertion = new SoftAssert();
	private String new_value, value, old_value;

	public void propertySelect(int no, String cat, int type) throws Exception {
		waitforseconds(5);
		log.info("----------------------------------------------------------------------------");
		if (type == 1) {
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("SELECT_LINE_1"));
			log.info("~>> Single BOM line selected");
		}
		if (type == 2) {

			waitforseconds(10);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(35));
			wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath(" .//div[@class='ui-grid-cell-contents']//div[@ng-if='grid.options.enableSelectAll']")));
			WebElement ele = CheckXpath("SELECT_ALL_BOM");
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click()", ele);
			log.info("~>> Multi  line BOM selected");
		}
		waitforseconds(10);
		boolean editbtn = false;
		try {
			editbtn = CheckXpath("EDIT_LINE_BUTTON").isDisplayed();
		} catch (Exception e) {

		}
		if (editbtn) {
			Assert.assertTrue(true);
			log.info("~>> RESULT  :: Edit button visible");
			if (CheckXpath("EDIT_LINE_BUTTON").getAttribute("aria-disabled").equals("false")) {
				waitforseconds(5);
				WebElement EditLineBtn = CheckXpath("EDIT_LINE_BUTTON");
				safeJavaScriptClick(EditLineBtn);
				log.info("~>> Edit button clicked");
				waitforseconds(3);
				log.info("~>> ** " + no + " EDIT PROPERTY :: " + cat);
				new Select(CheckXpath("BOM_PROP")).selectByVisibleText(cat);
				log.info("~>> Property selected :: " + cat);
			} else {
				log.info("~>> Edit button is disabled");
			}
		} else {
			Assert.assertTrue(false);
			log.info("~>> RESULT  ::  Edit button not visible");
		}
	}

	public void valueCheck(String valueChk) throws Exception {
		waitforseconds(5);
		old_value = CheckXpath(valueChk).getAttribute("aria-checked");
		log.info("~>> old value is :: " + old_value);
		safeJavaScriptClick(CheckXpath(valueChk));
		new_value = CheckXpath(valueChk).getAttribute("aria-checked");
		log.info("~>> Value changed from :: " + old_value + " to " + new_value);
		waitforseconds(8);
		CheckXpath("BOM_PROP_RESTART_WORKFLOW").click();
		log.info("~>> Restart workflow selected");
		// resetPricing();
		waitforseconds(5);
		CheckXpath("BOM_PROP_SAVE").click();
		log.info("~>> Save button clicked");
		waitforseconds(5);
	}

	public void resetPricing() {
		boolean pricing = false;
		try {
			pricing = CheckXpath("BOM_RESET_PRICING").isDisplayed();
		} catch (Exception e) {

		}
		if (pricing) {
			log.info("~>> Reset Pricing for line items is :: "
					+ CheckXpath("BOM_RESET_PRICING").getAttribute("aria-checked"));
		}
	}

	public void validationChkbox(String type, String Property) {
		boolean value_ele = false;
		try {
			value_ele = CheckXpath(Property).isDisplayed();
		} catch (Exception e) {

		}
		if (value_ele) {
			log.info("~>> Verifying Property  changed value");
			value = CheckXpath(Property).getAttribute("aria-checked");
			if (value.equals(new_value)) {
				// Assert.assertTrue(true);
				softAssertion.assertTrue(true);
				log.info("~>> RESULT  :: Changed value is same for  property  - " + type);
			} else {
				// Assert.assertTrue(false);
				softAssertion.assertTrue(false);
				log.info("~>> RESULT :: Changed value is different for  property -  " + type);
			}
		} else {
			log.info("~>> element not found");
		}
	}

	public void install(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING INSTALL CHECKBOX");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {

			if (type == 0) {
				propertySelect(1, "Install", 1);
				valueCheck("BOM_PROP_VALUE_INSTALL");
				// fieldstatus("Install");
				validationChkbox("Install", "BOM_INSTALL_" + type);
			}
			if (type == 1) {
				value = CheckXpath("BOM_INSTALL_1").getAttribute("aria-checked");
				safeJavaScriptClick(CheckXpath("BOM_INSTALL_1"));
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				waitforseconds(3);
				actionPopup();
				waitforseconds(3);
				new_value = CheckXpath("BOM_INSTALL_1").getAttribute("aria-checked");
				if (value != new_value) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: value changed successfully  for Install from " + value + " to "
							+ new_value);
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: value not changed successfully for Install");
				}
				waitforseconds(5);
			} else if (type == 2) {
				// fieldstatus("Install");
				propertySelect(1, "Install", 2);
				valueCheck("BOM_PROP_VALUE_INSTALL");
				multilineValidation("Install", "BOM_INSTALL_LABEL", "", 0);
			}
		}
	}

	public void actionPopup() throws Exception {
		boolean actions = false;
		try {
			actions = CheckXpath("ACTION_SUBMIT").isDisplayed();
		} catch (Exception e) {

		}
		if (actions) {
			safeJavaScriptClick(CheckXpath("ACTION_SUBMIT"));
			log.info("~>> Labor Calulations updated");
		} else {
			// log.info("~>> No Labor Calulations update");
		}
	}

	public void purchase(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING PURCHASE CHECKBOX");
		waitforseconds(8);
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(2, "Purchase", 1);
				waitforseconds(3);
				valueCheck("BOM_PROP_VALUE_PURCHASE");
				// fieldstatus("Purchase");
				validationChkbox("Purchase", "BOM_PURCHASE_" + type);
			}
			if (type == 1) {
				waitforseconds(3);
				value = CheckXpath("BOM_PURCHASE_1").getAttribute("aria-checked");
				safeJavaScriptClick(CheckXpath("BOM_PURCHASE_1"));
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				waitforseconds(3);
				actionPopup();
				waitforseconds(3);
				new_value = CheckXpath("BOM_PURCHASE_1").getAttribute("aria-checked");
				if (value != new_value) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: value changed successfully  for Purchase from " + value + " to "
							+ new_value);
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: value not changed successfully for Purchase");
				}
			} else if (type == 2) {
				propertySelect(2, "Purchase", 2);
				valueCheck("BOM_PROP_VALUE_PURCHASE");
				multilineValidation("Purchase", "BOM_PURCHASE_LABEL", "", 0);
			}
		}
	}

	public void qtyPer(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING  QTY PER");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(3, "Qty Per", 1);
				CheckXpath("BOM_QTY_EDIT").sendKeys("15");
				log.info("~>> Qty Per edited");
				resetPricing();
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				waitforseconds(5);
				// fieldstatus("Qty Per*");
				if (CheckXpath("QTY_PER_" + type).getText().equals("15")) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  QtyPer");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: Changed value is different for  property -  QtyPer");
				}
			} else if (type == 1) {
				// fieldstatus("Qty Per*");
				log.info("~>> Inline selection");
				// inlineInput("BOM_QTYPER_LABEL", "15", "-1-");
				qtyper();
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				if (CheckXpath("QTY_PER_" + type).getText().equals("15")) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  QtyPer");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: Changed value is different for  property -  QtyPer");
				}
			} else if (type == 2) {
				propertySelect(3, "Qty Per", 2);
				CheckXpath("BOM_QTY_EDIT").sendKeys("25");
				log.info("~>> Qty Per edited");
				resetPricing();
				// waitforseconds(3);
				// safeJavaScriptClick(CheckXpath("BOM_PROP_RESTART_WORKFLOW"));
				// log.info("~>> Restart workflow selected");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				multilineValidation("Qty Per", "BOM_QTYPER_LABEL", "25", 1);
			}
		}
	}

	public void description(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING DESCRIPTION");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(4, "Description", 1);
				CheckXpath("BOM_PROP_DESC").clear();
				CheckXpath("BOM_PROP_DESC").sendKeys("Auto Test Description");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				waitforseconds(8);
				// fieldstatus("Description");
				if (CheckXpath("BOM_DESC_" + type).getText().contains("Auto Test Description")) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  Description");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT  :: Changed value is  not same for  property  -  Description");
				}
			} else if (type == 1) {
				// fieldstatus("Description");
				inlineInput("BOM_DESC_LABEL", "Auto Test Description", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				log.info("~>> Save button clicked");
				actionPopup();
				waitforseconds(10);
				if (CheckXpath("BOM_DESC_" + type).getText().contains("Auto Test Description")) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  Description");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT  :: Changed value is  not same for  property  -  Description");
				}

			} else if (type == 2) {
				propertySelect(4, "Description", 2);
				CheckXpath("BOM_PROP_DESC").clear();
				CheckXpath("BOM_PROP_DESC").sendKeys("Multi-line Auto Test Description");
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				waitforseconds(8);
				// fieldstatus("Description");
				multilineValidation("Description", "BOM_DESC_LABEL", "Multi-line Auto Test Description", 1);
			}
		}
	}

	public void designator(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING DESIGNATOR");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(5, "Designator", 1);
				CheckXpath("BOM_PROP_DESIG").clear();
				CheckXpath("BOM_PROP_DESIG").sendKeys("Auto Test Designator");
				log.info("~>> Designator added");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Designator");
				waitforseconds(5);
				fieldCheck("Designator", "", "BOM_DESIG_LABEL", "Auto Test Designator", "-0-");
				waitforseconds(5);
			} else if (type == 1) {
				// fieldstatus("Designator");
				waitforseconds(5);
				inlineInput("BOM_DESIG_LABEL", "Auto Test Designator", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				fieldCheck("Designator", "", "BOM_DESIG_LABEL", "Auto Test Designator", "-1-");
			} else if (type == 2) {
				propertySelect(5, "Designator", 2);
				CheckXpath("BOM_PROP_DESIG").clear();
				CheckXpath("BOM_PROP_DESIG").sendKeys("Multi-line Auto Test Designator");
				log.info("~>> Designator added");
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Designator");
				waitforseconds(5);
				multilineValidation("Designator", "BOM_DESIG_LABEL", "Multi-line Auto Test Designator", 1);
				waitforseconds(5);
			}
		}
	}

	public void partNo(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING PART NO NOTES");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(6, "Part # Notes", 1);
				CheckXpath("BOM_PROP_PARTNO").sendKeys("Auto part# Notes");
				log.info("~>> Part# Notes added");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Part # Notes");
				fieldCheck("Part # Notes", "", "BOM_PARTNO_LABEL", "Auto part# Notes", "-0-");
			} else if (type == 1) {
				// fieldstatus("Part # Notes");
				inlineInput("BOM_PARTNO_LABEL", "Auto part# Notes", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				fieldCheck("Part # Notes", "", "BOM_PARTNO_LABEL", "Auto part# Notes", "-1-");

			}

			else if (type == 2) {
				propertySelect(6, "Part # Notes", 2);
				CheckXpath("BOM_PROP_PARTNO").sendKeys("Multi-line Auto part# Notes");
				log.info("~>> Part# Notes added");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Part # Notes");
				multilineValidation("Part # Notes", "BOM_PARTNO_LABEL", "Multi-line Auto part# Notes", 1);
			}
		}
	}

	public void NoLeads(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING NO OF LEADS");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(7, "No. Of Leads", 1);
				CheckXpath("BOM_PROP_LEADS").clear();
				CheckXpath("BOM_PROP_LEADS").sendKeys("10");
				log.info("~>> No. Of Leads added");
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("No. Of Leads");
				fieldCheck("No. Of Leads", "", "BOM_LEADS_LABEL", "10", "-0-");
			} else if (type == 1) {
				// fieldstatus("No. Of Leads");
				inlineInput("BOM_LEADS_LABEL", "20", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				fieldCheck("No. Of Leads", "", "BOM_LEADS_LABEL", "20", "-1-");
			} else if (type == 2) {
				propertySelect(7, "No. Of Leads", 2);
				CheckXpath("BOM_PROP_LEADS").clear();
				CheckXpath("BOM_PROP_LEADS").sendKeys("30");
				log.info("~>> No. Of Leads added");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("No. Of Leads");
				multilineValidation("No. Of Leads", "BOM_LEADS_LABEL", "30", 1);
			}
		}
	}

	public void partClass(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING PART CLASS");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(8, "Part Class", 1);
				waitforseconds(3);
				new Select(CheckXpath("BOM_PROP_PARTCLASS")).selectByIndex(1);
				waitforseconds(3);
				String partclass = new Select(CheckXpath("BOM_PROP_PARTCLASS")).getFirstSelectedOption().getText()
						.trim();
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				actionPopup();
				waitforseconds(8);
				// fieldstatus("Part Class");
				// log.info(driver.findElement(By.xpath(".//*[@id='dvPartClass_0']")).getAttribute("innerText"));
				if (driver.findElement(By.xpath(".//*[@id='dvPartClass_0']")).getAttribute("innerText")
						.contains(partclass)) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  PART CLASS");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: Changed value is different for  property -  PART CLASS");
				}
			} else if (type == 1) {
				// fieldstatus("Part Class*");
				WebElement partclass = driver.findElement(By.id("dvPartClass_1"));
				Actions actions = new Actions(driver);
				actions.moveToElement(partclass).doubleClick().perform();

				String partClass = driver.findElement(By.xpath(
						"(.//div[@class=\"ui-grid-filter-container ng-scope\"]//select//option[@class=\"ng-binding ng-scope\"])[3]"))
						.getText();

				waitforseconds(5);
				// driver.findElement(By.id("ddlPartClass_1")).click();
				new Select(driver.findElement(By.id("ddlPartClass_1"))).selectByVisibleText(partClass);
				driver.findElement(By.xpath(".//*[@id='ddlPartClass_1']")).click();
				driver.findElement(By.xpath("//body")).click();
				waitforseconds(3);
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				// fieldstatus("Part Class");
				log.info(driver.findElement(By.xpath(".//*[@id='dvPartClass_1']")).getAttribute("innerText"));
				if (driver.findElement(By.xpath(".//*[@id='dvPartClass_1']")).getAttribute("innerText")
						.contains(partClass)) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  PART CLASS");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: Changed value is different for  property -  PART CLASS");
				}
			} else if (type == 2) {
				propertySelect(8, "Part Class", 2);
				waitforseconds(3);
				new Select(CheckXpath("BOM_PROP_PARTCLASS")).selectByVisibleText("CAP-SMT 0402");
				waitforseconds(3);
				String partclass = new Select(CheckXpath("BOM_PROP_PARTCLASS")).getFirstSelectedOption().getText()
						.trim();
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				waitforseconds(8);
				// fieldstatus("Part Class*");
				multilineValidation("Part Class", "BOM_PARTCLASS_LABEL", partclass, 1);
			}
		}
	}

	public void partUOM(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING PART UOM");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(9, "Part UOM", 1);
				new Select(CheckXpath("BOM_PROP_UOM")).selectByVisibleText("EA");
				waitforseconds(3);
				String partUOM = new Select(CheckXpath("BOM_PROP_UOM")).getFirstSelectedOption().getText().trim();
				log.info("~>> Selected PartUOM :: " + partUOM);
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Part UOM");
				fieldCheck("Part UOM", "", "BOM_PARTUOM_LABEL", "EA", "-0-");
			} else if (type == 1) {
				// fieldstatus("Part UOM");
				inlineInput("BOM_PARTUOM_LABEL", "EA", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				// fieldstatus("Part UOM");
				fieldCheck("Part UOM", "", "BOM_PARTUOM_LABEL", "EA", "-1-");
			} else if (type == 2) {
				propertySelect(9, "Part UOM", 2);
				new Select(CheckXpath("BOM_PROP_UOM")).selectByVisibleText("EA");
				waitforseconds(3);
				String partUOM = new Select(CheckXpath("BOM_PROP_UOM")).getFirstSelectedOption().getText().trim();
				log.info(partUOM);
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Part UOM");
				multilineValidation("Part Class", "BOM_PARTUOM_LABEL", partUOM, 1);
			}
		}
	}

	public void leadQty(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING LEAD QTY");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(10, "Lead Qty", 1);
				CheckXpath("BOM_PROP_LEADQTY").clear();
				CheckXpath("BOM_PROP_LEADQTY").sendKeys("20");
				log.info("~>> No. Of Leads added");
				waitforseconds(5);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				actionPopup();
				waitforseconds(5);
				// fieldstatus("Lead Qty");
				fieldCheck("Lead Qty", "", "BOM_LEADQTY_LABEL", "20", "-0-");
			} else if (type == 1) {
				// fieldstatus("Lead Qty");
				inlineInput("BOM_LEADQTY_LABEL", "25", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				waitforseconds(8);
				// fieldstatus("Lead Qty");
				fieldCheck("Lead Qty", "", "BOM_LEADQTY_LABEL", "25", "-1-");
			} else if (type == 2) {
				propertySelect(10, "Lead Qty", 2);
				CheckXpath("BOM_PROP_LEADQTY").clear();
				CheckXpath("BOM_PROP_LEADQTY").sendKeys("30");
				log.info("~>> No. Of Leads added");
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Lead Qty");
				multilineValidation("Lead Qty", "BOM_LEADQTY_LABEL", "30", 1);
			}
		}
	}

	public void attrRate(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING ATTR RATE");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(11, "Attr Rate %", 1);
				CheckXpath("BOM_PROP_ATTRRATE").clear();
				CheckXpath("BOM_PROP_ATTRRATE").sendKeys("15");
				log.info("~>> Attr Rate % added");
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Attr Rate %");
				fieldCheck("Attr Rate %", "", "BOM_ATTRRATE_LABEL", "15", "-0-");
			} else if (type == 1) {
				inlineInput("BOM_ATTRRATE_LABEL", "30", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				fieldCheck("Attr Rate %", "", "BOM_ATTRRATE_LABEL", "30", "-1-");
			} else if (type == 2) {
				propertySelect(11, "Attr Rate %", 2);
				CheckXpath("BOM_PROP_ATTRRATE").clear();
				CheckXpath("BOM_PROP_ATTRRATE").sendKeys("25");
				log.info("~>> Attr Rate % added");
				waitforseconds(4);
				// CheckXpath("BOM_PROP_SAVE").click();
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Attr Rate %");
				multilineValidation("Attr Rate %", "BOM_ATTRRATE_LABEL", "25", 1);
			}
		}
	}

	public void MinPurchase(int type) throws Exception {
		log.info("---------------------------------------------------------------------------");
		log.info("** EDITING MINIMUM PURCHASE");
		boolean spinner = false;
		try {
			spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")).isDisplayed();
		} catch (Exception e) {

		}
		if (spinner) {
			log.info("~>> Previous operation is still in progress");
		} else {
			if (type == 0) {
				propertySelect(12, "Min Purchase Qty", 1);
				CheckXpath("BOM_PROP_MINPS").clear();
				CheckXpath("BOM_PROP_MINPS").sendKeys("30");
				log.info("~>> Min Purchase Qty added");
				waitforseconds(5);
				WebElement ele = CheckXpath("BOM_PROP_SAVE");
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].click()", ele);
				// CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Min Purchase Qty");
				fieldCheck("Min Purchase Qty", "Min Purchase", "BOM_MINPS_LABEL", "30.000000", "-0-");
			} else if (type == 1) {
				// fieldstatus("Min Purchase Qty");
				inlineInput("BOM_MINPS_LABEL", "40", "-1-");
				safeJavaScriptClick(CheckXpath("BOM_SAVE"));
				actionPopup();
				waitforseconds(8);
				fieldCheck("Minimum Purchase", "Min Purchase", "BOM_MINPS_LABEL", "40.000000", "-1-");
			} else if (type == 2) {
				propertySelect(12, "Min Purchase Qty", 2);
				CheckXpath("BOM_PROP_MINPS").clear();
				CheckXpath("BOM_PROP_MINPS").sendKeys("50");
				log.info("~>> Min Purchase Qty added");
				waitforseconds(4);
				CheckXpath("BOM_PROP_SAVE").click();
				log.info("~>> Save button clicked");
				// fieldstatus("Min Purchase Qty");
				multilineValidation("Min Purchase Qty", "BOM_MINPS_LABEL", "50.000000", 1);
			}
		}
	}

	public void fieldstatus(String filter) throws Exception {
		waitforseconds(10);
		WebElement bomfilter = CheckXpath("BOM_FILTER");
		Actions actions = new Actions(driver);
		actions.moveToElement(bomfilter).click().perform();
		waitforseconds(8);
		boolean filtermenu = false;
		try {
			filtermenu = driver.findElement(By.xpath(".//div[@ng-show='shownMid']")).isDisplayed();
		} catch (Exception e) {

		}
		if (filtermenu) {
			WebElement status = driver.findElement(By
					.xpath(".//button[@class='ui-grid-menu-item ng-binding'and contains(text(),'" + filter + "')]//i"));
			String status_class = status.getAttribute("class");
			if (status_class.contains("ui-grid-icon-ok")) {
				log.info("~>> field is already present :: " + filter);
			} else {
				log.info("~>> field is not present :: " + filter);
				waitforseconds(3);
				safeJavaScriptClick(status);
				// status.click();
			}
			waitforseconds(4);
			// bomfilter.click();
			safeJavaScriptClick(bomfilter);
			// driver.findElement(By.xpath(".//*[@id='content']")).click();
			waitforseconds(4);
		} else {
			log.info("~>> Filter not opened");
			safeJavaScriptClick(bomfilter);
		}
	}

	public void fieldCheck(String fieldname1, String fieldname2, String fieldid, String value, String idno)
			throws InterruptedException {
		waitforseconds(5);
		WebElement header = driver.findElement(
				By.xpath(".//span[contains(text(),'" + fieldname1 + "') or contains(text(),'" + fieldname2 + "')]"));
		// ((JavascriptExecutor) driver).executeScript("arguments[0].scrollRight =
		// arguments[0].offsetWidth", header);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", header);
		waitforseconds(5);
		boolean idpath = false;
		try {
			idpath = CheckXpath(fieldid).isDisplayed();
		} catch (Exception e) {

		}
		waitforseconds(8);
		if (idpath) {
			String id = CheckXpath(fieldid).getAttribute("id").replaceAll("-header-text", "");
			String id1 = id.split("-")[0].concat(idno).concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
			waitforseconds(8);
			if (driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div")).getText().contains(value)) {
				Assert.assertTrue(true);
				log.info("~>> RESULT  :: Changed value : " + value + "  is same for  property  -  " + fieldname1);
			} else {
				Assert.assertTrue(false);
				log.info("~>> RESULT  :: Changed value is  not same for  property  - " + fieldname1);
			}
		}
	}

	public void inlineInput(String headerId, String input, String idno) throws InterruptedException {
		String id = CheckXpath(headerId).getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.split("-")[0].concat(idno).concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		waitforseconds(3);
		WebElement desc = driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
		waitforseconds(3);
		/*
		 * JavascriptExecutor jse = (JavascriptExecutor) driver;
		 * jse.executeScript("arguments[0].scrollIntoView(true);", desc);
		 */
		waitforseconds(3);
		Actions actions = new Actions(driver);
		actions.moveToElement(desc).doubleClick().perform();
		waitforseconds(3);
		actions.sendKeys(input).perform();
		log.info("~>> " + input + " added"); //
		// actions.click();
		driver.findElement(By.xpath("//body")).click();
		waitforseconds(5);
	}

	public void qtyper() throws InterruptedException {
		WebElement qty = driver.findElement(By.id("dvQty_0"));
		Actions actions = new Actions(driver);
		actions.moveToElement(qty).doubleClick().perform();
		waitforseconds(2);
		driver.findElement(By.id("txtQty_0")).sendKeys("15");
		waitforseconds(2);
		driver.findElement(By.id("txtQty_0")).sendKeys(Keys.ENTER);
		// driver.findElement(By.id("content")).click();
		waitforseconds(3);
	}

	public void multilineValidation(String fieldname, String fieldid, String value, int type)
			throws InterruptedException {
		waitforseconds(8);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollRight = arguments[0].offsetWidth", fieldid);
		waitforseconds(3);
		String id = CheckXpath(fieldid).getAttribute("id").replaceAll("-header-text", "");
		String id1 = id.substring(id.indexOf("-"), id.length());
		// log.info(id);
		// log.info(id1);
		waitforseconds(5);
		if (type == 0) {
			List<WebElement> status = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]/div/input"));
			for (WebElement e : status) {
				if (e.getAttribute("aria-checked") != new_value) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  - " + fieldname);
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT  :: Changed value is different for  property  - " + fieldname);
				}
			}
		} else if (type == 1) {
			List<WebElement> status = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
			for (WebElement e : status) {
				System.out.println(e.getText());
				if (driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div")).getText()
						.contains(value)) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value - " + value + "  is same for  property  -  " + fieldname);
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT  :: Changed value is  not same for  property  - " + fieldname);
				}
			}
		} else if (type == 2) {
			List<WebElement> status = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div"));
			for (WebElement e : status) {
				System.out.println(e.getText());
				if (driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//div")).getAttribute("innerText")
						.contains(value)) {
					Assert.assertTrue(true);
					log.info("~>> RESULT  :: Changed value is same for  property  -  PART CLASS");
				} else {
					Assert.assertTrue(false);
					log.info("~>> RESULT :: Changed value is different for  property -  PART CLASS");
				}
			}
		}
	}

	public static void gridmenu() throws Exception {
		log.info("~>> Checking Grid Menu");
		System.out.println("~>> Checking Grid Menu");
		waitforseconds(20);

		WebElement bomfilter = CheckXpath("BOM_FILTER");
		Actions actions = new Actions(driver);
		actions.moveToElement(bomfilter).click().perform();

		// safeJavaScriptClick(CheckXpath("BOM_FILTER"));
		// CheckXpath("BOM_FILTER").click();
		log.info("~>> Grid Menu filter clicked");
		// waitforseconds(8);
		boolean filtermenu = false;
		try {
			filtermenu = driver.findElement(By.xpath(".//div[@ng-show='shownMid']")).isDisplayed();
		} catch (Exception e) {

		}
		if (filtermenu) {
			List<WebElement> lstmenu = driver.findElements(By.xpath(
					".//ul[@class='ui-grid-menu-items']//li//button[@class='ui-grid-menu-item ng-binding']//i[contains(@class,'ui-grid')]"));
			log.info("~>> Total fields :: " + lstmenu.size());
			waitforseconds(3);
			for (int i = 0; i < lstmenu.size(); i++) {
				if (lstmenu.get(i).getAttribute("class").equals("ui-grid-icon-cancel")) {
					log.info(">> " + i + lstmenu.get(i).getText() + " :: Disabled");
					safeJavaScriptClick(lstmenu.get(i));
					log.info(">> " + i + lstmenu.get(i).getText() + " ::  Enabled now");
				} else {
					log.info(">> " + i + lstmenu.get(i).getText() + " :: Already Enabled");
				}
				waitforseconds(5);
			}
		} else {
			log.info("~>> Filter is not available");
			System.out.println("~>> Filter is not available");
		}
	}
}
