package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.Membership;

public class MembershipManagement implements Serializable{

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject	private EntityManager em;
	
	public void addMembership(Membership member) {
		em.getTransaction().begin();
		em.persist(member);
		em.getTransaction().commit();
		Logger.getLogger("Membershipmanagement").info("number of Users: " + String.valueOf(em.createQuery("select count(u) from User u ").getSingleResult()));
		//Logger.getLogger("Membershiopmanagement").info("info of Users: " + String.valueOf(em.createQuery("select gender from User u ").getSingleResult()));
	}
	
	
	
	
	

}
