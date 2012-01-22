package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.WebUser;

public class WebUserManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public WebUserManagement(EntityManager em) {
		this.em = em;
	}

	public WebUserManagement() {

	}
	
	public void storeWebUser(WebUser w) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(w);
		em.getTransaction().commit();
		Logger.getLogger("ConsumableManagement").info("number of Consumable: " + String.valueOf(em.createQuery("select count(c) from Consumable c ").getSingleResult()));
	}
	
	public void removeWebUser(WebUser w) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(w);
		em.getTransaction().commit();
	}
	
	public WebUser loadConsumable(long id) {
		return em.find(WebUser.class, id);
	}

	public List<WebUser> getAllWebUsers() {
		return em.createQuery("from WebUser WHERE admin=true", WebUser.class).getResultList();
	}
}
