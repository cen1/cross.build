package build.cross.jenkins.mediation;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import build.cross.models.jpa.Container;
import build.cross.models.jpa.Project;

@Local
public interface ProjectMediationSbLocal {

	Response addProject(Project project);
	
}
