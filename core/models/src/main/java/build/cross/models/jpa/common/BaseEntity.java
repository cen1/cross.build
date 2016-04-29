package build.cross.models.jpa.common;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(generator = "SYS_ID_GEN")
	@GenericGenerator(name = "SYS_ID_GEN", strategy = "uuid2")
	private String id;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BaseEntity that = (BaseEntity) o;
		return Objects.equals(id, that.id)
				&& Objects.equals(createdAt, that.createdAt)
				&& Objects.equals(updatedAt, that.updatedAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createdAt, updatedAt);
	}
}
