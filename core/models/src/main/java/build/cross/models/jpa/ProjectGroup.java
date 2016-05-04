package build.cross.models.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import build.cross.models.jpa.common.BaseUserEntity;

@Table(name="project_groups")
@Entity
public class ProjectGroup extends BaseUserEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String repository;
	
	@OneToMany(mappedBy="projectGroup", fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
	private List<Project> projects;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
}
