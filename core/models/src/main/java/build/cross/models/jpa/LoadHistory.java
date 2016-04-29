package build.cross.models.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import build.cross.models.enums.LoadType;
import build.cross.models.jpa.common.BaseEntity;

@Entity
@Table(name="load_history")
@NamedQueries({
	@NamedQuery(name = "LoadHistory.getSingleAverage", query = "SELECT AVG(l.value) FROM LoadHistory l WHERE l.type=:type AND l.vm.id=:vmId AND l.createdAt>:date"),
	@NamedQuery(name = "LoadHistory.getGroupAverage", query = "SELECT AVG(l.value) FROM LoadHistory l WHERE l.type=:type AND l.vm.groupName=:group AND l.createdAt>:date"),
	@NamedQuery(name = "LoadHistory.findMinLoad", query="SELECT l.id, AVG(l.value) AS val FROM LoadHistory l WHERE l.vm.groupName=:groupName GROUP BY l.vm.groupName, l.id ORDER BY val ASC")
})
public class LoadHistory extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private LoadType type;
	
	@NotNull
	private Double value;
	
	@ManyToOne
	@JoinColumn(name="vm_id")
	private Vm vm;
	
	public LoadType getType() {
		return type;
	}

	public void setType(LoadType type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public LoadHistory(LoadType type, Double value, Vm vm) {
		super();
		this.type = type;
		this.value = value;
		this.vm = vm;
	}

	public LoadHistory() {
		
	}

	public Vm getVm() {
		return vm;
	}

	public void setVm(Vm vm) {
		this.vm = vm;
	}
		
}
