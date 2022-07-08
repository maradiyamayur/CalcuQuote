package SmokeTest;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import QuoteCQ.QuoteCQ_AddLabor;
import utility.UtilityMethods;

public class Smoke_Summary_Labor extends BaseInitsmokeTest {
	
	public static double labor$,MOH$,setup$,laborHr,MOHHr,setupHr,Labor_Sub_Total,Labor_Total,Labor_Markup,Labor_Margin;
	public double actResult;
	public double expResult;
	

		@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. ADD LABOR ACTIVITY")
	public void addLabor() throws Exception {
		headerFormat("#. CASE : ADD LABOR ACTIVITY");
		chkOpenQuote();
		waitforseconds(5); 
		safeJavaScriptClick(CheckXpath("LABOR_TAB"));
		log.info("~>> Redirected to Labor Tab ");
		QuoteCQ_AddLabor  QL= new QuoteCQ_AddLabor();
		QL.add();
		QL.laborsubmit();	
	}
	

	@Test(priority = 2, description = "#. CHECK SUMMARY TOTAL FOR LABOR")
	public void getLabor() throws Exception {
		headerFormat("#. CASE : CHECK SUMMARY TOTAL FOR LABOR");
		//summaryScroll("Labor Total");
		 ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		 waitforseconds(3);
		 ((JavascriptExecutor)driver).executeScript("window.scrollBy(2000,0)");	
		 waitforseconds(3);
		 labor$ = getValue(1,"Labor");
		 MOH$ = getValue(1,"MOH");
		 setup$ = getValue(1,"Setup");
		 laborHr = getValue(2,"Labor");
		 MOHHr = getValue(2,"MOH");
		 setupHr = getValue(2,"Setup");
	}

	
	@Test(priority = 3, description = "#. VERIFY CALCULATION ON SUMMARY SCREEN FOR LABOR")
	public void verifyCalcLabor() throws Exception {
		headerFormat("#. CASE : VERIFY CALCULATION ON SUMMARY SCREEN FOR LABOR");
		safeJavaScriptClick(CheckXpath("SUBMIT_TAB"));
		log.info("~>> Redirected to Summary Tab");
		waitforseconds(10);
		//scroll();
		summaryScroll("Labor Sub Total");
		getResult("Setup",setup$);
		getResult("MOH",MOH$);
		getResult("Variable",labor$);
		Smoke_Summary_Material SSM = new Smoke_Summary_Material();
		Labor_Sub_Total =	SSM.verifyOtherCost(2, "Labor");
		verifyLaborMarkup();
	}
	
	public double getValue(int type,  String laborType)
	{
		List<WebElement>lstLabor = null;
		if(type==1)
		{
		lstLabor = driver.findElements(By.xpath(".//span[contains(@id,'spn') and contains(@id,'"+laborType+"')]"));
		}
		if(type==2)
		{
		lstLabor = driver.findElements(By.xpath(".//div[contains(@id,'dv') and contains(@id,'"+laborType+"')]"));
		}
		double sum = 0;
		for(WebElement eleLabor : lstLabor)
		{
             sum+=Double.parseDouble(eleLabor.getText());
		}
		System.out.println("~>> Total " + laborType + " - " + sum);
		return sum;
	}
	
	
	public void  getResult(String strName, double calc)
	{	
		boolean bolLabor = false;
		try
				{
			driver.findElement(By.xpath("(.//span[contains(text(),'" + strName
					+ "') and contains(text(),'Lab')])[1]//preceding::div[2]")).isDisplayed();
				}
		catch(Exception e)
		{
			
		}
		if(bolLabor)
		{
		WebElement ele = driver.findElement(By.xpath("(.//span[contains(text(),'" + strName
				+ "') and contains(text(),'Lab')])[1]//preceding::div[2]"));
		String id = ele.getAttribute("id").replace("-cell", "");
		String id1 = id.split("-")[0].concat("-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		actResult = Double.parseDouble(
				driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//span[@class='ng-binding']")).getText()
						.replaceAll("\\s", ""));
		expResult = calc;	
		
		verifyResult(expResult, actResult);
//		String strExpected = String.format("%.6f", expResult);
//		String strActual = String.format("%.6f", actResult);
//		
//		System.out.println("~>> Actual " + strName + " - " + strActual);
//		log.info("~>> Actual " + strName + " - " + strActual);	
//		System.out.println("~>> Expected " + strName + " - " + strExpected);
//		log.info("~>> Expected " + strName + " - " +strExpected);	
//		 if(strExpected.equals(strActual))
//		  {
//		  System.out.println("~>> RESULT :: MATCHED");
//		  log.info("~>> RESULT :: MATCHED"); 
//		 // Assert.assertTrue(true);
//			  } 
//		  else
//		  {
//		  System.out.println("~>> RESULT :: NOT MATCHED");
//		  log.info("~>> RESULT :: NOT MATCHED"); 
//		  //Assert.assertTrue(false);	 
//		  }
			System.out.println("");	
		}
	}
	
