package build.cross.services.rest;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import build.cross.models.jpa.User;
import build.cross.services.rest.local.UserManagerSbLocal;

@Stateless
public class UserManagerSb implements UserManagerSbLocal {
	
	@PersistenceContext(unitName = "crossbuild")
	protected EntityManager em;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public User createWithId(String id) {
		User user = new User();
		user.setId(id);
		em.persist(user);
		return user;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public User get(String id) {
		User u = em.find(User.class, id);
		return u;
	}
}
