package edu.uclm.esi.tysweb2023.http;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.tysweb2023.model.Tablero4R;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.services.UserService;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("payments")
@CrossOrigin(origins="http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
public class PaymentController {
	
	@Autowired
	private UserService userService;
	public static ConcurrentHashMap<String, HttpSession> httpSessions = new ConcurrentHashMap<>();
	static {
		Stripe.apiKey="";
	}
	
	@PostMapping("/autorizarPago")
	public String prepay(HttpSession session, int matches) {
		try {
			if (session.getAttribute("user")==null) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Tienes que estar logado para comprar partidas");
			}
			if (matches!=10) {
				
			}
			
			long total = matches==10 ? 10 : 15;
			total = total * 100;
			
			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
					.setCurrency("eur")
					.setAmount((long) total)
					.build();
			
			PaymentIntent intent = PaymentIntent.create(createParams);
			JSONObject jso = new JSONObject();
			jso.put("client_secret", intent.getClientSecret());
			
			return jso.toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	//Añadir confirmarPago
	
}
