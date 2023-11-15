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
		e.send("Luis.Molina1@alu.uclm.es","AsuntoDesdeMain", "hola desde main");

	}
	
	public void send(String destinatario, String asunto, String body) {
		try {
			this.setConfiguration(read("./parametros.txt"));
		} catch (Exception e) {
			//Error
		}
		
		//formatear json brevo
		JSONObject jEmail = getConfiguration().getJSONObject("email");
		
		JSONArray jsaHeaders = new JSONArray().
				put("api-key").put(jEmail.getString("api-key")).
				put("accept").put("application/json").
				put("content-type").put(jEmail.getString("content-type"));
		
		JSONArray jsonTo = new JSONArray().
				put("email").put(destinatario).
				put("name").put(destinatario);
		
		//Faltan estos datos  //sender/to/subject/htmlContent
		JSONArray jsonData = new JSONArray().
				put("sender",JEmal.getJSOnObject,
		put("to").put(JSONArray().put(jsonTo)),
		put("subject").put(asunto)
		put("htmlContent").put(body)
				
		JSONArray payload = new JSONArray().
				put("url").put(jEmail.getString("end-point")).
				put("headers").put(jsaHeaders).
				put("data").put(jsoData);
		
		Client client = new Client();
		client.sendCurlPost(payload, body);
		
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
