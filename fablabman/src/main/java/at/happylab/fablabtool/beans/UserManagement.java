package at.happylab.fablabtool.beans;

import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.User;

public class UserManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public UserManagement() {

	}

	public UserManagement(EntityManager em) {
		this.em = em;
	}

	public void storeUser(User u) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(u);
		em.getTransaction().commit();
		Logger.getLogger("UserManagement").info("number of Users: " + String.valueOf(em.createQuery("select count(d) from User d ").getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removeUser(User ag) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(ag);
		em.getTransaction().commit();
	}

	public User loadUser(long id) {
		return em.find(User.class, id);
	}

	public User loadUserFromKeycard(String rfid) {
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
