package at.happylab.fablabtool.beans;

import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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
		Logger.getLogger("UserManagement").info(
				"number of Users: "
						+ String.valueOf(em.createQuery(
								"select count(d) from User d ")
								.getSingleResult()));
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


}
