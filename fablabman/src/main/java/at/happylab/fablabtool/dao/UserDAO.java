package at.happylab.fablabtool.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import net.micalo.persistence.dao.BaseDAO;
import at.happylab.fablabtool.model.User;


public class UserDAO extends BaseDAO<User> {
	private static final long serialVersionUID = 1L;

	public UserDAO(){
		super(User.class); 
	}
	
	public UserDAO(EntityManager em){
		super(User.class, em); 
	}
	
	public User loadFromKeycard(String rfid) {
		User user = null;
		Query qry = em.createQuery("from User where Keycard_id=:keycard_id", User.class);
		qry.setParameter("keycard_id", rfid);

		try {
			user = (User) qry.getSingleResult();
		} catch (NoResultException e) {
			// there is no user, therefore only the access times of this keycards count
			user = null;
		} catch (IllegalArgumentException e) {
			user = null;
		}

		return user;
	}
	
}
