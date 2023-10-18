package edu.uclm.esi.tysweb2023.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.uclm.esi.tysweb2023.dao.UserDAO;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUser {
	@Autowired
	private MockMvc server;
	
	private MockHttpSession sessionPepe, sessionAna, sessionTurno;
	
	@Autowired
	private UserDAO userDAO;

	private String idTablero;
	
	//Se ejecuta antes que el siguiente método (1 sola vez)
	@BeforeAll
	void setUp() {
		this.userDAO.deleteAll();
	}
	
	//Test con parámetros
	@ParameterizedTest
	@CsvSource({
				
		"Pepe Pérez, jose.perez@gmail.com, joseperez, joseperez, 200, OK",
		"Ana, ana.perez@gmail.com, anaperez, anaperez, 403, El nombre debe tener al menos 5 caracteres",
		"Ana Pérez, ana.perez@gmail.com, ana, ana, 403, La contraseña debe tener al menos 5 caracteres",
		"Ana Pérez, ana.perez@gmail.com, ana1234, ana123, 403, Las contraseñas no coinciden",
		"Ana Pérez, ana.perez@gmail.com, ana1234, ana1234, 200, OK",
		"Ana López, ana.perez@gmail.com, ana1234, ana1234, 403, Ese correo electrónico ya existe",
	})
	
	@Order(1)
	void testRegistroMultiple(String nombre, String email, String pwd1, String pwd2, int codigo, String mensaje) throws Exception {
		
		JSONObject jso  = new JSONObject().
				put("nombre", nombre).
				put("email", email).
				put("pwd1", pwd1).
				put("pwd2", pwd2);
		
		
		RequestBuilder request = MockMvcRequestBuilders.
				post("/users/register").contentType("application/json").content(jso.toString());
		
		ResultActions resultActions = this.server.perform(request);
		MockHttpServletResponse response = resultActions.andReturn().getResponse();
		
		
		String mensajeRecibido = response.getErrorMessage();
		if (codigo == 200) {
			mensaje = null;
		}
		resultActions.andExpect(status().is(codigo));
		assertEquals(mensaje, mensajeRecibido);
	}
	
	
	@ParameterizedTest
	@CsvSource({	
		"jose.perez@gmail.com, joseperez, 200",
		"ana.perez@gmail.com, ana123, 403",
		"ana.perez@gmail.com, ana1234, 200",
	})
	@Order(2)
	void testLoginsMultiple(String email, String pwd, int codigo) throws Exception {
		
		JSONObject jso  = new JSONObject().
				put("email", email).
				put("pwd", pwd);
		
		
		RequestBuilder request = MockMvcRequestBuilders.
				put("/users/login").contentType("application/json").content(jso.toString());
		
		ResultActions resultActions = this.server.perform(request);
		resultActions.andExpect(status().is(codigo));
		
		if (codigo == 200 && email.startsWith("jose")) {
			this.sessionPepe = (MockHttpSession) resultActions.andReturn().getRequest().getSession();
		}else if(codigo == 200 && email.startsWith("ana")){
			this.sessionAna = (MockHttpSession) resultActions.andReturn().getRequest().getSession();
		}
	}
	
	
	@Test
	@Order(3)
	void testInicioDePartida() throws Exception {
		RequestBuilder requestPepe = MockMvcRequestBuilders.
				get("/matches/start").session(this.sessionPepe);
		
		RequestBuilder requestAna = MockMvcRequestBuilders.
				get("/matches/start").session(this.sessionAna);
		
		//Tablero Pepe
		ResultActions raPepe = this.server.perform(requestPepe);
		String tableroPepe = raPepe.andReturn().getResponse().getContentAsString();
		JSONObject jsoTableroPepe = new JSONObject(tableroPepe);
		
		System.out.println(jsoTableroPepe);
		
		
		//Tablero Ana
		ResultActions raAna = this.server.perform(requestAna);
		String tableroAna = raAna.andReturn().getResponse().getContentAsString();
		JSONObject jsoTableroAna = new JSONObject(tableroAna);
		
		System.out.println(jsoTableroAna);
		
		assertTrue(jsoTableroPepe.get("id").equals(jsoTableroAna.get("id")), "Los ids no coinciden");
		this.idTablero = jsoTableroPepe.getString("id");
		
		String idJugadorConElTurno = jsoTableroAna.getJSONObject("jugadorConElTurno").getString("id");
		String idPepe = jsoTableroAna.getJSONArray("players").getJSONObject(0).getString("id");
		
		if (idJugadorConElTurno.equals(idPepe))
			this.sessionTurno = this.sessionPepe;
		else
			this.sessionTurno = this.sessionAna;
	}
	
	
	@Test
	@Order(4)
	void testPartida() throws Exception {
		//Request PEPE
		JSONObject m = new JSONObject()
				.put("id", this.idTablero)
				.put("columna", 2);
		
		RequestBuilder request1 = MockMvcRequestBuilders.
				post("/matches/poner")
				.session(this.sessionTurno)
				.contentType("application/json")
				.content(m.toString());
		
		this.server.perform(request1).andExpect(status().isOk());
		
		this.cambiarTurno();
		
		RequestBuilder meToca = MockMvcRequestBuilders
				.get("/matches/meToca?id="+ this.idTablero)
				.session(this.sessionTurno);
		
		ResultActions raMeToca = this.server.perform(meToca);
		raMeToca.andExpect(status().isOk());
		assertTrue(raMeToca.andReturn().getResponse().getContentAsString().equals("true"));
		
		RequestBuilder request2 = MockMvcRequestBuilders.
				post("/matches/poner")
				.session(this.sessionTurno)
				.contentType("application/json")
				.content(m.toString());
		
		this.server.perform(request2).andExpect(status().isOk());
	}
	

	private void cambiarTurno() {
		if(this.sessionTurno == this.sessionPepe) {
			this.sessionTurno = this.sessionAna;
		}else {
			this.sessionTurno = this.sessionPepe;
		}
		
	}

	//Petición Post register que toma JSON con 4 parámetros 
	@Test
	@Disabled
	void testRegistrar() throws Exception{
		
		//Caso correcto
		//Registro usuario normal
		JSONObject pepe = new JSONObject().
				put("nombre", "Pepe Pérez").
				put("email", "pepe@gmail.com").
				put("pwd1", "joseperez").
				put("pwd2", "joseperez");
		
		RequestBuilder request = MockMvcRequestBuilders.
				post("/users/register").contentType("application/json").content(pepe.toString());
		
		this.server.perform(request).andExpect(status().isOk());
		
		//Caso incorrecto
		//Nombre con longitud menor a 5 caracteres 
		JSONObject ana  = new JSONObject().
				put("nombre", "Ana").
				put("email", "ana@gmail.com").
				put("pwd1", "anaperez").
				put("pwd2", "anaperez");
		request = MockMvcRequestBuilders.
				post("/users/register").contentType("application/json").content(ana.toString());
		
		ResultActions resultActions = this.server.perform(request);
		MockHttpServletResponse response = resultActions.andReturn().getResponse();
		
		String mensaje = response.getErrorMessage();
		resultActions.andExpect(status().is(403));
		assertEquals("El nombre debe tener al menos 5 caracteres", mensaje);
	}
}
