package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import at.happylab.fablabtool.model.ConsumationEntry;

public class ConsumationEntryManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public ConsumationEntryManagement(EntityManager em) {
		this.em = em;
	}

	public ConsumationEntryManagement() {

	}

/*
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
*/

	public void removeEntry(ConsumationEntry entry) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(entry);
		em.getTransaction().commit();
	}	

}