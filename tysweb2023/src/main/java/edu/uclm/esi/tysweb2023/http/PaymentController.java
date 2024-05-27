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
		Stripe.apiKey="sk_test_51OMogvF1fgtfUhRcy5yWIO44NdLcTD5gE1746960vlBd87VjnzCDz2q20Scr60YimG6bwOpKxAMS6OIBJK8vhULT0043akQdCE";
	}
	
	@GetMapping("/autorizarPago")
	public String prepay(HttpSession session, @RequestParam int matches) {
		try {
			if (session.getAttribute("user")==null) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Tienes que estar logado para comprar partidas");
			}
			if (matches!=10 && matches!=20)
				throw new ResponseStatusException(HttpStatus.CONFLICT,"You can only buy either 10 or 20 matches");
			
			long total = matches==10 ? 10 : 15;
			total = total * 100;
			
			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
					.setCurrency("eur")
					.setAmount(total)
					.build();
			
			PaymentIntent intent = PaymentIntent.create(createParams);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			session.setAttribute("matches", matches);
			return clientSecret;

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	

	@GetMapping("/confirmarPago")
	public void confirm(HttpSession session) {
		if (session.getAttribute("client_secret")==null
				|| session.getAttribute("matches")==null
				|| session.getAttribute("user")==null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		String userId = session.getAttribute("user").toString();
		this.userService.addMatches(userId, (Integer) session.getAttribute("matches"));
	}	
}
