package SearchCQ;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class SearchCQ_Login extends BaseInitsearchCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SearchCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Parameters("cqUrl")
	@Test(description = "#. HAPPY PATH :: SEARCH CQ")
	public void frontLogin(String cqUrl) throws InterruptedException, IOException {
		log.info("----------------------------------------------------------------------------");
		log.info("CASE : LOGIN TO SEARCH CQ");
		System.out.println("CASE : LOGIN TO SEARCH CQ");
		driver.get(cqUrl);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(100));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='companyName']")));
		log.info("~>> Enter Company Name");
		CheckXpath("SEARCH_CQ_COMPANY").clear();
		CheckXpath("SEARCH_CQ_COMPANY").sendKeys("regression");
		log.info("~>> Enter UserName");
		CheckXpath("USER_NAME").clear();
		CheckXpath("USER_NAME").sendKeys(UtilityMethods.Username);
		log.info("~>> Enter Password");
		CheckXpath("PASSWORD").clear();
		CheckXpath("PASSWORD").sendKeys(prop.getProperty("CQ_PASSWORD"));
		log.info("~>> Click on Login Button");
		CheckXpath("LOGIN").click();
		waitforseconds(5);
		UtilityMethods.url = driver.getCurrentUrl().replace("https://", "").replace("/#/DashBoard", "");
		Thread.sleep(8000);
		if (driver.getTitle().equals("CalcuQuote-SearchCQ")) {
			log.info("RESULT  1 :: Login Done Successfully ");
			log.info("RESULT  2 :: Redirected to " + driver.getTitle() + "Page");
			Assert.assertTrue(true);
			System.out.println(">>> Login Done Successfully");
		} else {
			log.info("RESULT ::   Login Failed");
			Assert.assertTrue(false);
		}
		log.info("----------------------------------------------------------------------------");
	}
}
