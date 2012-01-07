package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.wicket.util.template.PackagedTextTemplate;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PackageType;

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
		Logger.getLogger("SubscriptionManagement").info(
				"number of Subscriptions: "
						+ String.valueOf(em.createQuery(
								"select count(m) from Subscription m ")
								.getSingleResult()));
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
	


}
