package ShopCQ;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import QuoteCQ.BaseInitquoteCQ;
import utility.UtilityMethods;

public class ShopCQ_SentLineitem extends BaseInitshopCQ {
	String quoteId;
	String timenow;

	@BeforeTest
	public void checkTestCase() {
		if (!UtilityMethods.checkExecutionModeTestCase(Test_case, this.getClass().getSimpleName(), "ShopCQ")) {
			throw new SkipException("Execution mode of the test case is set to NO");
		}
	}

	@Test(description = "#. HAPPY PATH :: SCQ - SENT LINE ITEM FROM QUOTECQ TO SHOPCQ")
	public void exportPO() throws Exception {
		log.info("----------------------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------------------");
		log.info("CASE : SENT LINE ITEM FROM QUOTECQ TO SHOPCQ");
		System.out.println("CASE : SENT LINE ITEM FROM QUOTECQ TO SHOPCQ");
		waitforseconds(5);
		BaseInitquoteCQ.RFQ_list();
		safeJavaScriptClick(CheckXpath("RFQ_COMPLETED"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		safeJavaScriptClick(CheckXpath("QUOTE_MENU"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		purchase();
	}

	public void purchase() throws Exception {
		waitforseconds(5);
		boolean purchase = false;
		try {
			purchase = CheckXpath("QUOTE_PURCHASE").isDisplayed();
		} catch (Exception e) {

		}
		if (purchase) {
			safeJavaScriptClick(CheckXpath("QUOTE_PURCHASE"));
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
			getTag();
		} else {
			log.info("~>> NO RECORD FOR PURCHASE");
			System.out.println("~>> NO RECORD FOR PURCHASE");
		}

	}

	public void getTag() throws Exception {
		boolean elequoteid = false;
		try {
			elequoteid = driver.findElement(By.xpath(".//*[@id='lnkQuoteId_0']")).isDisplayed();
		} catch (Exception e) {

		}
		if (elequoteid) {
			waitforseconds(5);
			quoteId = driver.findElement(By.xpath(".//*[@id='lnkQuoteId_0']")).getText();
			log.info("~>> Purchased Quote Id : " + quoteId);
			timenow = String.valueOf(System.currentTimeMillis());
			driver.findElement(By.xpath(".//*[@id='Tags']//input")).sendKeys(timenow);
			log.info("~>> Tag Added :: " + timenow);
			
			waitforseconds(5);
			safeJavaScriptClick(CheckXpath("PURCHASE_BUTTON"));
			alertacceptpopup();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			verification();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} else {
			log.info("~>> RESULT ::  No line item to sent to ShopCQ");
			System.out.println("~>> RESULT ::  No line item to sent to ShopCQ");
		}
	}

	public void verification() throws Exception {
		log.info("~>> Verifying lineitem from ShopCQ");
		log.info("~>> Quote Id : " + quoteId);
		System.out.println("~>> Quote Id : " + quoteId);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		demandList();
		waitforseconds(5);
		CheckXpath("DL_TAGS_FILTER").clear();
		CheckXpath("DL_TAGS_FILTER").sendKeys(timenow);
		waitforseconds(15);
		String id2 = CheckXpath("DL_TAGS_LABEL").getAttribute("id").replaceAll("-header-text", "");
		String id3 = id2.split("-")[1].concat("-").concat(id2.split("-")[2]);
		List<WebElement> lstresult = driver.findElements(
				By.xpath(".//div[contains(@id,'" + id3 + "')]//div[@class='ui-grid-cell-contents ng-binding']"));
		for (WebElement result : lstresult) {
			if (result.getAttribute("innerText").contains(timenow)) {
				log.info("~>> RESULT :: LINE ITEM SENT SUCCESSFULLY FROM QUOTECQ TO SHOPCQ");
				System.out.println("~>>RESULT :: LINE ITEM SENT SUCCESSFULLY FROM QUOTECQ TO SHOPCQ");

			} else {
				log.info("~>> RESULT :: LINE ITEM NOT SENT SUCCESSFULLY FROM QUOTECQ TO SHOPCQ");
				System.out.println("~>>RESULT :: LINE ITEM NOT SENT SUCCESSFULLY FROM QUOTECQ TO SHOPCQ");
			}
		}
	}
}