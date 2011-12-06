package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.Membership;

public class MembershipManagement implements Serializable{

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject	private EntityManager em;
	
	public void storeMembership(Membership member) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(member);
		em.getTransaction().commit();
		Logger.getLogger("Membershipmanagement").info("number of Members: " + String.valueOf(em.createQuery("select count(m) from Membership m ").getSingleResult()));
	}
	
	public Membership loadMembership(long id) {
		return em.find(Membership.class, id);
	}

}
