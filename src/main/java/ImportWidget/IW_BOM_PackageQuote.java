package ImportWidget;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class IW_BOM_PackageQuote extends BaseInitImportWidget {

	HashSet<String> hashmpn;

	// TASK : 53160
	@Test(description = "#. VERIFY THE PACKAGE BOM SHOULD BE IMPORTED VIA BOM WIDGET")
	public void sameQtyMain() throws Exception {
		headerFormat("#. CASE : VERIFY THE PACKAGE BOM SHOULD BE IMPORTED VIA BOM WIDGET");
		IW_PKGRFQ PKG = new IW_PKGRFQ();
		PKG.create(1);
		quoteChecker();
		importfile("BOM", "BOM_PKG.xlsx");
		mappingScreen();
		IW_BOM_MultiLevel.getTotalsublevelBOM();
		parentMPN();
		IW_ImportValidation.validatePopup();
		verifyPkgBOM();
		validateimport();
		IW_BOM_MultiLevel.verifyMultilevelBOM();
		// ImportVerification.clear();
	}

	public void parentMPN() {
		String mpnColumn = CheckXpath("IW_SHEET_MPN_LABEL").getAttribute("data-x");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
				CheckXpath("IW_SHEET_MPN_LABEL"));
		waitforseconds(5);
		List<WebElement> lstmpn = driver
				.findElements(By.xpath(".//*[@id='validationSpreadsheet']//table//tr//td[@data-x='" + mpnColumn
						+ "' and contains(@name,'col')]"));
		log.info("~>> SIZE :: " + lstmpn.size());
		String[] mpnarr = new String[lstmpn.size()];
		for (int k = 0; k < lstmpn.size(); k++) {
			mpnarr[k] = lstmpn.get(k).getText();
			log.info("~>> Existing MPN :: " + mpnarr[k]);
		}

		hashmpn = new HashSet<String>(Arrays.asList(mpnarr));
		log.info("~>> ACTUAL  MPN ::  " + hashmpn.size());
		hashmpn.retainAll(IW_BOM_MultiLevel.hashBomLevel);
		log.info("~>> ACTUAL  COMMON MPN :: " + hashmpn);
	}

	public void verifyPkgBOM() {
		waitforseconds(15);
		List<WebElement> lstsubbom = driver.findElements(By.xpath(".//i[@title='SubBom']"));
		if (IW_BOM_MultiLevel.hashBomLevel.size() == (lstsubbom.size())) {
			log.info("~>> RESULT ::  SUB LEVEL BOM COUNT IS CORRECT");
		} else {
			log.info("~>> RESULT :: SUB LEVEL BOM COUNT IS WRONG");
		}
		List<WebElement> lstqty = driver.findElements(By.xpath(".//div[contains(@id,'dvQty')]"));
		for (WebElement eleqty : lstqty) {
			if (eleqty.getText().equals("Multiple")) {
				log.info("~>> RESULT ::  QTY PER IS CORRECT FOR PACKAGE BOM");
				System.out.println("~>> RESULT ::  QTY PER IS CORRECT FOR PACKAGE BOM");
				eleqty.click();
				waitforseconds(3);
				List<WebElement> lastActMPN = driver.findElements(By.xpath(".//*[@id='msform']//tr//td[3]//span"));
				String[] actmpnarr = new String[lastActMPN.size()];
				for (int k = 0; k < lastActMPN.size(); k++) {
					actmpnarr[k] = lastActMPN.get(k).getText();
					log.info("~>> Existing MPN :: " + actmpnarr[k]);
				}

				HashSet<String> expthashMPN = new HashSet<String>(Arrays.asList(actmpnarr));
				log.info("ACTUAL MPN FOR  :: " + expthashMPN);
				if (hashmpn.equals(expthashMPN)) {
					log.info("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE CORRECT");
					System.out.println("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE CORRECT");
				} else {
					log.info("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE WRONG");
					System.out.println("~>> RESULT :: EXPECTED AND ACTUAL MPN ARE WRONG");
				}
				driver.findElement(By.xpath(".//*[@id='msform']/button")).click();
				waitforseconds(3);
			} else {
				log.info("~>> RESULT ::  QTY PER IS WRONG FOR PACKAGE BOM");
				System.out.println("~>> RESULT ::  QTY PER IS WRONG FOR PACKAGE BOM");
			}
		}
	}
}
