package build.cross.models.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import build.cross.models.enums.CloudProvider;
import build.cross.models.jpa.common.BaseEntity;

@Table(name="vm_settings")
@Entity
@NamedQueries({
	@NamedQuery(name="VmSetting.findAll", query="SELECT v FROM VmSetting v")
})
public class VmSetting extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="platform_id")
	private Platform platform;
	
	@Enumerated(EnumType.STRING)
	private CloudProvider provider;
	
	@Column(name="group_name")
	private String groupName;
	
	//path to where bootstrap should be uploaded to
	@Column(name="bootstrap_upload_path")
	@JsonIgnore
	private String bootstrapUploadPath;
	
	//user that is used to login via ssh
	@Column(name="login_user")
	@JsonIgnore
	private String loginUser;
	
	//command that executes the boostrap script
	@Column(name="bootstrap_exec_cmd")
	@JsonIgnore
	private String bootstrapExecCmd;
	
	@Column(name="ami_id")
	private String amiId;
	
	//whether boostrap script should be executed as sudo
	@Column(name="bootstrap_as_sudo")
	@JsonIgnore
	private boolean bootstrapAsSudo;
	
	//name of the container template file which will be uploaded
	@Column(name="container_file")
	@JsonIgnore
	private String containerFile;
	
	//command which creates new container
	@Column(name="create_container_cmd")
	@JsonIgnore
	private String createContainerCmd;
	
	//command which creates new container
	@Column(name="delete_container_cmd")
	@JsonIgnore
	private String deleteContainerCmd;
	
	@OneToMany(mappedBy="vmSetting")
	@JsonIgnore
	private List<Vm> vms;
	
	@OneToMany(mappedBy="vmSetting")
	@JsonIgnore
	private List<Project> projects;
	
	public String getGroupName() {
		return groupName;
	}
	
	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setBootstrapUploadPath(String bootstrapUploadPath) {
		this.bootstrapUploadPath = bootstrapUploadPath;
	}

	public String getBootstrapExecCmd() {
		return bootstrapExecCmd;
	}
	public void setBootstrapExecCmd(String bootstrapExecCmd) {
		this.bootstrapExecCmd = bootstrapExecCmd;
	}	
	public String getAmiId() {
		return amiId;
	}
	public void setAmiId(String amiId) {
		this.amiId = amiId;
	}	
	public String getBootstrapUploadPath() {
		return bootstrapUploadPath;
	}	
	public boolean isBootstrapAsSudo() {
		return bootstrapAsSudo;
	}
	public void setBootstrapAsSudo(boolean bootstrapAsSudo) {
		this.bootstrapAsSudo = bootstrapAsSudo;
	}
	public String getContainerFile() {
		return containerFile;
	}
	public void setContainerFile(String containerFile) {
		this.containerFile = containerFile;
	}	
	public String getCreateContainerCmd() {
		return createContainerCmd;
	}
	public void setCreateContainerCmd(String createContainerCmd) {
		this.createContainerCmd = createContainerCmd;
	}
	
	public String getDeleteContainerCmd() {
		return deleteContainerCmd;
	}

	public void setDeleteContainerCmd(String deleteContainerCmd) {
		this.deleteContainerCmd = deleteContainerCmd;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public CloudProvider getProvider() {
		return provider;
	}

	public void setProvider(CloudProvider provider) {
		this.provider = provider;
	}

	public List<Vm> getVms() {
		return vms;
	}

	public void setVms(List<Vm> vms) {
		this.vms = vms;
	}

	public VmSetting(
			String loginUser, 
			String amiId, 
			String bootstrapUploadPathPrefix, 
			String bootstrapExecCmd, 
			boolean bootstrapAsSudo, 
			boolean bootstrapDos2Unix, 
			String containerFile, 
			String createContainerCmd,
			String deleteContainerCmd,
			Platform platform,
			CloudProvider provider) {
		super();
		
		this.loginUser = loginUser;
		this.bootstrapExecCmd = bootstrapExecCmd;
		this.amiId = amiId;
		this.bootstrapAsSudo = bootstrapAsSudo;
		this.containerFile = containerFile;
		this.createContainerCmd = createContainerCmd;
		this.deleteContainerCmd = deleteContainerCmd;
		
		this.bootstrapUploadPath = bootstrapUploadPathPrefix+"/"+loginUser+"/"+"bootstrap_"+amiId+".sh";
		this.groupName = "crossbuild-"+amiId;
		
		this.platform=platform;
		this.provider=provider;
	}
	
	public VmSetting() {};
}
