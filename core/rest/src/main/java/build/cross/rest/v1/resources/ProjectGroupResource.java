package build.cross.rest.v1.resources;

import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import build.cross.jenkins.mediation.NodeMediationSbLocal;
import build.cross.jenkins.mediation.ProjectMediationSbLocal;
import build.cross.models.jpa.Project;
import build.cross.models.jpa.ProjectGroup;
import build.cross.models.jpa.User;
import build.cross.rest.v1.exceptions.ApiError;
import build.cross.rest.v1.exceptions.ApiException;
import build.cross.rest.v1.interceptors.ValidatePermissions;
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
			jenkinsNodes.createNode(project.getContainer());
			jenkinsProjects.addProject(project);
			jenkinsProjects.configureProject(project);
		}
		
		return Response.ok(projectGroup).build();
    }
	
	@PUT
	@Path("/{projectGroupId}")
	@Transactional(rollbackOn = {Exception.class, RuntimeException.class})
    public Response update(@PathParam(value="userId") String userId,
    					   @PathParam(value="projectGroupId") String projectGroupId,
    					   ProjectGroup projectGroup) throws ApiException {
		
		validateEntity(userId, projectGroupId);
    	User t = em.find(User.class, userId);
    	if (t == null) {
            throw new ApiException(new ApiError(404, "resource.not.found", User.class.getSimpleName()+" "+userId));
        }
    	projectGroup.setUser(t);
    	projectGroup.setId(projectGroupId);
    	
    	projectGroup=em.merge(projectGroup);
		
		for (Project project : projectGroup.getProjects()) {
			project.setProjectGroup(projectGroup);
			project=em.merge(project);
			jenkinsProjects.configureProject(project);
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
	public Response getBuild(
			@PathParam(value="userId") String userId,
			@PathParam(value="projectGroupId") String projectGroupId,
			@PathParam(value="projectId") String projectId,
			@PathParam(value="buildNumber") String buildNumber) throws ApiException, JsonProcessingException, IOException {
		
		ProjectGroup pg = validateEntity(userId, projectGroupId);
		Project proj = em.find(Project.class, projectId);
		
		if (pg == null || proj == null || !proj.getProjectGroup().equals(pg)) {
            throw new ApiException(new ApiError(404, "Resource not found", ProjectGroup.class.getSimpleName()+" "+projectGroupId));
        }
				
		Response r = jenkinsProjects.getBuildDetails(proj.getId(), buildNumber);
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
				
		Response r = jenkinsProjects.getBuildConsoleText(proj.getId(), buildNumber);
		String json = r.readEntity(String.class);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("/{projectGroupId}/buildNow")
	public Response buildNow(
			@PathParam(value="userId") String userId,
			@PathParam(value="projectGroupId") String projectGroupId) throws ApiException {
		
		ProjectGroup projectGroup = validateEntity(userId, projectGroupId);
		
		for (Project project : projectGroup.getProjects()) {
			jenkinsProjects.buildNow(project.getId());
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("/{projectGroupId}/projects/{projectId}/build/{buildNumber}/progressiveConsole")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getProgressiveConsole(
			@PathParam(value="userId") String userId,
			@PathParam(value="projectGroupId") String projectGroupId,
			@PathParam(value="projectId") String projectId,
			@PathParam(value="buildNumber") String buildNumber,
			@QueryParam(value="start") String start) throws ApiException, JsonProcessingException, IOException {
		
		ProjectGroup pg = validateEntity(userId, projectGroupId);
		Project proj = em.find(Project.class, projectId);
		
		if (pg == null || proj == null || !proj.getProjectGroup().equals(pg)) {
            throw new ApiException(new ApiError(404, "Resource not found", ProjectGroup.class.getSimpleName()+" "+projectGroupId));
        }
				
		Response r = jenkinsProjects.getProgressiveConsole(proj.getId(), buildNumber, start);
		String json = r.readEntity(String.class);
		
		return Response.ok(json).header("X-Text-Size", r.getHeaderString("X-Text-Size"))
								.header("X-More-Data", r.getHeaderString("X-More-Data"))
								.build();
	}
	
	@DELETE
	@Path("/{projectGroupId}")
	@Transactional
	public Response delete(@PathParam(value="userId") String userId, 
						   @PathParam(value="projectGroupId") String projectGroupId) throws ApiException {
		ProjectGroup projectGroup = validateEntity(userId, projectGroupId);
		
		//delete from jenkins and containers
		for (Project project : projectGroup.getProjects()) {
			jenkinsProjects.deleteProject(project.getId());
			jenkinsNodes.deleteNode(project.getContainer().getName());
			try {
				cmng.deleteContainer(project);
			} catch (ServiceException e) {
				throw new ApiException(new ApiError(500, "Error deleting containers"));
			}
		}		
		em.remove(projectGroup);
		
		return Response.ok().build();
	}
}
