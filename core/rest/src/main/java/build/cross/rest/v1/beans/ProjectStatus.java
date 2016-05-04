package build.cross.rest.v1.beans;

import com.fasterxml.jackson.databind.JsonNode;

import build.cross.models.jpa.Project;

public class ProjectStatus {
	
	private Project project;
	private JsonNode status;
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public JsonNode getStatus() {
		return status;
	}
	public void setStatus(JsonNode status) {
		this.status = status;
	}
	
}
