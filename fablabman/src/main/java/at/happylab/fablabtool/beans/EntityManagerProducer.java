package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EntityManagerProducer implements Serializable{
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("FabLabMan");
	
	@Produces
	public EntityManager getEm(){
		return emf.createEntityManager();
	}
	
	

}
