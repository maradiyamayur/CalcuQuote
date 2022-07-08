package SmokeTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.BaseInitquoteCQ;
import utility.UtilityMethods;

public class Smoke_Email extends BaseInitsmokeTest {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. EMAIL TO CUSTOMER")
	public void email() throws Exception {
		System.out.println("");
		headerFormat("#. CASE : EMAIL TO CUSTOMER");
		BaseInitquoteCQ.RFQ_list();
		safeJavaScriptClick(CheckXpath("RFQ_LIST_COMPLETED_FILTER"));
		waitforseconds(5);
		new Select(CheckXpath("RFQ_LIST_OTHER_STATUS_FILTER")).selectByVisibleText("Won");
		waitforseconds(15);
		boolean isrecord = false;
		try {
			isrecord = driver
					.findElement(
							By.xpath("(.//div[contains(@ng-if,'IsNreOnly')]//span[@class='steps' and text()='S'])[1]"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (isrecord) {
			System.out.println("~>> Completed Quote available to send Email");
			log.info("~>> Completed Quote available to send Email");
			driver.findElement(
					By.xpath("(.//div[contains(@ng-if,'IsNreOnly')]//span[@class='steps' and text()='S'])[1]")).click();
			waitforseconds(10);
			emailCustomer("Quote Report");
			emailCustomer("Detailed Quote Report");
			emailCustomer("Detailed Quote with Exception Report");
			emailCustomer("Exception Report");
			emailCustomer("Quote with Exception Report");
		} else {
			System.out.println("~>> No Completed Quote to send Email");
		}
		BaseInitquoteCQ.RFQ_list();
		safeJavaScriptClick(CheckXpath("RFQ_LIST_INPROGRESS_STATUS_FILTER"));
		log.info("~>> Clicked on In Progress status filter");
	}

	public void emailCustomer(String reportName) throws Exception {
		System.out.println("~>> Checking send Email functionality for " + reportName);
		log.info("~>> Checking send Email functionality for " + reportName);
		waitforseconds(15);
		safeJavaScriptClick(CheckXpath("SUBMIT_ACTION"));
		waitforseconds(2);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(".//a[text()='Send']"))).build().perform();
		waitforseconds(2);
		driver.findElement(By.xpath(".//a[text()='" + reportName + "']")).click();
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("SEND_MAIL_BUTTON"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(".//div[@id='swal2-content']"))));
		if (driver.findElement(By.xpath(".//div[@id='swal2-content']")).getText().contains("Email sent")) {
			System.out.println("~>>  RESULT :: " + reportName + " Email Sent Succcessfully");
			log.info("~>>  RESULT :: " + reportName + " Email Sent Succcessfully");
			Assert.assertTrue(true);
		} else {
			System.out.println("~>>  RESULT :: " + reportName + " Email not Sent Succcessfully");
			log.info("~>>  RESULT :: " + reportName + " Email not Sent Succcessfully");
			Assert.assertTrue(false);
		}
		System.out.println(
				"...............................................................................................................................");
	}
}