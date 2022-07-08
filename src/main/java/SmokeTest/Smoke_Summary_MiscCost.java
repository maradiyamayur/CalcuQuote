package SmokeTest;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import QuoteCQ.BaseInitquoteCQ;
import utility.UtilityMethods;

public class Smoke_Summary_MiscCost extends BaseInitsmokeTest {
	
	public WebElement MiscCost;
	
		@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. GO TO MISC COST")
	public void MiscCost() throws Exception {
		waitforseconds(20); 
		BaseInitquoteCQ.RFQ_list();
		BaseInitquoteCQ.first_RFQ();
		waitforseconds(5); 
		safeJavaScriptClick(CheckXpath("SUBMIT_TAB"));
		log.info("~>> Redirected to Summary Tab");
		waitforseconds(10);
		summaryScroll("Misc Cost");
		driver.findElement(By.xpath(".//a[contains(@class,'AddCostCursor')]")).click();
		summaryScroll("Misc Cost");	
	}
	
	@Test(priority = 2, description = "#. ADD MISC COST")
	public void addMiscCost() throws Exception {
		headerFormat("#. CASE : ADDING MISC COST");
		String id = driver.findElement(By.xpath("(.//span[contains(text(),'Misc Cost Misc')])[last()]//preceding::div[@role='gridcell'][1]")).getAttribute("id").replace("-cell", "");		
		String id1 = id.split("-")[0].concat("-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		WebElement eleMiscCost = driver.findElement(By.xpath("(.//div[contains(@id,'"+id1+"')])[last()]"));
		Actions action = new Actions(driver);
		action.moveToElement(eleMiscCost).doubleClick().perform();
		waitforseconds(3);
		action.sendKeys("100").perform();
		System.out.println("~>> Additional Misc Cost added :: 100");
		log.info("~>> Additional Misc Cost added :: 100");
		safeJavaScriptClick(CheckXpath("SUMMARY_SAVE_AS_DRAFT"));
		waitforseconds(15);		
	}
	
	@Test(priority = 3, description = "#. GET EXISTING MISC COST ADDED MISC COST AND VERIFYING MISC COST TOTAL")
	public void getMiscCost()
	{
		headerFormat("#. CASE : GETTING EXISTING ADDED MISC COST");
		summaryScroll("Misc Cost");
		verifyMiscCost();
	}
	
	public void verifyMiscCost()
	{
		headerFormat("#. CASE : VERIFYING MISC COST TOTAL");
		WebElement elemiscCost = driver.findElement(By.xpath("(.//span[contains(text(),'Misc Cost Misc')])[1]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elemiscCost);
		waitforseconds(8);
		String id = driver.findElement(By.xpath(".//button[@id='btn-append-to-body']//preceding::div[@role='gridcell'][1]")).getAttribute("id").replace("-cell", "");		
		String id2 = id.split("-")[0].concat("-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		MiscCost = driver.findElement(By.xpath("(.//div[contains(@id,'"+id2+"')])[last()]"));
		String costId = MiscCost.getAttribute("id");
		String id1 = MiscCost.getAttribute("id").split("-")[2].concat("-").concat(MiscCost.getAttribute("id").split("-")[3]);
		log.info("id1 - " + id1);
		int firstrange = Integer.parseInt(costId.split("-")[1]);
		log.info("first - " + firstrange);
		waitforseconds(5);
		String matSubId = driver.findElement(By.xpath("(.//span[contains(text(),'Misc Cost Total')])[last()]//preceding::div[2]")).getAttribute("id");
		int lastrange = Integer.parseInt(matSubId.split("-")[1]);
		log.info("last - " + lastrange);
		Double expMiscTotal = (double) 0;
		for (int i = firstrange; i <= lastrange; i++) {
			String strNumber = driver.findElement(By.xpath("(.//div[contains(@id,'"+id1+"')]//div//span)[" + i + "]"))
					.getText();
			System.out.println("~>> Added Misc Cost - " + strNumber);
			 if (strNumber != null && strNumber.length() > 0) {
				 expMiscTotal  += Double.parseDouble(strNumber);
			 }
		}
		System.out.println("~>> EXPECTED MISC COST TOTAL - " + expMiscTotal);
		log.info("~>> EXPECTED MISC COST TOTAL - " + expMiscTotal);
		int total = lastrange+1;
		double actMiscTotal = Double.parseDouble(driver.findElement(By.xpath("(.//div[contains(@id,'"+id1+"')]//div//span)[" + total + "]"))
				.getText());
		System.out.println("~>> ACTUAL MISC COST TOTAL - " + actMiscTotal);
		log.info("~>> ACTUAL MISC COST TOTAL - " + actMiscTotal);
		if(Math.abs(actMiscTotal-expMiscTotal)<0.000001)
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