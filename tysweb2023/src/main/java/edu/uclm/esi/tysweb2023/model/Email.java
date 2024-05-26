package edu.uclm.esi.tysweb2023.model;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class Email {
	JSONObject configuration;

	public JSONObject getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JSONObject configuration) {
		this.configuration = configuration;
	}

	public static void main(String[] args) {
		Email e = new Email();
		//e.send("prueba@gmail.com","AsuntoDesdeMain", "hola desde main");

	}
	
	public void send(String destinatario, String asunto, String body) {
		try {
			this.setConfiguration(read("./parametros.txt"));
		} catch (Exception e) {
			System.out.println("Se ha producido un error al obtener los datos de parametros.txt: "+e.getMessage());
		}
		
		//formatear json brevo
	
		JSONObject jEmail = getConfiguration().getJSONObject("email");
		
		JSONArray jsaHeaders = new JSONArray().
				put("api-key").put(jEmail.getString("api-key")).
				put("accept").put("application/json").
				put("content-type").put(jEmail.getString("content-type"));
		
		JSONObject jsoTo = new JSONObject().
				put("email", destinatario).
				put("name", destinatario);
		
		 JSONObject jsoData = new JSONObject().
		        put("sender",jEmail.getJSONObject("sender")).
		        put("to",new JSONArray().put(jsoTo)).
		        put("subject", asunto).
		        put("htmlContent", body);
			    
		 JSONObject payload = new JSONObject().
		        put("url", jEmail.getString("endpoint")).
		        put("headers", jsaHeaders).
		        put("data", jsoData);

		Client client = new Client();
		client.sendCurlPost(jEmail.getString("endpoint"), jEmail.getString("api-key"), jsoData);
		
	}

	private JSONObject read(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) {
                return null;
            }
            byte[] b = new byte[is.available()];
            is.read(b);
            String content = new String(b, StandardCharsets.UTF_8);
            return new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace(); 
            return null;
        }
	}

}
