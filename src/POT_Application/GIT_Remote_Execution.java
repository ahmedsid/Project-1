package POT_Application;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class GIT_Remote_Execution {
	
	@Test
	public void RemoteExecution() throws InterruptedException{
		
		System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("http://bincdv-a10197.corp.biogen.com:8080/InfoViewApp/logon.jsp");
		driver.switchTo().frame(0);
		driver.findElement(By.xpath("//*[@id='usernameTextEdit']")).clear();
		driver.findElement(By.xpath("//*[@id='usernameTextEdit']")).sendKeys("asiddiqu");
		driver.findElement(By.xpath("//*[@id='passwordTextEdit']")).sendKeys("report1");
		driver.findElement(By.xpath("//*[@id='buttonTable']/input")).click();
		driver.switchTo().defaultContent(); 
		
		Thread.sleep(3000);
		
		driver.switchTo().frame(0);
		driver.findElement(By.xpath("//*[@id='IconImg_Txt_btnListing']")).click();
		Thread.sleep(3000);

}
}
