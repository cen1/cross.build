package build.cross.jenkins.mediation;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Stateless
public class MediationCommonSb {
	
	@PersistenceContext(unitName = "crossbuild")
	protected EntityManager em;
	
	protected String jenkinsApi;
	protected String jenkinsUser;
	protected String jenkinsPassword;
	
	protected String authorizationHeaderValue;
	
	@PostConstruct
	protected void init() {
		jenkinsApi=System.getProperty("JENKINS_API");
		jenkinsUser=System.getProperty("JENKINS_USER");
		jenkinsPassword=System.getProperty("JENKINS_PASSWORD");
		
		String usernameAndPassword = jenkinsUser + ":" + jenkinsPassword;
        authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
 
	}
}
