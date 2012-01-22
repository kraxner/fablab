package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.Subscription;

public class SubscriptionManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public SubscriptionManagement(EntityManager em) {
		this.em = em;
	}

	public SubscriptionManagement() {

	}

	public void storeSubscription(Subscription subscription) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(subscription);
		em.getTransaction().commit();
		Logger.getLogger("SubscriptionManagement").info("number of Subscriptions: " + String.valueOf(em.createQuery("select count(m) from Subscription m ").getSingleResult()));
	}

	/**
	 * Removes a subscription and all dependent objects
	 * 
	 * @param subscription
	 */
	public void removeMembership(Subscription subscription) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(subscription);
		em.getTransaction().commit();
	}

	public Subscription loadSubscription(long id) {
		return em.find(Subscription.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Subscription> getAllSubscriptionsFromMember(long memberID) {
		ArrayList<Subscription> subscriptions = null;
		Query qry = em.createQuery("FROM Subscription WHERE Bookedby_ID = :memberid", Subscription.class);
		qry.setParameter("memberid", memberID);
		
		try {
			subscriptions = new ArrayList<Subscription>(qry.getResultList());
		} catch (NoResultException e) {
			subscriptions = null;
		} catch (IllegalArgumentException e) {
			subscriptions = null;
		}

		return subscriptions;
	}

}