	public void verifyLaborMarkup()
	{
		summaryScroll("Labor Total");
		System.out.println("#. CASE : CALCULATING LABOR MARKUP$");
		String markupid = driver.findElement(By.xpath("(.//span[contains(text(),'Labor') and contains(text(),'Markup')])[1]//preceding::div[@role='gridcell'][1]")).getAttribute("id");
		String  markupid1= markupid.split("-")[0].concat("-").concat(markupid.split("-")[1]);		
		Double Markup = Double.parseDouble(driver.findElement(By.xpath("(.//div[contains(@id,'"+markupid1+"')]//span)[last()]")).getText());	
		System.out.println("~>> Labor Markup  - " + Markup);
	
		String markupidPer = driver.findElement(By.xpath("(.//span[contains(text(),'Labor') and contains(text(),'Markup')])[2]//preceding::div[@role='gridcell'][1]")).getAttribute("id");
		String  markupidPer1= markupidPer.split("-")[0].concat("-").concat(markupidPer.split("-")[1]);
		Double ActMarkupPer = Double.parseDouble(driver.findElement(By.xpath("(.//div[contains(@id,'"+markupidPer1+"')]//span)[last()]")).getText());
		System.out.println("~>> Actual Labor Markup :: " + ActMarkupPer);
		
		//System.out.println("********************************************************************");
		Labor_Markup = Labor_Sub_Total*(Markup/100);
		System.out.println("~>> Expected Labor MarkUp :: " + Labor_Markup);
		String strActMarkup = String.format("%.6f",ActMarkupPer);
		String strExpMarkup = String.format("%.6f", Labor_Markup);
		
		if(strActMarkup.equals(strExpMarkup)) 
		{
			System.out.println("~>> RESULT :: MATCHED");
			log.info("~>> RESULT :: MATCHED");
		}
		else
		{
			System.out.println("~>> RESULT :: NOT MATCHED");
			log.info("~>> RESULT :: NOT MATCHED");
		}	
		System.out.println("");
		System.out.println("#. CASE : CALCULATING LABOR MARGIN");
		String markupidmar = driver.findElement(By.xpath("(.//span[contains(text(),'Labor') and contains(text(),'Margin')])[1]//preceding::div[@role='gridcell'][1]")).getAttribute("id");
		String  markupidmar1= markupidmar.split("-")[0].concat("-").concat(markupidmar.split("-")[1]);
		Double ActMarkupmar = Double.parseDouble(driver.findElement(By.xpath("(.//div[contains(@id,'"+markupidmar1+"')]//span)[last()]")).getText());
	
		//Smoke_Summary_Material SSM = new Smoke_Summary_Material();
		//Labor_Sub_Total = SSM.expSubTotal;
		Labor_Total = Double.sum(Labor_Sub_Total,Labor_Markup);
		System.out.println("~>> Labor Total :: " + Labor_Total);
		System.out.println("~>> Actual Labor Margin :: " + ActMarkupmar);
		Labor_Margin = (Labor_Markup*100)/Labor_Total;	
		System.out.println("~>> Expected Labor Margin :: " +  Labor_Margin);
		
		String strActMargin = String.format("%.6f", ActMarkupmar);
		String strExpMargin = String.format("%.6f", Labor_Margin);
		
		if(strActMargin.contains(strExpMargin)) {
			System.out.println("~>> RESULT :: MATCHED");
			log.info("~>> RESULT :: MATCHED");
		}
		else
		{
			System.out.println("~>> RESULT :: NOT MATCHED");
			log.info("~>> RESULT :: NOT MATCHED");
		}
		System.out.println("");
		System.out.println("#. CASE : CALCULATING LABOR TOTAL");
		System.out.println("~>> Expected Labor Total :: " + Labor_Total);
		int materialTotal = Integer.parseInt(markupidmar.split("-")[1])+1;
		String materialTotalid = markupidmar.split("-")[0].concat("-").concat(Integer.toString(materialTotal));		
		Double ActMaterialTotal = Double.parseDouble(driver.findElement(By.xpath("(.//div[contains(@id,'"+materialTotalid+"')]//span)[last()]")).getText());
		System.out.println("~>> Actual Labor Total :: " + ActMaterialTotal);
		String strActTotal = String.format("%.6f",ActMaterialTotal);
		String strExpTotal = String.format("%.6f", Labor_Total);
		if(strActTotal.equals(strExpTotal)) {
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