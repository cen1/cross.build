package build.cross.rest.v1.resources;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import build.cross.jenkins.mediation.NodeMediationSbLocal;
import build.cross.jenkins.mediation.ProjectMediationSbLocal;
import build.cross.jenkins.mediation.SecurityMediationSbLocal;
import build.cross.models.jpa.Container;
import build.cross.models.jpa.Project;
import build.cross.models.jpa.ProjectGroup;
import build.cross.models.jpa.User;
import build.cross.models.jpa.VmSetting;
import build.cross.rest.exceptions.ApiError;
import build.cross.rest.exceptions.ApiException;
import build.cross.rest.v1.interceptors.ValidatePermissions;
import build.cross.rest.v1.resources.common.CrudResource;
import build.cross.services.beans.ProjectSbLocal;
import build.cross.services.cloud.containers.ContainerManagerSbLocal;
import build.cross.services.exceptions.ServiceException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/projectgroups")
@RequestScoped
@ValidatePermissions
public class ProjectGroupResource extends CrudResource<ProjectGroup> {
	
	@EJB
	private ProjectSbLocal pm;
	
	@EJB
	private ProjectMediationSbLocal jenkinsProjects;
		
	@EJB
	private ContainerManagerSbLocal cmng;
	
	@EJB
	private NodeMediationSbLocal jenkinsNodes;
	
	@POST
	@Transactional(rollbackOn = {Exception.class, RuntimeException.class})
    public Response addNew(ProjectGroup projectGroup) throws ApiException {
		
		User user = em.find(User.class, sc.getUserPrincipal().getName());
		projectGroup.setUser(user);
		em.persist(projectGroup);
		
		for (Project project : projectGroup.getProjects()) {
			project.setId(null);
			project.setProjectGroup(projectGroup);
			try {
				project = cmng.createNewContainer(project);
			} catch (ServiceException e) {
				throw new ApiException("Bad request", new ApiError(400, e.getMessage()));
			}
			em.persist(project);
			jenkinsProjects.addProject(project);
			jenkinsNodes.createNode(project.getContainer());
		}
		
		return Response.ok(projectGroup).build();
    }
}
