package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.SelectOption;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Package;

public class KeycardManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public KeycardManagement(EntityManager em) {
		this.em = em;
	}

	public KeycardManagement() {

	}

	public void storeAccessGrant(AccessGrant ag) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(ag);
		em.getTransaction().commit();
		Logger.getLogger(" KeycardManagement").info(
				"number of  Keycards: "
						+ String.valueOf(em.createQuery(
								"select count(k) from Keycard k ")
								.getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removeKeycard( KeyCard k) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(k);
		em.getTransaction().commit();
	}

	public KeyCard loadKeyCard(long id) {
		return em.find(KeyCard.class, id);
	}

	public List<KeyCard> getAllKeyCards() {
		return em.createQuery("from KeyCard", KeyCard.class).getResultList();

	}


}
