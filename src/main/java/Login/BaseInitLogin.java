package Login;

import java.io.IOException;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import parentclass.BaseInit;
import utility.UtilityMethods;

public class BaseInitLogin extends BaseInit {

	@BeforeSuite
	  public void checkExecutionModeTestSuite() throws IOException{			
		  startup();
			//Code to check execution mode of Test Suite		  
		  if(!UtilityMethods.checkExecutionModeTestSuite(Test_suite, "login", "TestSuite"))
		  {
				throw new SkipException("Execution mode of the test suite is set to NO");
			}	
}
}
