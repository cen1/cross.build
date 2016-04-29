package build.cross.jenkins.mediation;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface SecurityMediationSbLocal {

	Response addGlobalCredentials();

	String getCrossBuildCredential();

}
