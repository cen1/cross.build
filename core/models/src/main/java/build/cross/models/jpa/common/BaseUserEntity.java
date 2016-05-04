package build.cross.models.jpa.common;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import build.cross.models.jpa.User;

@MappedSuperclass
public class BaseUserEntity extends BaseEntity {

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
