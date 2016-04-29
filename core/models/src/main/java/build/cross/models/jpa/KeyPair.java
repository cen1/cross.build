package build.cross.models.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import build.cross.models.jpa.common.BaseEntity;

@Entity
@Table(name="keypairs")
@NamedQueries({
	@NamedQuery(name = "KeyPair.findByName", query = "SELECT k FROM KeyPair k WHERE k.name = :name") }
)
public class KeyPair extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private String name;
	
	@NotNull
	@Column(name="priv_material")
	private String privMaterial;
	
	@NotNull
	@Column(name="pub_material")
	private String pubMaterial;
	
	@OneToMany(mappedBy="keyPair")
	private List<Vm> vms;
	
	@OneToMany(mappedBy="keyPair")
	private List<Container> containers;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrivMaterial() {
		return privMaterial;
	}

	public void setPrivMaterial(String privMaterial) {
		this.privMaterial = privMaterial;
	}

	public String getPubMaterial() {
		return pubMaterial;
	}

	public void setPubMaterial(String pubMaterial) {
		this.pubMaterial = pubMaterial;
	}
	
}
