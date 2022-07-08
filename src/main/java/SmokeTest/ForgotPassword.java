package SmokeTest;

import java.time.Duration;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class ForgotPassword extends BaseInitsmokeTest {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. CASE : FORGOT PASSWORD ")
	public void forgotPwd() throws Exception {

		headerFormat("#. CASE :  FORGOT PASSWORD ");
		boolean isDisplay = false;
		try {
			isDisplay = driver.findElement(By.xpath(".//*[@id='divlogin']//h3[text()='Login']")).isDisplayed();
		} catch (Exception e) {

		}
		if (isDisplay) {
			System.out.println("~>> Login screen displayed");
			forgot();
		} else {
			LogOut Lo = new LogOut();
			Lo.logOut();
			forgot();
		}
	}

	public void forgot() throws Exception {
		safeJavaScriptClick(CheckXpath("FORGOT_PASSWORD"));
		log.info("~>> Clicked on  Forgot Password");
		CheckXpath("FP_USER_EMAIL").sendKeys(UtilityMethods.Username);
		log.info("~>> Adding Email address");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("FP_SEND_MAIL_BTN"));
		log.info("~>> Clicked on Send mail button");
		/*
		 * Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(80,
		 * TimeUnit.SECONDS) .pollingEvery(1,
		 * TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		 */

		
		 Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			        .withTimeout(Duration.ofSeconds(80))
			        .pollingEvery(Duration.ofSeconds(2))
			        .ignoring(NoSuchElementException.class);
			/*
			 * Wait<WebDriver> wait = new
			 * FluentWait(driver).withTimeout(Duration.ofSeconds(80)).pollingEvery(Duration.
			 * ofSeconds(2)) .ignoring(NoSuchElementException.class);
			 */

			/*
			 * Wait<WebDriver> wait = new
			 * FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(80))
			 * .pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class);
			 */

		wait.until(ExpectedConditions.visibilityOf(
				driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']"))));
		if (driver.findElement(By.xpath(".//div[@id='toast-container']//div[@class='toast-message']")).getText()
				.contains("password reset link has been sent to your email")) {
			System.out.println("~>> RESULT :: PASSWORD RESET LINK HAS BEEN SENT SUCCESSFULLY");
			log.info("~>> RESULT :: PASSWORD RESET LINK HAS BEEN SENT SUCCESSFULLY");
			Assert.assertTrue(true);
		} else {
			System.out.println("~>> RESULT :: PASSWORD RESET LINK SENT FAILED");
			log.info("~>> RESULT ::PASSWORD RESET LINK SENT FAILED");
			Assert.assertTrue(false);
		}
	}
}