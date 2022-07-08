package ShopCQ;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import BOM.BOM_Properties;
import utility.UtilityMethods;

public class ShopCQ_AddDemandline extends BaseInitshopCQ {

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Parameters({ "correctusername", "correctpassword", })
	@Test(description = "#. HAPPY PATH :: SCQ - ADD DEMAND LINE")
	public void demandLine() throws Exception {
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : ADD DEMAND LINE");
		System.out.println("CASE : ADD DEMAND LINE");
		waitforseconds(5);
		safeJavaScriptClick(CheckXpath("SHOP_CQ"));
		waitforseconds(5);
		String demandList = driver.getCurrentUrl().replace("shopcqhome", "demandlist");
		driver.get(demandList);
		waitforseconds(10);
		// driver.findElement(By.xpath(".//div[@class='center
		// ng-scope']//i[@href='#/shopcq/demandlist']")).click();
		// safeJavaScriptClick(CheckXpath("DEMAND_LIST"));
		log.info("~>> Clicked on Demand list");
		driver.navigate().refresh();
		waitforseconds(10);
		addDetails("12", "Mouser");
		addDetails("18", "Mouser");
	
		log.info("----------------------------------------------------------------------------");
	}

	public void addDetails(String targetpr, String supplier) throws Exception {
		waitforseconds(10);
		safeJavaScriptClick(CheckXpath("ADD_DEMAND_LINE"));
		log.info("~>> Clicked on Add Demand line");
		waitforseconds(5);
		CheckXpath("DL_TARGET_PRICE").clear();
		CheckXpath("DL_TARGET_PRICE").sendKeys(targetpr);
		log.info("~>> Target Price Added :: " + targetpr);
		mfgrDetails();
		otherDetails();
		supplierDetails(supplier);
		safeJavaScriptClick(CheckXpath("DL_SUBMIT"));
		log.info("~>> RESULT :: DEMAND LINE ADDED SUCCESSFULLY");
		System.out.println("~>> RESULT :: DEMAND LINE ADDED SUCCESSFULLY");
		waitforseconds(10);
	}

	public void mfgrDetails() throws InterruptedException {
		BOM_Properties BP = new BOM_Properties();
		BP.inlineInput("DL_MFGR_LABEL", "Mouser", "-0-");
		log.info("~>> Mfgr Added");
		waitforseconds(3);
		WebElement mpn = driver.findElement(By.xpath(".//*[@id='dvMPN_0']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(mpn).doubleClick().perform();
		actions.sendKeys("5014").perform();
		actions.click();
		log.info("~>> MPN number added");
		BP.inlineInput("DL_REV_LABEL", "1", "-0-");
	}

	public void otherDetails() throws InterruptedException {
		driver.findElement(By.xpath(".//*[@id='Tags']//input")).sendKeys("Auto Add Demandline");
		waitforseconds(3);
		CheckXpath("DL_DESIGNATOR").sendKeys("Auto Designator - 001");
		log.info("~>> Designator Added");
		CheckXpath("DL_DESCRIPTION").sendKeys("Auto Test Desription");
		log.info("~>> Description Added");
		CheckXpath("DL_NOTES").sendKeys("Auto Test Notes");
		log.info("~>> Notes Added");
		waitforseconds(5);
	}

	public void supplierDetails(String supplier) throws Exception {
		safeJavaScriptClick(CheckXpath("DL_SUPPLIER_DETAILS"));
		log.info("~>> Clicked on Supplier Details");
		waitforseconds(12);
		CheckXpath("DL_ADD_SUPPLIER").clear();
		CheckXpath("DL_ADD_SUPPLIER").sendKeys(supplier);
		waitforseconds(5);
		WebElement link = driver.findElement(By.linkText(supplier));
		safeJavaScriptClick(link);
		log.info("~>> Supplier Added :: Mouser");
		waitforseconds(4);
		CheckXpath("DL_UNIT_PRICE").clear();
		CheckXpath("DL_UNIT_PRICE").sendKeys("10");
		log.info("~>> Unit Price Added");
		CheckXpath("DL_SUPPLY_QTY").clear();
		CheckXpath("DL_SUPPLY_QTY").sendKeys("20");
		log.info("~>> Supply Qty Added");
		CheckXpath("DL_DOCK_DATE").click();
		CheckXpath("DL_DOCK_DATE_SELECT").click();
		log.info("~>> Dock Date Selected");
	}
}
