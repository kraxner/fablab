package at.happylab.fablabtool.beans;

import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
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

	public void storeConsumationEntry(ConsumationEntry entry) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(entry);
		em.getTransaction().commit();
		Logger.getLogger("ConsumationEntryManagement").info("number of ConsumationEntry: " + String.valueOf(em.createQuery("select count(e) from ConsumationEntry e").getSingleResult()));
	}

	public void removeEntry(ConsumationEntry entry) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(entry);
		em.getTransaction().commit();
	}
	
	public ConsumationEntry loadConsumationEntry(long id) {
		return em.find(ConsumationEntry.class, id);
	}

}