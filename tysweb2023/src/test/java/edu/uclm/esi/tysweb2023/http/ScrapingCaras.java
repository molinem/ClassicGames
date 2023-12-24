package edu.uclm.esi.tysweb2023.http;

import java.time.Duration;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScrapingCaras {
	private WebDriver driver1;
	private WebDriverWait wait1;
	
	@BeforeEach
	public void setUp() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		String driverPath = SeleniumManager.getInstance().getDriverPath(options, false).getDriverPath();
		
		System.setProperty("webdriver.chrome.driver", driverPath);
		driver1 = new ChromeDriver(options);
		
		
		wait1= new WebDriverWait(driver1, Duration.ofSeconds(3));
		
		
		Point izquierda = new Point(0, 0);
		driver1.manage().window().setPosition(izquierda);
		
	}
	
	@Test @Order(1)
	public void testEscenario1() throws Exception {
		Actions actions = new Actions(driver1);
		for(int i=0; i<100; i++){
			driver1.get("https://thispersondoesnotexist.com");
			WebElement img = driver1.findElement(By.xpath("/html/body/img"));
			//hacemos click derecho sobre la imagen
			actions.contextClick(img);
			actions.sendKeys(Keys.DOWN,Keys.ARROW_DOWN).perform();
			actions.sendKeys(Keys.DOWN,Keys.ARROW_DOWN).perform();
			actions.perform();
			System.out.println();
		}
		
	}

	@AfterEach
	public void tearDown() {
		driver1.quit();
	}
	
	
}