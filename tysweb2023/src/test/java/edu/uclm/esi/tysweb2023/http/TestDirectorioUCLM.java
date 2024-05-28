package edu.uclm.esi.tysweb2023.http;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDirectorioUCLM {
	private FirefoxDriver driver;
	
	@BeforeAll
	public void setUo() {
		String ruta_driver = "C:\\Repositorios\\tyweb2023\\geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", ruta_driver);
		driver = new FirefoxDriver();
	}
	
	@Test
	public void buscarAlRector() {
		driver.get("https://directorio.uclm.es");
		WebElement caja = driver.findElement(By.id("CPH_CajaCentro_HF_ItemSelected"));
		caja.click();
		caja.sendKeys("garde");
		
		WebElement boton = driver.findElement(By.xpath("/html/body/form/div[3]/div[2]/div[2]/div/div[1]/div[2]/div[2]/a"));
		boton.click();

		//Código de la foto
	}
	
	@AfterEach
	public void teardown() {
		driver.quit();
	}
}
