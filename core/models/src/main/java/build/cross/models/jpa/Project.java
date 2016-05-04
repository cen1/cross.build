package build.cross.models.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import build.cross.models.jpa.common.BaseEntity;

@Table(name="projects")
@Entity
public class Project extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="build_command")
	private String buildCommand;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="vm_setting_id")
	private VmSetting vmSetting;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy = "project")
	@JsonIgnore
	private Container container;
	
	@ManyToOne
	@JoinColumn(name="project_group_id")
	@JsonIgnore
	private ProjectGroup projectGroup;

	public String getBuildCommand() {
		return buildCommand;
	}

	public void setBuildCommand(String buildCommand) {
		this.buildCommand = buildCommand;
	}

	public VmSetting getVmSetting() {
		return vmSetting;
	}

	public void setVmSetting(VmSetting vmSetting) {
		this.vmSetting = vmSetting;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public ProjectGroup getProjectGroup() {
		return projectGroup;
	}

	public void setProjectGroup(ProjectGroup projectGroup) {
		this.projectGroup = projectGroup;
	}
}
