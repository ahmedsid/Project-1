package listener;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;

import Utility.Constants;
import Utility.ErrorUtil;
import Utility.Xls_Reader;


public class CustomListener extends TestListenerAdapter implements IInvokedMethodListener,ISuiteListener{
	public static Hashtable<String,String> resultTable;
	public static ArrayList<String> keys ;
	public static String resultFolderName;
	
	public void onTestFailure(ITestResult tr){
		//report(tr.getName(), tr.getThrowable().getMessage());
		List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
		String errMsg="";
		for(int i=0;i<verificationFailures.size();i++){
			errMsg=errMsg+"["+verificationFailures.get(i).getMessage()+"]-";
		}
		report(tr.getName(), errMsg);
		
	}
	
	public void onTestSkipped(ITestResult tr) {
		report(tr.getName(), tr.getThrowable().getMessage());

	}
	
	public void onTestSuccess(ITestResult tr){
		report(tr.getName(), "PASS");
	}
	
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		Reporter.setCurrentTestResult(result);

		if (method.isTestMethod()) {
			List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
			//if there are verification failures...
			if (verificationFailures.size() != 0) {
				//set the test to failed
				result.setStatus(ITestResult.FAILURE);
				
				//if there is an assertion failure add it to verificationFailures
				if (result.getThrowable() != null) {
					verificationFailures.add(result.getThrowable());
				}
 
				int size = verificationFailures.size();
				//if there's only one failure just set that
				if (size == 1) {
					result.setThrowable(verificationFailures.get(0));
				} else {
					//create a failure message with all failures and stack traces (except last failure)
					StringBuffer failureMessage = new StringBuffer("Multiple failures (").append(size).append("):nn");
					for (int i = 0; i < size-1; i++) {
						failureMessage.append("Failure ").append(i+1).append(" of ").append(size).append(":n");
						Throwable t = verificationFailures.get(i);
						String fullStackTrace = Utils.stackTrace(t, false)[1];
						failureMessage.append(fullStackTrace).append("nn");
					}
 
					//final failure
					Throwable last = verificationFailures.get(size-1);
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":n");
					failureMessage.append(last.toString());
 
					//set merged throwable
					Throwable merged = new Throwable(failureMessage.toString());
					merged.setStackTrace(last.getStackTrace());
 
					result.setThrowable(merged);
					
				}
			}
		
		}

				
	}
 
	public void beforeInvocation(IInvokedMethod arg0, ITestResult test) {
		
	}

	
	@Override
	public void onStart(ISuite suite) {
		System.out.println("Starting with "+suite.getName());
		
		keys = new ArrayList<String>();
		resultTable = new Hashtable<String,String>();
		
		if(resultFolderName==null){
			Date d = new Date();
			resultFolderName=d.toString().replace(":", "_");
			File f = new File(System.getProperty("user.dir")+"//Target//Report//"+resultFolderName);
			f.mkdir();
			File srcFile = new File(System.getProperty("user.dir")+"//Target//Report//ReportTemplate.xlsx");
			File destFile = new File(System.getProperty("user.dir")+"//Target//Report//"+resultFolderName+"//Report.xlsx");
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	@Override
	public void onFinish(ISuite suite) {
		System.out.println("Ending with "+suite.getName());
		System.out.println(resultTable);
		System.out.println(keys);
		
		//writing result in xls report
		if(!suite.getName().equals(Constants.ROOT_SUITE)){	
		Xls_Reader xls = new Xls_Reader(System.getProperty("user.dir")+"//Target//Report//"+resultFolderName+"//Report.xlsx");
		//adding sheets
		xls.addSheet(suite.getName());
		//adding columns
		xls.setCellData(suite.getName(), 0, 1, "TestCase");
		xls.setCellData(suite.getName(), 1, 1, "Result");
		
		//adding result
	for(int i=0;i<keys.size();i++){
		String key = keys.get(i);
		String result = resultTable.get(key);
		xls.setCellData(suite.getName(), 0, i+2, key);
		xls.setCellData(suite.getName(), 1, i+2, result);
	}
		
		}
		resultTable=null;
		keys = null;
		
	}
	public void report(String name, String result){
		int iteration_number =1;
		while(resultTable.containsKey(name+" iteration "+iteration_number)){
			iteration_number++;
		}
		resultTable.put(name+" iteration "+iteration_number, result);
		keys.add(name+" iteration "+iteration_number);
	}

}

	