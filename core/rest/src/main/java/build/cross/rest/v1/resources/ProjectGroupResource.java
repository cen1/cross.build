package build.cross.rest.v1.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
import build.cross.rest.v1.beans.ProjectStatus;
import build.cross.rest.v1.interceptors.ValidatePermissions;
import build.cross.rest.v1.resources.common.CrudResource;
import build.cross.rest.v1.resources.common.CrudUserResource;
import build.cross.services.beans.ProjectSbLocal;
import build.cross.services.cloud.containers.ContainerManagerSbLocal;
import build.cross.services.exceptions.ServiceException;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/projectgroups")
@RequestScoped
@ValidatePermissions
public class ProjectGroupResource extends CrudUserResource<ProjectGroup> {
	
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
	
	@GET
	public Response getList(@PathParam(value="userId") String userId) {
		return super.getList(userId);
	}
	
	@GET
	@Path("/{projectGroupId}")
	public Response get(@PathParam(value="userId") String userId, @PathParam(value="projectGroupId") String projectGroupId) throws ApiException {
		return super.get(userId, projectGroupId);
	}
	
	@GET
	@Path("/{projectGroupId}/projects/{projectId}/status")
	public Response getStatus(
			@PathParam(value="userId") String userId,
			@PathParam(value="projectGroupId") String projectGroupId,
			@PathParam(value="projectId") String projectId) throws ApiException, JsonProcessingException, IOException {
		
		ProjectGroup pg = validateEntity(userId, projectGroupId);
		Project proj = em.find(Project.class, projectId);
		
		if (pg == null || proj == null || !proj.getProjectGroup().equals(pg)) {
            throw new ApiException(new ApiError(404, "Resource not found", ProjectGroup.class.getSimpleName()+" "+projectGroupId));
        }
				
		Response r = jenkinsProjects.getProjectInfo(proj.getId());
		String json = r.readEntity(String.class);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/{projectGroupId}/projects/{projectId}/build/{buildNumber}")
	public Response getStatus(
			@PathParam(value="userId") String userId,
			@PathParam(value="projectGroupId") String projectGroupId,
			@PathParam(value="projectId") String projectId,
			@PathParam(value="buildNumber") String buildNumber) throws ApiException, JsonProcessingException, IOException {
		
		ProjectGroup pg = validateEntity(userId, projectGroupId);
		Project proj = em.find(Project.class, projectId);
		
		if (pg == null || proj == null || !proj.getProjectGroup().equals(pg)) {
            throw new ApiException(new ApiError(404, "Resource not found", ProjectGroup.class.getSimpleName()+" "+projectGroupId));
        }
				
		Response r = jenkinsProjects.getBuildDetails(proj.getId(), buildNumber);;
		String json = r.readEntity(String.class);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/{projectGroupId}/projects/{projectId}/build/{buildNumber}/console")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getBuildConsole(
			@PathParam(value="userId") String userId,
			@PathParam(value="projectGroupId") String projectGroupId,
			@PathParam(value="projectId") String projectId,
			@PathParam(value="buildNumber") String buildNumber) throws ApiException, JsonProcessingException, IOException {
		
		ProjectGroup pg = validateEntity(userId, projectGroupId);
		Project proj = em.find(Project.class, projectId);
		
		if (pg == null || proj == null || !proj.getProjectGroup().equals(pg)) {
            throw new ApiException(new ApiError(404, "Resource not found", ProjectGroup.class.getSimpleName()+" "+projectGroupId));
        }
				
		Response r = jenkinsProjects.getBuildConsoleText(proj.getId(), buildNumber);;
		String json = r.readEntity(String.class);
		return Response.ok(json).build();
	}
}
