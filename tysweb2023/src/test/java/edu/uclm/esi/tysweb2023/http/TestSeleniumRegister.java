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
public class TestSeleniumRegister {
	private FirefoxDriver driver;
	
	@BeforeAll
	public void setUo() {
		String ruta_driver = "C:\\Repositorios\\tyweb2023\\geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", ruta_driver);
		driver = new FirefoxDriver();
	}
	
	@Test
	public void registrarUsuario() {
		driver.get("http://localhost:4200/Register");
		
		WebElement cajaNombre = driver.findElement(By.xpath("/html/body/app-root/app-register/div/div/div[1]/div[2]/div/form/div[1]/div/input"));
		cajaNombre.click();
		cajaNombre.sendKeys("Pepe");
		
		WebElement cajaEmail = driver.findElement(By.xpath("/html/body/app-root/app-register/div/div/div[1]/div[2]/div/form/div[3]/div/input"));
		cajaEmail.click();
		cajaEmail.sendKeys("angel.villafranca@alu.uclm.es");
		
		WebElement cajaPassword = driver.findElement(By.xpath("/html/body/app-root/app-register/div/div/div[1]/div[2]/div/form/div[5]/div/input"));
		cajaPassword.click();
		cajaPassword.sendKeys("1234567890");
		
		WebElement cajaValidarPassword = driver.findElement(By.xpath("/html/body/app-root/app-register/div/div/div[1]/div[2]/div/form/div[7]/div/input"));
		cajaValidarPassword.click();
		cajaValidarPassword.sendKeys("1234567890");
		
		WebElement boton = driver.findElement(By.xpath("/html/body/app-root/app-register/div/div/div[1]/div[2]/div/form/div[9]/div/button"));
		//boton.click();
	}
	
	@AfterEach
	public void teardown() {
		//driver.quit();
	}
}
