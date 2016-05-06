package suiteA;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Utility.ErrorUtil;

public class Test1 {

	@Test(dataProvider="getData")
	public void test1(String a, String b){
		try{
			Assert.assertEquals("U1", a);
		}catch(Throwable t){
			
		ErrorUtil.addVerificationFailure(t);
		}
	}
		

	
	@DataProvider
	public Object[][] getData(){
		Object data[][] = new Object[2][2];
		
		data[0][0] ="U1";
		data[0][1] ="P1";
		
		data[1][0] ="U2";
		data[1][1] ="P2";
		return data;
		
	}
}
