package build.cross.models.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

@Table(name="containers")
@Entity
@NamedQueries( {
	@NamedQuery(name="Container.findLatest", query="SELECT c FROM Container c WHERE c.vm.id=:vmId ORDER BY c.createdAt DESC"),
	@NamedQuery(name="Container.findByName", query="SELECT c FROM Container c WHERE c.name=:name")
})
public class Container implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String projectId;
	
	private String name;
	private String ip;
	private Integer port;
	
	@ManyToOne
	@JoinColumn(name="vm_id")
	private Vm vm;
	
	@OneToOne
	@MapsId
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private Project project;
	
	@ManyToOne
	@JoinColumn(name="keypair_id")
	private KeyPair keyPair;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Vm getVm() {
		return vm;
	}
	public void setVm(Vm vm) {
		this.vm = vm;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public KeyPair getKeyPair() {
		return keyPair;
	}
	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}
}
