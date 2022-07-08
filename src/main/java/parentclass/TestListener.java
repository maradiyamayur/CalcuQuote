package parentclass;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class TestListener implements ITestListener, IExecutionListener {
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	private static ExtentReports extent = ExtentManager.createInstance();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public synchronized void onStart(ITestContext context) {

		System.out.println("[Extent Reports  Test Suite started!] :: " + context.getName());
	}

	public synchronized void onFinish(ITestContext context) {
		System.out.println("[Extent Reports  Test Suite finished!] :: " + context.getName());
		if (extent != null) {
			extent.flush();
		}
	}

	public synchronized void onTestStart(ITestResult result) {

		System.out.println("[" + (result.getMethod().getMethodName() + " started! ]"));
		// ExtentTest extentTest = extent
		// .createTest(result.getTestClass().getName() + " :: " +
		// result.getMethod().getDescription());
		ExtentTest extentTest1 = extent.createTest(result.getMethod().getDescription());
		test.set(extentTest1);
		// test.set(extentTest);
	}

	public synchronized void onTestSuccess(ITestResult result) {

		String logText = "<b> Test Method - " + result.getMethod().getMethodName() + "  is Succesful </b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		test.get().log(Status.PASS, m);
	}

	public synchronized void onTestFailure(ITestResult result) {

		String logText = "<b> Test Method - " + result.getMethod().getMethodName() + " is Failed</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
		test.get().log(Status.FAIL, m);
		try {
			ExtentManager.getScreenshot(result.getName());
			test.get().addScreenCaptureFromPath(ExtentManager.getScreenshot(result.getName()));
			test.get().log(Status.FAIL, m + " FAIL with error " + result.getThrowable());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void onTestSkipped(ITestResult result) {

		String logText = "<b> Test Method - " + result.getMethod().getMethodName() + " is Skipped</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.ORANGE);
		test.get().log(Status.SKIP, m);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
	}

	public void onExecutionFinish() {
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("~>> EXECUTION FINISHED");
		try {
			Multipart multipart = new MimeMultipart();
			SendMail.execute(multipart, System.getProperty("user.dir") + "//test-output//ExtentReport.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onExecutionStart() {
		// TODO Auto-generated method stub

	}
}