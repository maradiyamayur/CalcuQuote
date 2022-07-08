package Login;

import java.io.IOException;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class Login extends BaseInitLogin {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "login")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Parameters("cqUrl")
	@Test(description = "Login with Valid Credentail ")
	public void frontLogin(String cqUrl) throws InterruptedException, IOException {
		log.info("----------------------------------------------------------------------------");
		log.info("** Login with Valid Email id or Password");
		UtilityMethods.signIN(cqUrl);
		long start = System.currentTimeMillis();
		Thread.sleep(8000);
		if (driver.getTitle().equals("CalcuQuote - Dashboard")) {
			log.info("RESULT  1 :: Login Done Successfully ");
			log.info("RESULT  2 :: Redirected to " + driver.getTitle() + "Page");
			Assert.assertTrue(true);
			System.out.println(">>> Login Done Successfully");
		} else {
			log.info("RESULT ::   Login Failed");
			Assert.assertTrue(false);
		}
		long finish = System.currentTimeMillis();
		long totalTime = finish - start;
		long secondtime = (totalTime / 1000) % 60;
		System.out.println(">>> Time to login  ::  " + secondtime + " Second(s)");
		log.info("RESULT  3 :: Time to login  :: " + secondtime + " Second(s)");
		long limit = 60;
		if (secondtime < limit) {
			log.info("RESULT  4 :: Login done within 60 seconds");
			Assert.assertTrue(true);
		} else {
			log.info("RESULT  4 :: Login took more than 60 seconds");
			Assert.assertTrue(false);
		}
		log.info("----------------------------------------------------------------------------");
	}
}
