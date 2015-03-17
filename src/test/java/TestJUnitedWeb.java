import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.api.IMobileDriver;

public class TestJUnitedWeb {

	static MobileDriver driver;
	//Before the test, I create Perfecto Mobile driver in order to connect the the cloud.
	//The driver Parameters are cloud_name, user and Password
	// to create your account please visit http://www.perfectomobile.com/
	@BeforeClass
	public static void testSetup() {
//		String host = Constants.PM_CLOUD;
//		String user = Constants.PM_USER;
//		String password = Constants.PM_PASSWORD;
		String host = System.getProperty("PerfectoCloud");
		String user = System.getProperty("PerfectoUsername");
		String password = System.getProperty("PerfectoPassword");		
		driver = new MobileDriver(host, user, password);

	}

	@AfterClass

	// After the test I close the driver and get the Perfecto Mobile report into the local machine 
	public static void testCleanup() {
		driver.quit();
	}

	// The test work on the specific device ID
	// This testcase go to united.com , look for specific flight and validate we go   
	@Test
	public void CheckFlight() {
		IMobileDevice device = ((IMobileDriver) driver).getDevice(System.getProperty("PerfectoDeviceID"));
		
		
		
		// Getting the device form Perfecto mobile system, open it and take it to the ready mode (HOME)
		device.open();
		device.home();

		// Define two types of web driver 
				// 1. DOM - standard web webdriver works with the DOM objects
				// 2. Visual Driver - allows to validate that text appear on the screen using visual analysis (OCR).
				//    This validation is very important and simulate the real user experience.

		WebDriver webDriver = device.getDOMDriver ("www.united.com");
		WebDriver visualDriver = device.getVisualDriver();

	 	webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
 		
		// press on the button "flight status"
		WebElement webElement = webDriver.findElement(By.xpath(".//*[@id='mainContent']/div[3]/ul[3]/li/div/div/a"));
		webElement.click();

		// press on the button "flight number"
		webElement = webDriver.findElement(By.xpath(".//*[@id='FlightNumber']"));
		webElement.sendKeys("84");

		// press on the button "Search"
		webDriver.findElement(By.xpath(".//*[@id='FLifoPartialHolder']/form/div[3]/input")).click();

		// visual based validation - validate the text appear on the screen and can be seen by users.
		visualDriver.manage().timeouts().implicitlyWait(25,TimeUnit.SECONDS);
		visualDriver.findElement(By.linkText("New York/Newark"));

		// object based validation - validate the text appear in the as part of the DOM structure 
		String text =  webDriver.findElement(By.xpath(".//*[@id='mainContent']/div[3]/ul/li[3]/div[1]/div[2]/text()")).getAttribute("text");
  	 	assertEquals("New dork/Newark, NJ (EWR)",text);
  
  	}


}