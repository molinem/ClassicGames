package edu.uclm.esi.tysweb2023.http;

import java.util.Enumeration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("matches")
public class EjemplosController {
	
	//@GetMapping("")
	public String start(HttpServletRequest request,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String pwd) {
		
		Enumeration<String> headers = request.getHeaderNames();
		while(headers.hasMoreElements()) {
			String header = headers.nextElement();
			System.out.println(header + " = " + request.getHeader(header));
		}
		
		return "Hola, " + nombre;
	}
}
