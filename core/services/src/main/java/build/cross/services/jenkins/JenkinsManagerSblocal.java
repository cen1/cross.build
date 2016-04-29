package build.cross.services.jenkins;

import java.io.IOException;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface JenkinsManagerSblocal {

	Response registerAdmin();

	Response enableFinalSecurity() throws IOException;

	Response enableInitialSecurity() throws IOException;

}
