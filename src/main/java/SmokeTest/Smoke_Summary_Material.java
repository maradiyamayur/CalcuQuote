package SmokeTest;

import java.text.DecimalFormat;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import QuoteCQ.QuoteCQ_AutoSelect;
import QuoteCQ.QuoteCQ_ImportBOM;
import utility.UtilityMethods;

public class Smoke_Summary_Material extends BaseInitsmokeTest {

	QuoteCQ_ImportBOM IB = new QuoteCQ_ImportBOM();
	QuoteCQ_AutoSelect QAS = new QuoteCQ_AutoSelect();
	public static double qtyPer = 20.0, reqQty, leadQty = 30.0, attRate = 25;
	public static double asmPrice, unitPrice, totalQty, excessAmt, orderCost, ActResult, excessQty, expSubTotal;
	public double Material_Cost, Material_Lead_Cost, Material_Attr_Cost, Material_Sub_Total, Material_Markup,
			Material_Margin, Material_Total, Material_Amortize_Access;

	DecimalFormat df = new DecimalFormat("0.000000");

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "SmokeTest")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(priority = 1, description = "#. IMPORT BOM LINE ITEM")
	public void importLine() throws Exception {
		headerFormat("#. CASE : IMPORT BOM LINE ITEM");
		waitforseconds(20);
		chkOpenQuote();
		waitforseconds(5);
//		  boolean importBOM = false; 
//		  try 
//		  {
//			  importBOM =CheckXpath("IMPORT_BOM").isDisplayed(); 
//		  }
//		  catch (Exception e) {
//		  
//		  } 
//		  if (importBOM)
//		  { 
//			  IB.import_Steps();
//			  IB.import_upload("SummaryCalc.xlsx");
//		  } 
//		  else
//		  {
//		  log.info("~>> BOM line items are already available");
//		  safeJavaScriptClick(CheckXpath("BOM_ACTIONS_BUTTON")); 
//		  waitforseconds(3);
//		  safeJavaScriptClick(CheckXpath("DELETE_BOM")); 
//		  waitforseconds(8);
//		  alertacceptpopup(); 
//		  waitforseconds(5);
//		  log.info("~>> BOM Deleted Successfully");
//		  driver.navigate().refresh();
//		  waitforseconds(8);
//		  boolean spinner = false; 
//		  try 
//		  { 
//			  spinner = driver.findElement(By.xpath(".//i[@class='fa fa-spinner fa-spin']")). isDisplayed();
//		 } 
//		  catch (Exception e) 
//		  {
//		  
//		  }
//		  if (spinner) 
//		  { 
//			  driver.navigate().refresh();
//			  waitforseconds(15);
//			  }
//		  IB.import_Steps();
//		  IB.import_upload("SummaryCalc.xlsx");
//			}
//			waitforseconds(30);
//			IB.submitBOM();
//			waitforseconds(15); 
	}

	@Test(priority = 2, description = "#.  UPDATE PRICING AND AUTO SELECT")
	public void Pricing() throws Exception {
//		QuoteCQ_UpdatePricing QUP = new QuoteCQ_UpdatePricing(); 
//		QUP.pricingmain();
//		waitforseconds(5);
//		
//		QuoteCQ_AutoSelect QAS = new QuoteCQ_AutoSelect(); 		
//		availblePricing();
//		waitforseconds(8);
//		QAS.submit();
		safeJavaScriptClick(CheckXpath("MATERIAL_TAB"));
		log.info("~>> Redirected to Material Tab ");
		waitforseconds(8);
	}

	@Test(priority = 3, description = "#.  GET VALUES FROM MATERIAL COSTING SCREEN")
	public void getValue() throws Exception {
		headerFormat("#. CASE : GET VALUES FROM MATERIAL COSTING SCREEN");
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 3000", scrollbar);
		log.info("~>> Scrolling done");
		waitforseconds(8);
		asmPrice = getValue("Assembly Price");
		unitPrice = getValue("Unit Price");
		totalQty = getValue("Total Qty");
		excessAmt = getValue("Excess Amt");
		orderCost = getValue("Order Cost");
	}

	@Test(priority = 4, description = "#.  VERIFY CALCULATION ON SUMMARY SCREEN FOR MATERIAL COSTING")
	public void verifyCalcMaterial() throws Exception {
		headerFormat("#. CASE : CHECK SUMMARY TOTAL FOR MATERIAL");
		safeJavaScriptClick(CheckXpath("SUBMIT_TAB"));
		log.info("~>> Redirected to Summary Tab");
		waitforseconds(10);
		reqQty = Double.parseDouble(
				driver.findElement(By.xpath(".//input[@ng-model='col.displayName']")).getAttribute("value"));
		System.out.println("~>> Requested Qty :: " + reqQty);
		System.out.println("");
		verifyCalc(1, Material_Cost);
		verifyCalc(2, Material_Lead_Cost);
		verifyCalc(3, Material_Attr_Cost);
		Material_Sub_Total = verifyOtherCost(1, "Material");
		verifyMaterialMarkup();
	}

	public void availblePricing() throws Exception {
		waitforseconds(8);
		WebElement scrollbar = driver.findElement(
				By.xpath("//div[@ng-style='colContainer.getViewportStyle()' and @style='overflow: scroll;']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft += 3000", scrollbar);
		boolean pricing = false;
		try {
			pricing = driver.findElement(By.xpath(".//a//i[contains(@class,'fa-square pricing')]")).isDisplayed();
		} catch (Exception e) {

		}
		if (pricing) {
			driver.findElement(By.xpath(".//a//i[contains(@class,'fa-square pricing')]")).click();
			waitforseconds(8);
			driver.findElement(By.xpath(".//*[@id='SupplierTypesFlag']//button")).click();
			driver.findElement(By.xpath(".//*[@id='SupplierTypesFlag']/div/div/ul[1]/li[4]/a")).click();
			waitforseconds(5);
			driver.findElement(By.xpath("(.//div[@class='ui-grid-cell-contents ng-scope'])[3]//span")).click();
			waitforseconds(3);
			safeJavaScriptClick(CheckXpath("CQPS_CLOSE"));
			alertacceptpopup();
			System.out.println("~>> RESULT :: PRICING SELECTION DONE");
			log.info("~>> PRICING SELECTION DONE");
		}
	}

	// Get Value from Material Costing screen
	public double getValue(String strItem) {
		waitforseconds(5);
		headerFormat("#. CASE : GET " + strItem);
		WebElement Pricefilter = driver
				.findElement(By.xpath("(.//i[@class='ui-grid-icon-angle-down' and @aria-hidden='true'])[last()]"));
		String id = Pricefilter.findElement(By.xpath(".//parent::div")).getAttribute("id").replaceAll("-menu-button",
				"");
		String id1 = id.split("-")[1].concat("-").concat(id.split("-")[2]);
		Pricefilter.click();
		waitforseconds(10);
		driver.findElement(By.xpath(".//button[contains(text(),'" + strItem + "')]")).click();
		waitforseconds(2);
		double sum = 0;
		List<WebElement> lstItem = driver.findElements(By.xpath(".//div[contains(@id,'" + id1 + "')]//div//a"));
		System.out.println("~>> Total Line Items :: " + lstItem.size());
		for (int i = 0; i < lstItem.size(); i++) {
			log.info(lstItem.get(i).getAttribute("innerText").replace("$", ""));
			sum += Double.parseDouble(
					lstItem.get(i).getAttribute("innerText").replace("$", "").replace("kr", "").replace(",", ""));
		}
		System.out.println("~>> " + strItem + " Total :: " + sum);
		log.info("~>> " + strItem + " Total :: " + sum);
		return sum;
	}

	// Calculated Material Cost, leadcost, Attr Cost and verified it.
	public void verifyCalc(int type, Double expResult) {
		if (type == 1) {
			System.out.println("#. CALCULATING MATERIAL COST");
			summaryScroll("Material Sub Total");
			getResult("Material Cost", "MATERIAL_SUBTOTAL_HEADER", "Material", "Cost", 1);
			Material_Cost = (qtyPer * unitPrice);
			System.out.println("~>> Expected Material Cost - " + Material_Cost);
			log.info("~>> Expected Material Cost - " + Material_Cost);
			expResult = Material_Cost;
		}

		if (type == 2) {
			System.out.println("#. CASE : CALCULATING MATERIAL LEAD COST");
			getResult("Material Lead Cost", "MATERIAL_SUBTOTAL_HEADER", "Material", "Lead", 1);
			Material_Lead_Cost = (leadQty * unitPrice) / reqQty;
			System.out.println("~>> Expected Material Lead Cost - " + Material_Lead_Cost);
			log.info("~>> Expected Material Lead Cost - " + Material_Lead_Cost);
			expResult = Material_Lead_Cost;
		}

		if (type == 3) {
			System.out.println("#. CASE : CALCULATING MATERIAL ATTRITION COST");
			getResult("Material Attrition Cost", "MATERIAL_SUBTOTAL_HEADER", "Material", "Attrition", 1);
			Material_Attr_Cost = (((reqQty * qtyPer * attRate) / 100) * unitPrice) / reqQty;
			System.out.println("~>> Expected Material Attrition Cost -" + Material_Attr_Cost);
			log.info("~>> Expected Material Attrition Cost - " + Material_Attr_Cost);
			expResult = Material_Attr_Cost;
		}
		if (type == 4) {
			System.out.println("#. CASE : CALCULATING MATERIAL AMORTIZE EXCESS");
			getResult("Amortize Excess", "MATERIAL_SUBTOTAL_HEADER", "Amortize", "Excess", 1);
			Material_Amortize_Access = (excessQty * unitPrice) / reqQty;
			System.out.println("~>> Expected Material Attrition Cost -" + Material_Attr_Cost);
			log.info("~>> Expected Material Attrition Cost-" + Material_Attr_Cost);
			expResult = Material_Amortize_Access;
		}

		verifyResult(expResult, ActResult);
		System.out.println("");
	}

	public void verifyTotal() {
		getResult("Material Sub  Total", "MATERIAL_EXCESS_TOTAL", "Material", "Sub", 1);
		getResult("Material Markup Total", "MATERIAL_EXCESS_TOTAL", "Material", "Markup", 1);
	}

	//
	public void getResult(String name, String scroll, String strFirst, String strLast, int i) {
		waitforseconds(10);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", CheckXpath(scroll));
		waitforseconds(5);
		WebElement ele = driver.findElement(By.xpath("(.//span[contains(text(),'" + strFirst
				+ "') and contains(text(),'" + strLast + "')])[" + i + "]//preceding::div[2]"));
		String id = ele.getAttribute("id").replace("-cell", "");
		String id1 = id.split("-")[0].concat("-").concat(id.split("-")[1]).concat("-").concat(id.split("-")[2]);
		ActResult = Double.parseDouble(
				driver.findElement(By.xpath(".//div[contains(@id,'" + id1 + "')]//span[@class='ng-binding']")).getText()
						.replaceAll("\\s", ""));
		System.out.println("~>> Actual " + name + " - " + String.format("%.6f", ActResult));
		log.info("~>> Actual " + name + " - " + String.format("%.6f", ActResult));
	}

	// Calculate and verify Misc. Material Cost
	public double verifyOtherCost(int type, String strName) {
		System.out.println("#. CASE : CALCULATING SUB TOTAL");
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
		String matSubId = driver
				.findElement(By.xpath("(.//span[contains(text(),'" + strName
						+ "') and contains(text(),'Sub') and contains(text(),'Total')])[last()]//preceding::div[2]"))
				.getAttribute("id");
		int lastrange = Integer.parseInt(matSubId.split("-")[1]);
		Double sum = (double) 0;
		for (int i = firstrange + 2; i <= lastrange; i++) {
			sum += Double.parseDouble(driver.findElement(
					By.xpath(".//a[@class='AddCostCursor ng-binding ng-scope']//following::span[@class='ng-binding']["
							+ i + "]"))
					.getText());
		}
		if (type == 1) {
			expSubTotal = Double.sum(sum, Material_Cost) + Double.sum(Material_Lead_Cost, Material_Attr_Cost);
			System.out.println("~>> Expected " + strName + " SubTotal :: " + expSubTotal);
		}
		if (type == 2) {
			expSubTotal = Double.sum(sum, Smoke_Summary_Labor.labor$)
					+ Double.sum(Smoke_Summary_Labor.MOH$, Smoke_Summary_Labor.setup$);
		}
		int sub = lastrange + 1;
		double actSubTotal = Double.parseDouble(driver.findElement(By.xpath(
				".//a[@class='AddCostCursor ng-binding ng-scope']//following::span[@class='ng-binding'][" + sub + "]"))
				.getText());
		System.out.println("~>> Actual " + strName + " SubTotal :: " + actSubTotal);

		verifyResult(expSubTotal, actSubTotal);
		System.out.println("");
		return expSubTotal;
	}

	// Calculate and Verify Material Markup
	public void verifyMaterialMarkup() {
		summaryScroll("Material Total");
		System.out.println("#. CASE : CALCULATING MATERIAL MARKUP$");
		String markupid = driver.findElement(By.xpath(
				"(.//span[contains(text(),'Material') and contains(text(),'Markup')])[1]//preceding::div[@role='gridcell'][1]"))
				.getAttribute("id");
		String markupid1 = markupid.split("-")[0].concat("-").concat(markupid.split("-")[1]);
		Double Markup = Double.parseDouble(
				driver.findElement(By.xpath("(.//div[contains(@id,'" + markupid1 + "')]//span)[last()]")).getText());
		System.out.println("~>> Material Markup - " + Markup);

		String markupidPer = driver.findElement(By.xpath(
				"(.//span[contains(text(),'Material') and contains(text(),'Markup')])[2]//preceding::div[@role='gridcell'][1]"))
				.getAttribute("id");
		String markupidPer1 = markupidPer.split("-")[0].concat("-").concat(markupidPer.split("-")[1]);
		Double ActMarkupPer = Double.parseDouble(
				driver.findElement(By.xpath("(.//div[contains(@id,'" + markupidPer1 + "')]//span)[last()]")).getText());
		System.out.println("~>> Actual Material Markup :: " + ActMarkupPer);
		Material_Markup = Material_Sub_Total * (Markup / 100);
		System.out.println("~>> Expected Material MarkUp :: " + Material_Markup);
		verifyResult(Material_Markup, ActMarkupPer);

		System.out.println("");
		System.out.println("#. CASE : CALCULATING MATERIAL MARGIN");
		String markupidmar = driver.findElement(By.xpath(
				"(.//span[contains(text(),'Material') and contains(text(),'Margin')])[1]//preceding::div[@role='gridcell'][1]"))
				.getAttribute("id");
		String markupidmar1 = markupidmar.split("-")[0].concat("-").concat(markupidmar.split("-")[1]);
		Double ActMarkupmar = Double.parseDouble(
				driver.findElement(By.xpath("(.//div[contains(@id,'" + markupidmar1 + "')]//span)[last()]")).getText());
		System.out.println("~>> Actual Material Margin :: " + ActMarkupmar);
		Material_Total = Double.sum(Material_Sub_Total, Material_Markup);
		Material_Margin = (Material_Markup * 100) / Material_Total;
		System.out.println("~>> Expected Material Margin :: " + Material_Margin);
		verifyResult(Material_Margin, ActMarkupmar);

		System.out.println("");
		System.out.println("#. CASE : CALCULATING MATERIAL TOTAL");
		System.out.println("~>> Expected Material Total :: " + Material_Total);
		int materialTotal = Integer.parseInt(markupidmar.split("-")[1]) + 1;
		String materialTotalid = markupidmar.split("-")[0].concat("-").concat(Integer.toString(materialTotal));
		Double ActMaterialTotal = Double.parseDouble(driver
				.findElement(By.xpath("(.//div[contains(@id,'" + materialTotalid + "')]//span)[last()]")).getText());
		System.out.println("~>> Actual Material Total :: " + ActMaterialTotal);
		verifyResult(Material_Total, ActMaterialTotal);
	}
}