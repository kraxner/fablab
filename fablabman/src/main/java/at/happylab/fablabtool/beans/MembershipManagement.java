package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.User;

public class MembershipManagement implements Serializable{

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject	private EntityManager em;
	
	public void addMembership(User member) {
		em.getTransaction().begin();
		em.persist(member);
		em.getTransaction().commit();
		Logger.getLogger("Membershiopmanagement").info("number of Users: " + String.valueOf(em.createQuery("select count(u) from User u ").getSingleResult())); 
	}

}