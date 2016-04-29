package build.cross.services.jenkins;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

@Stateless
public class JenkinsManagerSb implements JenkinsManagerSblocal {
	
	private Client client;
	
	private String jenkinsApi;
	
	@PostConstruct
	private void init() {
		jenkinsApi=System.getProperty("JENKINS_API");
		client = ClientBuilder.newClient();
	}
	
	@Override
	public Response enableInitialSecurity() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("initial_security.json").getFile());
		String body = FileUtils.readFileToString(file);
		
		Form form = new Form();
		form.param("json", body);
		
		WebTarget wt = client.target(jenkinsApi);
		Response response = wt.path("/configureSecurity/configure")
			.request(MediaType.APPLICATION_FORM_URLENCODED)
	    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		return response;
	}
	
	@Override
	public Response registerAdmin() {
		
		JSONObject j = new JSONObject();
		j.put("username", "admin");
		j.put("password1", System.getProperty("JENKINS_PASSWORD"));
		j.put("password2", System.getProperty("JENKINS_PASSWORD"));
		j.put("fullname", "admin");
		j.put("email", System.getProperty("JENKINS_EMAIL"));
		
		Form form = new Form();
		form.param("json", j.toString());
		
		WebTarget wt = client.target(jenkinsApi);
		Response response = wt.path("/securityRealm/createAccount")
			.request(MediaType.APPLICATION_FORM_URLENCODED)
	    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		return response;
	}
	
	@Override
	public Response enableFinalSecurity() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("final_security.json").getFile());
		String body = FileUtils.readFileToString(file);
		
		Form form = new Form();
		form.param("json", body);
		
		WebTarget wt = client.target(jenkinsApi);
		Response response = wt.path("/configureSecurity/configure")
			.request(MediaType.APPLICATION_FORM_URLENCODED)
	    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		return response;
	}
}
