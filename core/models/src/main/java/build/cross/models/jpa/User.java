package build.cross.models.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

@Table(name="users")
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private List<ProjectGroup> projects;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ProjectGroup> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectGroup> projects) {
		this.projects = projects;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	protected Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	protected Date updatedAt;

	@PrePersist
	private void onCreate() {
		Date date = new Date();

		this.setUpdatedAt(date);
		this.setCreatedAt(date);
	}

	@PreUpdate
	private void onUpdate() {
		this.setUpdatedAt(new Date());
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
