package edu.uclm.esi.tysweb2023.http;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFakeBank {
	private FirefoxDriver driver1;
	private FirefoxDriver driver2;
	private WebDriverWait wait1,wait2;
	
	@BeforeAll
	public void setUp() {
		String ruta_driver = "C://repositorio//tyweb2023//geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", ruta_driver);
		driver1 = new FirefoxDriver();
		driver2 = new FirefoxDriver();
		
		wait1 = new WebDriverWait(driver1, Duration.ofSeconds(3));
		wait2 = new WebDriverWait(driver2, Duration.ofSeconds(3));
		
		Point izquierda = new Point(0,0);
		driver1.manage().window().setPosition(izquierda);
		
		Point derecha = new Point(600,0);
		driver1.manage().window().setPosition(derecha);
		
		driver1.manage().window().setPosition(izquierda);
		driver1.manage().window().setPosition(derecha);
	}
	
	private void borrarUsuario(WebDriver driver, String nombre) {
		driver.get("http://alarcosj.esi.uclm.es/fakeBank");
		WebElement cajaNombre = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[1]/input"));
		cajaNombre.click(); cajaNombre.clear(); cajaNombre.sendKeys(nombre);
		
		WebElement cajaPwd = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[2]/input"));
		cajaPwd.click();
		
		WebElement linkBorrar = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[4]/div/a"));
		linkBorrar.click();
		
	}
	
	private void login(WebDriver driver, String nombre, String pwd) {
		WebElement link = driver.findElement(By.xpath("/html/body/div/div[2]/div/oj-navigation-list/div/div/ul/li[1]/a"));
		link.click();
		
		this.borrarUsuario(driver1, "luis1");
		this.borrarUsuario(driver2, "luis2");
		
		WebElement cajaNombre = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[1]/input"));
		WebElement cajaPwd = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[2]/input"));
		cajaNombre.click();  cajaNombre.clear();  cajaNombre.sendKeys(nombre);
		cajaPwd.click(); cajaPwd.clear(); cajaPwd.sendKeys(pwd);
		
		WebElement boton = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/div[3]/button"));
		boton.click();
	}
	
	@Test @Order(1)
	public void testEscenario1() {
		driver1.get("http://alarcosj.esi.uclm.es/fakeBank");
		driver2.get("http://alarcosj.esi.uclm.es/fakeBank");
		
		this.registrar(driver1, "luis1", "luism1@luism.com", "luis1", "luis1");
		this.registrar(driver2, "luis2", "luism2@luism.com", "luis2", "luis2");
		
		this.login(driver1, "luis1", "luis1");
		this.login(driver2, "luis2", "luis2");
	}
	
	private void registrar(FirefoxDriver driver, String nombre, String email, String pwd1, String pwd2) {
		
		WebElement link = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[3]/div/a"));
		link.click();
		
		WebElement cajaNombre = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/input[1]"));
		WebElement cajaEmail = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/input[2]"));
		WebElement cajaPwd1 = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/input[1]"));
		WebElement cajaPwd2 = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/div[1]/input[2]"));
		WebElement boton = driver.findElement(By.xpath("/html/body/div/oj-module/div[1]/div[2]/div/div/div/button[2]"));
		
		cajaNombre.click(); cajaNombre.clear(); cajaNombre.sendKeys(nombre);
		cajaEmail.click(); cajaEmail.clear(); cajaEmail.sendKeys(email);
		cajaPwd1.click(); cajaPwd1.clear(); cajaPwd1.sendKeys(pwd1);
		cajaPwd2.click(); cajaPwd2.clear(); cajaPwd2.sendKeys(pwd2);
		boton.click();
	}

	@AfterEach
	public void teardown() {
		driver1.quit();
		driver2.quit();
	}
}
