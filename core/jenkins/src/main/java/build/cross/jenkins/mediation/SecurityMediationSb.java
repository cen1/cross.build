package build.cross.jenkins.mediation;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import build.cross.models.jpa.KeyPair;

@Stateless
public class SecurityMediationSb extends MediationCommonSb implements SecurityMediationSbLocal {
	
	private Logger logger = Logger.getLogger(SecurityMediationSb.class.getSimpleName());
	
	@Override
	public Response addGlobalCredentials() {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		TypedQuery<KeyPair> queryK = em.createNamedQuery("KeyPair.findByName", KeyPair.class);
		queryK.setParameter("name", "crossbuild_container");
		KeyPair kp = queryK.getResultList().get(0);

		Form form = new Form();
		
		JSONObject json = new JSONObject();
		
		JSONObject credentials = new JSONObject();
		credentials.put("scope", "GLOBAL");
		credentials.put("username", "crossbuild");
		credentials.put("passphrase", "");
		credentials.put("description", "");
		credentials.put("id", "");
		credentials.put("$stapler.class", "com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey");
		credentials.put("$class", "com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey");
		
		JSONObject privateKeySource = new JSONObject();
		privateKeySource.put("value", "0");
		privateKeySource.put("privateKey", kp.getPrivMaterial());
		privateKeySource.put("stapler-class", "com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey$DirectEntryPrivateKeySource");
		
		credentials.put("privateKeySource", privateKeySource);
		json.put("credentials", credentials);
		
		form.param("json", json.toString());
		
		Response response = wt.path("/credential-store/domain/_/createCredentials")
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		//logger.info("Response from jenkins: "+response.getStatus());
		
		return response;
	}
	
	@Override
	public String getCrossBuildCredential() {
		Form form = new Form();
		form.param("host", "");
		form.param("port", Integer.toString(22));
		
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Response response = wt.path("/descriptorByName/hudson.plugins.sshslaves.SSHLauncher/fillCredentialsIdItems")
				.request().accept(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_HTML)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		String entityString = response.readEntity(String.class);
		JSONObject entity = new JSONObject(entityString);
		
		JSONArray jarr = entity.getJSONArray("values");
		for (int i = 0; i < jarr.length(); i++) {
			  JSONObject cred = jarr.getJSONObject(i);
			  if (cred.getString("name").equals("crossbuild")) {
				  return cred.getString("value");
			  }
		}
		
		return null;
	}
}
