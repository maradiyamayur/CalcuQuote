package parentclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import utility.UtilityMethods;
import utility.XLSDatatable_Connectivity;

public class BaseInit {

	public static WebDriver driver = null;
	public static Logger log = null;
	public static Properties prop = null;
	public static XLSDatatable_Connectivity Test_case = null;
	public static XLSDatatable_Connectivity Test_suite = null;
	public static XSSFWorkbook workbook;
	protected static XSSFSheet Sheet;
	protected static XSSFCell cell;
	protected static XSSFRow Row; 
	public static File src;
	public static Sheet Sheetname;

	public void startup() throws IOException {

		if (driver == null) {
			log = Logger.getLogger("devpinoyLogger");
			log.info("~>> Properties file is loading now....");
			prop = new Properties();
			FileInputStream f1 = new FileInputStream(
					System.getProperty("user.dir") + "//src/main//java//Properties//General_Xpath.properties");
			FileInputStream f2 = new FileInputStream(
					System.getProperty("user.dir") + "//src/main//java//Properties//BOM_Xpath.properties");
			FileInputStream f3 = new FileInputStream(
					System.getProperty("user.dir") + "//src/main//java//Properties//Labor_Xpath.properties");
			FileInputStream f4 = new FileInputStream(
					System.getProperty("user.dir") + "//src///main//java//Properties//Material_Xpath.properties");
			FileInputStream f5 = new FileInputStream(
					System.getProperty("user.dir") + "//src///main//java//Properties//BidCQ_Xpath.properties");
			FileInputStream f6 = new FileInputStream(
					System.getProperty("user.dir") + "//src///main//java//Properties//ShopCQ_Xpath.properties");
			FileInputStream f7 = new FileInputStream(
					System.getProperty("user.dir") + "//src///main//java//Properties//Credential.properties");
			prop.load(f1);
			prop.load(f2);
			prop.load(f3);
			prop.load(f4);
			prop.load(f5);
			prop.load(f6);
			prop.load(f7);
			log.info("~>> Properties file loaded successfully....");
			log.info("~>> Browser is launching now...");

			if (prop.getProperty("browser").equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();			
				/*
				 * System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
				 * "\\src\\main\\java\\Resources\\geckodriver.exe");
				 */		 
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,
						System.getProperty("user.dir") + "\\Logs\\myLogs.txt");
				driver = new FirefoxDriver();
			} else if (prop.getProperty("browser").equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();		
				/*
				 * System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
				 * + "//src//main//java//Resources//chromedriver.exe");
				 */
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,
						System.getProperty("user.dir") + "\\Logs\\myLogs.txt");
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
				ChromeOptions options = new ChromeOptions();				
				driver = new ChromeDriver(options);
			}
			log.info("~>> Browser launched successfully.....");
			driver.manage().window().maximize();		
			Test_case = new XLSDatatable_Connectivity(
					System.getProperty("user.dir") + "//src//main//java//XLSFiles//TestCase.xlsx");
			Test_suite = new XLSDatatable_Connectivity(
					System.getProperty("user.dir") + "//src/main//java///XLSFiles//TestSuite.xlsx");
		}
	}

//	public static String getDecodepassword(int row, int col) throws IOException {
//		FileInputStream fs = new FileInputStream(
//				System.getProperty("user.dir") + "//src//main//java//Resources//myData.xlsx");
//		XSSFWorkbook workbook = new XSSFWorkbook(fs);
//		XSSFSheet sheet = workbook.getSheetAt(0);
//		String password = sheet.getRow(row).getCell(col).toString();
//		return new String(password);
//	}

	public static WebElement CheckXpath(String key) {
		try {
			return driver.findElement(By.xpath(prop.getProperty(key)));
		} catch (Throwable t) {
			t.getMessage();
			// System.out.println("XPATH:Link not present in properties file");
			return null;
		}
	}

	public static List<WebElement> CheckXpathlist(String key) {
		try {
			return driver.findElements(By.xpath(prop.getProperty(key)));
		} catch (Throwable t) {
			t.getMessage();
			// System.out.println("XPATH:Link not present in properties file");
			return null;
		}
	}

	public static WebElement CheckCss(String key) {
		try {
			return driver.findElement(By.cssSelector(prop.getProperty(key)));
		} catch (Throwable t) {
			// System.out.println("CSS:Link not present in properties file");
			t.getMessage();
			return null;
		}
	}

	public static WebElement CheckLink(String key) {
		try {
			return driver.findElement(By.linkText(prop.getProperty(key)));
		} catch (Throwable t) {
			t.getMessage();
			// System.out.println("LINK TEXT:Link not present in properties
			// file");
			return null;
		}
	}

	public static WebElement CheckID(String key) {
		try {
			return driver.findElement(By.id(prop.getProperty(key)));
		} catch (Throwable t) {
			t.getMessage();
			// System.out.println("ID:Link not present in properties file");
			return null;
		}
	}

	// get current instance
	public String[] getHost() throws MalformedURLException {
		String[] getHost = new String[2];
		URL url = new URL(UtilityMethods.url);
		getHost[0] = url.getHost();
		getHost[1] = url.getFile();
		log.info("~>> Current Host :: " + getHost[0]);
		log.info("~>> Sub Instance :: " + getHost[1]);
		return getHost;
	}

	public static void waitforseconds(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException interruptedException) {

		}
	}

	public static void safeJavaScriptClick(WebElement element) throws Exception {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else {
				
			}
		} catch (StaleElementReferenceException e) {
			log.info("!WARNING: Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			log.info("!WARNING: Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			log.info("!WARNING: Unable to click on element " +e.getStackTrace());
		}
	}

	public void alertacceptpopup() throws InterruptedException {
		waitforseconds(3);
		boolean popup = false;
		try {
			popup = driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).isDisplayed();
		} catch (Exception e) {

		}
		if (popup) {
			driver.findElement(By.xpath(".//button[@class='swal2-confirm swal2-styled']")).click();
			log.info("~>> Alert accepted");
		} else {
			log.info("~>> No alert");
		}
	}

	public void headerFormat(String title) {
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		log.info(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		log.info(title);
		System.out.println(title);
	}

	public void footerFormat() {
		log.info(
				"......................................................................................................");
		System.out.println(
				"......................................................................................................");
	}

	
}