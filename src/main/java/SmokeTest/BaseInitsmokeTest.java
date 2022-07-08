package SmokeTest;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import QuoteCQ.BaseInitquoteCQ;
import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitsmokeTest extends BaseInit {

	@BeforeSuite
	public void checkExecutionModeTestSuite() throws IOException {
		startup();
		// Code to check execution mode of Test Suite
		if (!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "SmokeTest", "TestSuite")) {
			throw new SkipException("Execution mode of the test suite is set to NO");
		}
	}

	public void chkOpenQuote() throws Exception {
		boolean openQuote = false;
		try {
			openQuote = driver.findElement(By.xpath(".//*[@id='HeaderShow']")).isDisplayed();
		} catch (Exception e) {

		}
		if (openQuote) {
			System.out.println("~>> Quote is already open");
		} else {
			BaseInitquoteCQ.RFQ_list();
			BaseInitquoteCQ.first_RFQ();
			waitforseconds(5);
		}
	}
	
	public void summaryScroll(String strName)
	{
		waitforseconds(5);
		boolean treeMinus = false;
		try
		{
			treeMinus = CheckXpath("SUMMARY_HEADER_MINUS").isDisplayed();
		}		
		catch(Exception e)
		{
			
		}
		if(treeMinus)
		{
			CheckXpath("SUMMARY_HEADER_MINUS").click();
		}
		waitforseconds(5);
		WebElement element = CheckXpath("MISC_COST_HEADER");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		waitforseconds(8);
		driver.findElement(By.xpath(".//div[text()='"+strName+"']//preceding::i[@class='ui-grid-icon-plus-squared'][1]")).click();
		waitforseconds(2);
	}
	
	public void verifyResult(Double expResult, Double actResult)
	{
	//if(Math.abs((Math.round(expResult * 100.0) / 100.0)-(Math.round(actResult * 100.0) / 100.0))<0.000001)
if(Double.compare((Math.round(expResult * 100.0) / 100.0),(Math.round(actResult * 100.0) / 100.0))==0)
	{
		System.out.println("~>> RESULT :: MATCHED");
		log.info("~>> RESULT :: MATCHED");
		Assert.assertTrue(true);
	}
	else
	{
		System.out.println("~>> RESULT :: NOT MATCHED");
		log.info("~>> RESULT :: NOT MATCHED");
		Assert.assertTrue(false);
	}

	}
}
