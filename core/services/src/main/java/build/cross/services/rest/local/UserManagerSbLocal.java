package build.cross.services.rest.local;

import javax.ejb.Local;

import build.cross.models.jpa.User;

@Local
public interface UserManagerSbLocal {

	User get(String id);

	User createWithId(String id);

}
