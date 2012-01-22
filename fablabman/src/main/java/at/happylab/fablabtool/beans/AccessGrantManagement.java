package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.AccessGrant;

public class AccessGrantManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public AccessGrantManagement(EntityManager em) {
		this.em = em;
	}
	
	public AccessGrantManagement() {
		
	}

	public void storeAccessGrant(AccessGrant ag) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(ag);
		em.getTransaction().commit();
		Logger.getLogger("AccessGrantManagement").info(
				"number of AccessGrants: "
						+ String.valueOf(em.createQuery(
								"select count(a) from AccessGrant a ")
								.getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removeAccessGrant(AccessGrant ag) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(ag);
		em.getTransaction().commit();
	}

	public AccessGrant loadAccessGrant(long id) {
		return em.find(AccessGrant.class, id);
	}

}
