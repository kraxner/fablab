package net.micalo.persistence;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.wicket.util.NonContextual;

@ConversationScoped
public class EntityManagerProducer implements Serializable {
	private static final long serialVersionUID = 1L;

	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("fablabman");
	
	private static Log log = LogFactory.getLog(EntityManagerProducer.class);

//	@PersistenceUnit
//	EntityManagerFactory entityManagerFactory;

	@Produces
	@ConversationScoped
	public EntityManager createEntityManager() {
		log.debug("Creating entity manager");
		EntityManager em = entityManagerFactory.createEntityManager();
		log.debug("Created entity manager: " + em.toString());
		return em;
		
	}

	public void closeEntityManager(@Disposes EntityManager em) {
		log.debug("Closing entity manager: " + em.toString());
		em.close();
	}
	
	public static EntityManager createContextualEntityManager(){
		log.debug("Creating contextual entity manager");
		NonContextual<EntityManagerProducer> entityManagerProducerRef;
		
	    BeanManager manager = new BeanManagerLocator().getBeanManager();
	    entityManagerProducerRef = NonContextual.of(EntityManagerProducer.class, manager);

		EntityManager em = entityManagerProducerRef.newInstance().produce().inject().get().createEntityManager();
		log.debug("Created entity manager: " + em.toString());
		return em; 		
	}
}
