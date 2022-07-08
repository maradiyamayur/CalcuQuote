package RFQ;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class RFQ_Validations extends BaseInitRFQ {

	protected String Quoteid;
	SoftAssert softAssertion = new SoftAssert();

	public void features() throws InterruptedException {

	}

	// blank due date validation
	public void defaultBlankDueDate() throws InterruptedException {
		// waitforseconds(3);
		String dueDate = CheckXpath("QI_DUEDATE").getAttribute("value");
		if (dueDate.equals("")) {
			CheckXpath("QI_DUEDATE_CAL").click();
			waitforseconds(3);
			CheckXpath("QI_DUEDATE_SELECT").click();
			log.info("~>> Quote duedate is selected");
		} else {
			log.info("~>> Quote duedate is already selected");
		}
		// waitforseconds(3);
	}

	// Customer validation after deal selection
	public void customer_validation() throws InterruptedException {
		waitforseconds(3);
		if (driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).getText().contains("Company/Contacts details sync successfully for the selected Deal.")) {
			Assert.assertTrue(true);
			log.info("RESULT-3  :: Toast Alert message displayed on deal selection");
			log.info(driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']"))
					.getText());
			log.info("Customer Name selected with deal :: " + CheckXpath("CUST_NAME").getAttribute("value"));
			driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).click();
		} else if (driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).getText()
				.contains("No Company/Contacts details found for the selected Deal.")) {
			// Assert.assertTrue(true);
			softAssertion.assertTrue(true);
			log.info("RESULT-3  :: Toast Alert message displayed on deal selection");
			log.info(driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']"))
					.getText());
			driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).click();
		} else {
			// Assert.assertTrue(false);
			softAssertion.assertTrue(false);
			log.info("~>>1.  Toast Alert was not displayed on deal selection");
		}
		waitforseconds(5);
	}

	// mandatory validations for other fields
	public void mandatory_Validation() throws Exception {
		waitforseconds(10);
		log.info("~>>1.  Check mandatory validation for Add Assembly");
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		waitforseconds(3);
		safeJavaScriptClick(CheckXpath("RFQ_SUBMIT"));
		log.info("~>> Clicked on submit button");
		// waitforseconds(5);
		if (CheckXpath("TOAST_ALERT").getText().contains("Please enter at least ONE assembly per RFQ")) {
			// Assert.assertTrue(true);
			softAssertion.assertTrue(true);
			log.info("RESULT-1 [VALIDATION] :: " + CheckXpath("TOAST_ALERT").getText());
		} else {
			// Assert.assertTrue(false);
			softAssertion.assertTrue(false);
			log.info("RESULT-1 [VALIDATION] ::  Alert was not displayed");
		}
		log.info("----------------------------------------------------------------------------");
		log.info("~>>2.  Checking mandatory validation for other fields");
		waitforseconds(10);
		WebElement myelement = CheckXpath("ADD_ASSEMBLY");
		JavascriptExecutor jse2 = (JavascriptExecutor) driver;
		jse2.executeScript("arguments[0].scrollIntoView()", myelement);
		myelement.click();
		waitforseconds(8);
		validation_msg("2");
	}

	// Assembly- Invalid input validations
	public void asm_Invalid_Validation(int i, int no) throws InterruptedException {
		log.info("~>> Check Validations for Assembly" + no);
		log.info(
				"~>> 3. Adding  invalid Quote ID, Assembly Name, Assemly Number, Assembly Id, Revision, Qty and Turntime");
		quote_visible_Validations();
		CheckXpath("ASSEMBLY_NAME_" + i).clear();
		CheckXpath("ASSEMBLY_NAME_" + i).sendKeys("Test@123456578<>");
		CheckXpath("ASSEMBLY_NO_" + i).clear();
		CheckXpath("ASSEMBLY_NO_" + i).sendKeys("Test@123456578<>");
		waitforseconds(3);
		asm_visible_Validations(0, "ASSEMBLY_ID_", "", i);
		CheckXpath("ASSEMBLY_REVISION_" + i).clear();
		CheckXpath("ASSEMBLY_REVISION_" + i).sendKeys("Test@123456578<>");
		assembly_Qtyturn(i, no, "12e", "def", "ARO");
		waitforseconds(1);
		validation_msg("4");

		log.info(
				"~>> 4. Adding invalid size for Assembly Name, Assemly Number,  Assembly Id, Revision, Qty and Turntime");
		CheckXpath("ASSEMBLY_NAME_" + i).clear();
		CheckXpath("ASSEMBLY_NAME_" + i).sendKeys("Test@1234565789123456789123456789123456789123456789");
		CheckXpath("ASSEMBLY_NO_" + i).clear();
		CheckXpath("ASSEMBLY_NO_" + i).sendKeys("Test@1234565789123456789123456789123456789123456789");
		waitforseconds(3);
		asm_visible_Validations(1, "ASSEMBLY_ID_", "Test@1234565789123456789123456789123456789123456789", i);
		CheckXpath("ASSEMBLY_REVISION_" + i).clear();
		CheckXpath("ASSEMBLY_REVISION_" + i).sendKeys("Test@12345657891234567891234567");
		assembly_Qtyturn(i, no, "123456789", "1234", "ARO");
		validation_msg("5");
	}

	// Assembly ID visibility check with input values
	public void quote_visible_Validations() {
		boolean element = false;
		try {
			element = CheckXpath("QUOTE_ID").isDisplayed();
		} catch (Exception e) {
		}
		if (element) {
			CheckXpath("QUOTE_ID").clear();
			CheckXpath("QUOTE_ID").sendKeys("Test@123456578<>");
		}
	}

	public void asm_visible_Validations(int type, String Path, String ID, int j) {
		boolean element = false;
		try {
			element = CheckXpath(Path + j).isDisplayed();
		} catch (Exception e) {

		}
		if (element) {
			if (type == 0) {
				CheckXpath(Path + j).clear();
				CheckXpath(Path + j).sendKeys("Test@123456578<>");
				// log.info("~>> " + name + "Added");
			} else if (type == 1) {
				CheckXpath(Path + j).clear();
				CheckXpath(Path + j).sendKeys(ID);
			}
		}
	}

	// Validation check common code
	public void validation_msg(String no) throws InterruptedException {
		CheckXpath("RFQ_SUBMIT").click();
		waitforseconds(5);
		if (CheckXpath("VALIDATION_SUMMARY").isDisplayed()) {
			Assert.assertTrue(true);
			log.info("RESULT- " + no + " ::  [VALIDATION] " + CheckXpath("VALIDATION_SUMMARY").getText());
		} else {
			Assert.assertTrue(false);
			log.info("RESULT-" + no + "  ::  Alert was not displayed");
		}
		log.info("----------------------------------------------------------------------------");
	}

	// Validations to check : package quote option should be disable, Currency
	// should not be editable
	public void edit_Validation() throws Exception {
		Quoteid = driver
				.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope' ]//a[@aria-hidden='false'])[1]"))
				.getText();
		log.info("~>> Quote ID is :: " + Quoteid);
		WebElement Quoteid = driver.findElement(
				By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope' ]//a[@aria-hidden='false'])[1]"));
		Actions new_actions = new Actions(driver);
		new_actions.moveToElement(Quoteid).click().perform();
		waitforseconds(2);
		log.info("~>> 8.  Check Package Quote option");
		boolean element = false;
		try {
			element = CheckXpath("PACKAGE_QUOTE_0").isDisplayed();
		} catch (Exception e) {

		}
		if (element) {

			if (CheckXpath("PACKAGE_QUOTE_0").isEnabled()) {
				log.info("RESULT-8:: Package Quote option is enabled at edit RFQ for assembly 1");
				Assert.assertTrue(false);
			} else {
				log.info("RESULT-8:: Package Quote option is disabled at edit RFQ for assembly 1");
				Assert.assertTrue(true);
			}
		} else

		{
			log.info("No Package option");
		}
		// waitforseconds(2);
		boolean element1 = false;
		try {
			element1 = CheckXpath("PACKAGE_QUOTE_1").isDisplayed();
		} catch (Exception e) {

		}
		if (element1) {
			{
				if (CheckXpath("PACKAGE_QUOTE_1").isEnabled()) {
					log.info("RESULT-8:: Package Quote option is enabled at edit RFQ for assembly 2");
					Assert.assertTrue(false);
				} else {
					log.info("RESULT-8:: Package Quote option is disabled at edit RFQ for assembly 2");
					Assert.assertTrue(true);
				}
			}
		} else {
			log.info("No more Assemblies");
		}
		log.info("~>> 9.  Check Currency option");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("CURRENCY_CONVERSATION"));
		// CheckXpath("CURRENCY_CONVERSATION").click();
		if (CheckXpath("RFQ_CURRENCY").isEnabled()) {
			log.info("RESULT-9:: Currency  is editable at edit RFQ");
			Assert.assertTrue(false);
		} else {
			log.info("RESULT-9:: Currency is not editable at edit RFQ");
			Assert.assertTrue(true);
		}
		waitforseconds(2);
		driver.navigate().refresh();
		waitforseconds(50);
		CheckXpath("RFQ_SUBMIT").click();
	}

	public void checklistBuilder() {

		boolean validation = false;
		try {
			validation = driver.findElement(By.xpath(".//*[@id='validationsummary']")).isDisplayed();
		} catch (Exception e) {

		}
		if (validation) {
			List<WebElement> errorsList = driver
					.findElements(By.xpath(".//*[@id='validationsummary']//ul//li[@aria-hidden=\"false\"]//label"));
			String[] errors = new String[errorsList.size()];
			for (int i = 0; i < errorsList.size(); i++) {
				errors[i] = errorsList.get(i).getText();
			}

			log.info("Total RFQ checklist mandatory fields are  :: " + errors.length);
			for (int i = 0; i < errors.length; i++) {

				if (errors[i].toLowerCase().contains("select")) {
					errors[i] = errors[i].substring(errors[i].indexOf("select") + 7, errors[i].length());
				} else if (errors[i].toLowerCase().contains("valid")) {
					errors[i] = errors[i].substring(errors[i].indexOf("valid") + 6, errors[i].length());
				}
				// log.info(errors[i]);
			}

			for (int i = 0; i < errors.length; i++) {
				String field_name = errors[i];
				log.info(i + "Field Name :: " + field_name);

				WebElement src = driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
						+ field_name + "')]//following-sibling::div"));

				String field_type = src.getAttribute("src");
				log.info("Field Type :: " + field_type);
				if (field_type != null) {
					if (field_type.contains("input.html")) {
						String sub_type = driver
								.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
										+ field_name + "')]//following-sibling::div//input"))
								.getAttribute("type");
						log.info("Type ::  " + sub_type);

						switch (sub_type) {

						case "password": {
							log.info("in switch password");
							driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
									+ field_name + "')]//following-sibling::div//input")).sendKeys("12345");
							break;
						}

						case "email": {
							log.info("in switch email");
							driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
									+ field_name + "')]//following-sibling::div//input")).sendKeys("Test@gmail.com");
							break;
						}

						case "text": {
							log.info("in switch text");
							driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
									+ field_name + "')]//following-sibling::div//input")).sendKeys("123456");
							break;
						}

						case "TextBox": {
							log.info("in switch Textbox");
							driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
									+ field_name + "')]//following-sibling::div//input")).sendKeys("123456");
							break;
						}
						}

					}

					else if (field_type.contains("date.html")) {
						log.info("select date");
						driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
								+ field_name + "')]//following-sibling::div//span//button")).click();
						driver.findElement(By.xpath("//button[@class='btn btn-default btn-sm active']")).click();

					}

					else if (field_type.contains("number.html")) {
						log.info("number");
						driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']//label[contains(text(),'"
								+ field_name + "')]//following-sibling::div//input")).sendKeys("2");
					}

					else if (field_type.contains("radiobutton.html")) {
						log.info("radio button");
						driver.findElement(By.xpath("(.//*[@id='RFQChecklistSection']//label[contains(text(),'"
								+ field_name + "')]//following-sibling::div//input)[1]")).click();
					}

					else if (field_type.contains("checkbox.html")) {
						log.info("checkbox");
						driver.findElement(By.xpath("(.//*[@id='RFQChecklistSection']//label[contains(text(),'"
								+ field_name + "')]//following-sibling::div//input)[1]")).click();
					}
				} else {
					log.info("Dropdown");
					src.click();
					driver.findElement(By.linkText("Check All")).click();
					driver.findElement(By.xpath(".//*[@id='RFQChecklistSection']")).click();

				}
			}
			CheckXpath("RFQ_SUBMIT").click();
		} else {
			log.info("~>> No mandatory validation for RFQ checklistbuilder");
		}
	}

}