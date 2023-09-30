package edu.uclm.esi.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import edu.uclm.esi.auth.AppAuth;

@SpringBootApplication
@ServletComponentScan
public class AppAuth extends SpringBootServletInitializer {
	public static void main(String[] args) {
		 SpringApplication.run(AppAuth.class, args);
	}

}
