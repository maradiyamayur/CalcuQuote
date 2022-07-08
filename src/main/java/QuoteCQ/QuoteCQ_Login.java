package QuoteCQ;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityMethods;

public class QuoteCQ_Login extends BaseInitquoteCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "QuoteCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	// @Parameters({ "correctusername", "correctpassword", })
	@Parameters("cqUrl")
	@Test(description = "#. HAPPY PATH :: QCQ - LOGIN TO QUOTE CQ")
	public void frontLogin(String cqUrl) throws InterruptedException, IOException {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : LOGIN TO QUOTE CQ");
		System.out.println("CASE : LOGIN TO QUOTE CQ");
		UtilityMethods.signIN(cqUrl);
		long start = System.currentTimeMillis();
		waitforseconds(8);
		if (driver.getTitle().equals("CalcuQuote - Dashboard")) {
			log.info("RESULT  1 :: Login Done Successfully ");
			log.info("RESULT  2 :: Redirected to " + driver.getTitle() + "Page");
			// Assert.assertTrue(true);
			System.out.println("RESULT :: Login Done Successfully");
		} else {
			log.info("RESULT :: Login Failed");
			// Assert.assertTrue(false);
		}
		long finish = System.currentTimeMillis();
		long totalTime = finish - start;
		long secondtime = (totalTime / 1000) % 60;
		System.out.println(">>> Time to login  ::  " + secondtime + " Second(s)");
		log.info("RESULT  3 :: Time to login  :: " + secondtime + " Second(s)");
		long limit = 60;
		if (secondtime < limit) {
			log.info("RESULT  4 :: Login done within 60 seconds");
			// Assert.assertTrue(true);
		} else {
			log.info("RESULT  4 :: Login took more than 60 seconds");
			// Assert.assertTrue(false);
		}
		log.info("----------------------------------------------------------------------------");
		System.out.println(
				".........................................................................................................");
	}
}
