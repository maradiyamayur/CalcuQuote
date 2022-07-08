package SmokeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import BidCQ.BaseInitbidCQ;
import BidCQ.BidCQ_BidGenerate;

import ShopCQ.ShopCQ_ExportPO;
import utility.UtilityMethods;

public class Smoke_Import_Export_Bid extends BaseInitsmokeTest {
	
	
	public static String strfile;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}
	
	@Test(priority = 1, description = "#. CREATE BID")
	public void bidCreate() throws Exception
	{		
		  headerFormat("CASE :: CREATE BID");
		  //writeFile();
		  BidCQ_BidGenerate BG = new BidCQ_BidGenerate(); 
		  BG.checkstatus("SELECT_ALL_LINES"); 
		  BG.addtobid(0);
		  waitforseconds(5); 
		  driver.navigate().refresh();
		  alertacceptpopup();
		  waitforseconds(5);
	}
	
	
	@Test(priority = 2, description = "#. EXPORT BID IN BIDCQ")
	public void bidExport() throws Exception {
		headerFormat("CASE :: EXPORT BID IN BIDCQ");	
		boolean bidinfo = false;
		try {
			bidinfo = driver
					.findElement(By.xpath(".//div[@class='modal-header HeaderText']//span[@ng-click='closeDrower()']"))
					.isDisplayed();
		} catch (Exception e) {

		}
		if (bidinfo) {
			log.info("~>> Bid Information popup displayed");
			System.out.println("~>> Bid Information popup displayed");
			safeJavaScriptClick(CheckXpath("BID_INFO"));
		} else {
			log.info("~>> Bid Information popup not displayed");
			System.out.println("~>> Bid Information popup not displayed");
		}
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("BID_SHEET_EXCEL"));
		safeJavaScriptClick(CheckXpath("BID_SHEET_EXPORT"));
		waitforseconds(8);
		ShopCQ_ExportPO.VerifyDownloadWithFileName("Bid", ".xlsx");
	}
	
	@Test(priority = 3, description = "#. IMPORT BID IN BIDCQ")
	public void bidImport() throws Exception {
		headerFormat("CASE :: IMPORT BID IN BIDCQ");
		writeFile(4, 17); 
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("BID_SHEET_EXCEL"));
		safeJavaScriptClick(CheckXpath("BID_SHEET_IMPORT"));
		waitforseconds(8);
		importFile();
		waitforseconds(8);
		boolean alert = false;
		try
		{
			alert = driver.findElement(By.xpath("swal2-modal swal2-show")).isDisplayed();
		}
		catch(Exception e)
		{
			
		}
		if(alert)
		{
			System.out.println("~>> RESULT :: IMPORT NOT DONE SUCCESSFULLY");
			log.info("~>> RESULT :: IMPORT NOT DONE SUCCESSFULLY");
			Assert.assertTrue(false);
		}
		else
		{
			System.out.println("~>> RESULT :: FILE IMPORTED SUCCESSFULLY");
			log.info("~>> RESULT :: FILE IMPORTED SUCCESSFULLY");
			Assert.assertTrue(true);
		}
		waitforseconds(8);
		BidCQ_BidGenerate BG = new BidCQ_BidGenerate();
		int intunit = BG.getIndex("Unit Price");
		WebElement Unitprice = driver
				.findElement(By.xpath(".//table[@class='htCore']//tbody//tr[2]//td[" + intunit + "]"));
		System.out.println("~>> Updated Unit Price :: " + Unitprice.getText());
		if(Unitprice.getText().equals("2"))
		{
			System.out.println("~>> Unit Price Updated Correctly");
		}
		else
		{
			System.out.println("~>>  Unit Price Updated Wrongly");
		}
	
	}

	@Test(priority = 4, description = "#. IMPORT BID IN MATERIAL COSTING")
	public void bidImportMaterial() throws Exception {	
		headerFormat("CASE :: IMPORT BID IN MATERIAL COSTING");
		writeFile(3,17);
		BaseInitbidCQ.switchToNewtabWithUrl(driver, 0);
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("MATERIAL_ACTION_BUTTON"));
		System.out.println("~>> Clicked on Action button");
		waitforseconds(2);
		Actions action = new Actions(driver);
		WebElement BidSheet = CheckXpath("MATERIAL_ACTION_BIDSHEET");
		action.moveToElement(BidSheet).perform();
		waitforseconds(2);
		safeJavaScriptClick(CheckXpath("MATERIAL_ACTION_BIDSHEET_IMPORT"));
	    importFile();
		verifyPricing();
	}
	

	public void importFile() throws Exception
	{
		waitforseconds(5);
		strfile = ShopCQ_ExportPO.fileName;
		File file = new File(System.getProperty("user.home")+ "\\Downloads\\" + strfile);		
		String absolute = file.getCanonicalPath();
		WebElement addfile = driver.findElement(By.xpath(".//input[@id='lineItemExcel']"));
		addfile.sendKeys(absolute);
		waitforseconds(10);
		log.info("~>> File uploaded");
		safeJavaScriptClick(CheckXpath("BID_IMPORT_BUTTON"));	
		System.out.println("~>> File Imported");
	}
	
	
	public void writeFile(int row, int col) throws IOException
	{
		headerFormat("~>> UPDATING EXPORTED BIDSHEET");
		strfile = ShopCQ_ExportPO.fileName;
		File file = new File(System.getProperty("user.home")+ "\\Downloads\\" + strfile);	
		FileInputStream fis = new FileInputStream(file);		
		XSSFWorkbook wb = new XSSFWorkbook(fis);			
		if(file.isFile() && file.exists())
		{
			XSSFSheet sh = wb.getSheetAt(0);
			Cell cell = null;	
			cell = sh.getRow(row).createCell(col);	
			log.info("~>> Unit Price updated");
			System.out.println("~>> Unit Price updated");
			cell.setCellValue("2");
			fis.close();
			FileOutputStream output_file =new FileOutputStream(file);
			wb.write(output_file);
			output_file.close();
			log.info("~>> File Closed");
		}
		else
		{
			System.out.println("CAN'T OPEN FILE");
		}
	}
	
	
	public void verifyPricing()
	{
		headerFormat("~>> VERIYING UNIT PRICE IMPORTED OR NOT ?");
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 3000", scrollbar);
		waitforseconds(8);
		boolean pricing = false;
		try
		{
			pricing = driver.findElement(By.xpath("(.//a[@uib-tooltip='Pricing Available  ']//i[contains(@class,'fa-square')])[1]")).isDisplayed();
		}
		catch(Exception e)
		{
			
		}
		if(pricing)
		{
			System.out.println("RESULT :: Unit Price Imported Successfully");
			log.info("~>> RESULT :: Unit Price Imported Successfully");
			Assert.assertTrue(true);
		}
		else
		{
			System.out.println("RESULT :: Unit Price not Imported Successfully");
			log.info("~>> RESULT :: Unit Price not Imported Successfully");
			Assert.assertTrue(false);
		}
	}
}