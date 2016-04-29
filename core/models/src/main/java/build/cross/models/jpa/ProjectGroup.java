package build.cross.models.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import build.cross.models.jpa.common.BaseEntity;

@Table(name="project_groups")
@Entity
public class ProjectGroup extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String repository;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy="projectGroup")
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
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
}
