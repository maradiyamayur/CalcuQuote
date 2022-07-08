package SmokeTest;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import utility.UtilityMethods;

public class Smoke_Summary_Overhead extends BaseInitsmokeTest {

	public String strRFQcurrency, currentUrl;
	public ArrayList<String> arrHours, arrDollars;
	public double sumSales, sumHours, salesOverhead, hoursOverhead;
	
	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. GET RFQ CURRENCY")
	public void getCurrency() throws Exception {
		headerFormat("#. GET RFQ CURRENCY");
		waitforseconds(20); 
		chkOpenQuote();
		strRFQcurrency = driver.findElement(By.xpath("(.//*[@id='currencyDropDown']//option)[1]")).getText();
		System.out.println("~>> RFQ Currency :: " + strRFQcurrency);
		log.info("~>> RFQ Currency :: " + strRFQcurrency);
		currentUrl = driver.getCurrentUrl();
	}
	
	@Test(priority = 2, description = "#. GET CONFIGURED OVERHEAD DRIVERS FOR RFQ CURRENCY")
	public void overheadDrivers() throws Exception {
		headerFormat("#. GET CONFIGURED OVERHEAD DRIVERS FOR RFQ CURRENCY");
		safeJavaScriptClick(CheckXpath("CONFIGURATION_TAB"));
		log.info("~>> Clicked on Configuration tab");
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("OVERHEAD_TAB"));
		log.info("~>> Clicked on Overhead tab");
		arrDollars = getdrivers("Sales Dollars");
		arrHours = getdrivers("Labor Hours");		
	}
	
	@Test(priority = 3, description = "#. GET OVERHEAD COST POOLS BASED ON OVERHEAD DRIVERS")
	public void overheadCost() throws Exception
	{
		headerFormat("#. GET ACTIVITY RATE FROM OVERHEAD COST POOLS BASED ON OVERHEAD DRIVERS");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("OVERHEAD_COST_POOLS"));	
		log.info("~>> Clicked on Overhead Cost Pools");
		waitforseconds(5);
		CheckXpath("OVERHEAD_ACTIVE").clear();
		CheckXpath("OVERHEAD_CURRENCY").clear();
		CheckXpath("OVERHEAD_CURRENCY").sendKeys(strRFQcurrency);
		CheckXpath("OVERHEAD_ACTIVE").sendKeys("true");
		waitforseconds(5);
		sumSales = getCost(arrDollars,"Sales Dollars");
		sumHours = getCost(arrHours, "Labor Hours");
	}
	
	@Test(priority = 4, description = "#. VERIFY OVERHEAD CALCULATION ON SUMMARY PAGE")
	public void overheadVerify() throws Exception
	{
		headerFormat("#. VERIFY OVERHEAD CALCULATION ON SUMMARY PAGE");
		driver.get(currentUrl);
		waitforseconds(8);
		safeJavaScriptClick(CheckXpath("SUBMIT_TAB"));
		log.info("~>> Redirected to Summary Tab");
		waitforseconds(8);
		summaryScroll("Overhead Costing");
		Double labor$total = Smoke_Summary_Labor.labor$ + Smoke_Summary_Labor.MOH$ +Smoke_Summary_Labor.setup$;
		salesOverhead = (labor$total + Smoke_Summary_Material.asmPrice)*sumSales;
		System.out.println("~>> TOTAL SALES OVERHEAD :: " + salesOverhead);
		
		Double laborHrtotal = Smoke_Summary_Labor.laborHr+Smoke_Summary_Labor.MOHHr+Smoke_Summary_Labor.setupHr;
		hoursOverhead = laborHrtotal*sumHours;
		System.out.println("~>> TOTAL HOURS OVERHEAD :: " + hoursOverhead);
		verifyOtherCost();
	}
	
	
	public ArrayList<String> getdrivers(String strName)
	{
		waitforseconds(5);
		CheckXpath("OVERHEAD_CURRENCY").clear();
		CheckXpath("OVERHEAD_ACTIVE").clear();
		CheckXpath("OVERHEAD_DRIVER_SHORTNAME").clear();
		log.info("~>> Search with RFQ Currency");			
		CheckXpath("OVERHEAD_ACTIVE").sendKeys("true");
		log.info("~>> Search with Active status");
		CheckXpath("OVERHEAD_DRIVER_SHORTNAME").sendKeys(strName);
		log.info("~>> Search with Short Name");
		waitforseconds(5);
		String id = driver.findElement(By.xpath(".//div[@role='button']//span[text()='Driver']")).getAttribute("id");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		log.info("~>> id1 - " +id1);
		List<WebElement>eleDrivers = driver.findElements(By.xpath(".//div[contains(@id,'"+id1+"')]//div"));
		ArrayList<String> arrDriver = new ArrayList<String>();		
		for(WebElement ele : eleDrivers)
		{
			arrDriver.add(ele.getAttribute("innerText"));
		}
		System.out.println("~>> Configured Drivers for " + strName +" - " + arrDriver);
		log.info("~>> Configured Drivers for " + strName +" - "  + arrDriver);
		return arrDriver;		
	}
	
	public double getCost(ArrayList<String> list, String strName)
	{
		waitforseconds(5);
		ArrayList<Double>arrRate = new ArrayList<Double>();
		for (int i=0;i<list.size();i++)
		{
			CheckXpath("OVERHEAD_COST_DRIVER").clear();
			CheckXpath("OVERHEAD_COST_DRIVER").sendKeys(list.get(i));
			waitforseconds(5);
			String id = driver.findElement(By.xpath(".//div[@role='button']//span[text()='Activity Rate']")).getAttribute("id");
			String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
			log.info("~>> id1 - " +id1);
			List<WebElement>lstActivities = driver.findElements(By.xpath(".//div[contains(@id,'"+id1+"')]//div"));
			waitforseconds(3);
			for(WebElement ele : lstActivities)
			{			
				arrRate.add(Double.parseDouble(ele.getAttribute("innerText")));				
			}
			
			log.info("~>> " + i+ " - "  +arrRate);
		}
		double sum = 0;
		for(int i=0;i<arrRate.size();i++)
		{
			
			sum += arrRate.get(i);
		}
		System.out.println("~>> Total Activity Rate for " + strName + " - "+ sum);
		log.info("~>> Total Activity Rate for " + strName + " - "+ sum);
		return sum;		
	}
	
	
	public void verifyOtherCost() {
		headerFormat("#. CASE : CALCULATING OVERHEAD TOTAL");
		waitforseconds(5);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);",
				driver.findElement(By.xpath(".//a[@class='AddCostCursor ng-binding ng-scope']")));
		waitforseconds(5);
		log.info("Scrolling done");
		String costId = driver
				.findElement(By.xpath(".//a[@class='AddCostCursor ng-binding ng-scope']//preceding::div[2]"))
				.getAttribute("id");
		int firstrange = Integer.parseInt(costId.split("-")[1]);
		log.info("First" + firstrange);
		String matSubId = driver.findElement(By.xpath(
				"(.//span[contains(text(),'Overhead') and contains(text(),'Total')])[last()]//preceding::div[2]"))
				.getAttribute("id");
		int lastrange = Integer.parseInt(matSubId.split("-")[1]);
		log.info("Last" + lastrange);
		Double sum = (double) 0;
		for (int i = firstrange + 2; i <= lastrange; i++) {
			sum += Double.parseDouble(driver.findElement(
					By.xpath(".//a[@class='AddCostCursor ng-binding ng-scope']//following::span[@class='ng-binding']["
							+ i + "]"))
					.getText());
			System.out.println("~>> Additional Over head Costing  - " +sum);
		}
		
		double expSubTotal = sum+salesOverhead+hoursOverhead;
		System.out.println("~>> Expected SubTotal :: " +  expSubTotal);
		
		int sub = lastrange + 1;
		double actSubTotal = Double.parseDouble(driver.findElement(By.xpath(
				".//a[@class='AddCostCursor ng-binding ng-scope']//following::span[@class='ng-binding'][" + sub + "]"))
				.getText());
		System.out.println("~>> Actual SubTotal :: " +  actSubTotal);
		
		verifyResult(expSubTotal,actSubTotal);
		System.out.println("");
	
	}
}