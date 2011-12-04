package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Factory for producing an EntityManager instance
 *   
 * @author Michael Kraxner
 *
 */
public class EntityManagerProducer implements Serializable{
	
	private static final long serialVersionUID = 8103333652627698382L;
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("fablabman");
	
	/**
	 * Responsible for creating an EntityManager instance
	 * 
	 * @return
	 */
	@Produces
	public EntityManager getEm(){
		emf = Persistence.createEntityManagerFactory("fablabman");
		return emf.createEntityManager();
	}
	
	

}
