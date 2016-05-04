package build.cross.jenkins.mediation;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import build.cross.models.jpa.Project;

@Local
public interface ProjectMediationSbLocal {

	Response addProject(Project project);

	Response getProjectInfo(String projectId);

	Response getBuildDetails(String projectId, String buildNumber);

	Response getBuildConsoleText(String projectId, String buildNumber);

	Response configureProject(Project project);
	
}
