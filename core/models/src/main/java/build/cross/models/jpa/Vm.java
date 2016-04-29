package build.cross.models.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import build.cross.models.jpa.common.BaseEntity;

@Table(name="vms")
@Entity
@NamedQuery(name="Vm.findAll", query="SELECT v FROM Vm v")
public class Vm extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String provider;
	
	@Column(name="cloud_id")
	@JsonIgnore
	private String cloudId;
	
	@Column(name="group_name")
	private String groupName;
	
	@JsonIgnore
	private String ip;
	
	@OneToMany(mappedBy="vm")
	@JsonIgnore
	private List<Container> containers;
	
	@OneToMany(mappedBy="vm")
	@JsonIgnore
	private List<LoadHistory> loadHistory;
	
	@ManyToOne
	@JoinColumn(name="keypair_id")
	@JsonIgnore
	private KeyPair keyPair;
	
	@ManyToOne
	@JoinColumn(name="vm_setting_id")
	private VmSetting vmSetting;

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public List<LoadHistory> getLoadHistory() {
		return loadHistory;
	}

	public void setLoadHistory(List<LoadHistory> loadHistory) {
		this.loadHistory = loadHistory;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public VmSetting getVmSetting() {
		return vmSetting;
	}

	public void setVmSetting(VmSetting vmSetting) {
		this.vmSetting = vmSetting;
	}
	
}
