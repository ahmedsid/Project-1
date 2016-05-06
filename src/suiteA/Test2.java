package suiteA;

import org.testng.Assert;
import org.testng.annotations.Test;

import Utility.ErrorUtil;

public class Test2 {
// Handling multiple failure scenarios using ErrorUtil funtions, make note of cutomListener file
	@Test
	public void test2(){
		try{
		Assert.assertEquals("A", "B");
		
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
		}
		
		try{
			Assert.assertEquals("C", "D");
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
		}
	}
	
	
}
