package build.cross.rest.v1.resources;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import build.cross.services.cloud.management.CryptographySblocal;
import build.cross.services.jenkins.JenkinsManagerSblocal;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/test")
@RequestScoped
public class TestResource {
	
	private Logger logger = Logger.getLogger(TestResource.class.getSimpleName());
	
	@EJB
	private CryptographySblocal crypto;
	
	@EJB
	private JenkinsManagerSblocal jenkins;
	
    @GET
    public Response registrirajId() throws IOException {
    	
    	Response r = jenkins.enableInitialSecurity();
    	logger.info(Integer.toString(r.getStatus()));
    	r=jenkins.registerAdmin();
    	logger.info(Integer.toString(r.getStatus()));
    	r=jenkins.enableFinalSecurity();
    	logger.info(Integer.toString(r.getStatus()));
    	
    	return Response.ok().build();
    }
}