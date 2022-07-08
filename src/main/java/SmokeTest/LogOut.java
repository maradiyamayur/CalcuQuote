package SmokeTest;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class LogOut extends BaseInitsmokeTest {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. CASE : LOGOUT FROM APPLICATION ")
	public void logOut() throws Exception {
		headerFormat("#. CASE : LOGOUT FROM APPLICATION ");
		safeJavaScriptClick(CheckXpath("USER_PROFILE"));
		log.info("~>> Clicked on  User Profile");
		safeJavaScriptClick(CheckXpath("LOGOUT_BTN"));
		log.info("~>> Clicked on Log Out button");
		alertacceptpopup();
		waitforseconds(10);
		if (driver.findElement(By.xpath(".//*[@id='divlogin']//h3[text()='Login']")).isDisplayed()) {
			System.out.println("~>> RESULT :: LOGOUT DONE SUCCESSFULLY");
			log.info("~>> RESULT :: LOGOUT DONE SUCCESSFULLY");
			Assert.assertTrue(true);
		} else {
			System.out.println("~>> RESULT :: LOGOUT OPERATION FAILED");
			log.info("~>> RESULT :: LOGOUT OPERATION FAILED");
			Assert.assertTrue(false);
		}
	}
}