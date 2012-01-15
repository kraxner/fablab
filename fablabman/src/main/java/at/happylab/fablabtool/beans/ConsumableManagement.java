package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.Consumable;

public class ConsumableManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public ConsumableManagement(EntityManager em) {
		this.em = em;
	}

	public ConsumableManagement() {

	}
	
	public void storeConsumable(Consumable c) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(c);
		em.getTransaction().commit();
		Logger.getLogger("ConsumableManagement").info("number of Consumable: " + String.valueOf(em.createQuery("select count(c) from Consumable c ").getSingleResult()));
	}
	
	public void removeConsumable(Consumable c) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}

		em.remove(c);
		em.getTransaction().commit();
	}
	
	public Consumable loadConsumable(long id) {
		return em.find(Consumable.class, id);
	}

	public List<Consumable> getAllConsumables() {
		return em.createQuery("from Consumable", Consumable.class).getResultList();

	}


}

