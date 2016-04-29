package build.cross.models.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import build.cross.models.enums.Arch;
import build.cross.models.enums.Kernel;
import build.cross.models.jpa.common.BaseEntity;

@Table(name="platforms")
@Entity
public class Platform extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	@Enumerated(EnumType.STRING)
	private Kernel kernel;
	
	@Enumerated(EnumType.STRING)
	private Arch arch;
	
	private String version;
		
	@OneToMany(mappedBy="platform")
	@JsonIgnore
	private List<VmSetting> vmSettings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Kernel getKernel() {
		return kernel;
	}

	public void setKernel(Kernel kernel) {
		this.kernel = kernel;
	}

	public Arch getArch() {
		return arch;
	}

	public void setArch(Arch arch) {
		this.arch = arch;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<VmSetting> getVmSettings() {
		return vmSettings;
	}

	public void setVmSettings(List<VmSetting> vmSettings) {
		this.vmSettings = vmSettings;
	}

	public Platform(String name, Kernel kernel, Arch arch, String version) {
		super();
		this.name = name;
		this.kernel = kernel;
		this.arch = arch;
		this.version = version;
	}

	public Platform() {
		super();
	}
}
