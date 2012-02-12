package net.micalo.persistence.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for data access objects which can handle entities implementing {@link IIdentifiableEntity}
 * 
 * @author micalo
 * @param <T>
 *
 */
public class BaseDAO<T extends IIdentifiableEntity<I>, I> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(BaseDAO.class);
	
	@Inject private EntityManager em;
	
	protected Class<T> entityClass;
	
	public BaseDAO(){
	}
	public BaseDAO(EntityManager em){
		this.em = em;
	}
		
	public void store(T entity){
		em.persist(entity);
	}
	
    public T load(I id) {
    	log.info("load: entityClass = " + String.valueOf(entityClass));
    	return em.find(entityClass, id);
    }
    
    public void delete(T entity) {
    	log.info("removing entity " + String.valueOf(entityClass) + " id: " + String.valueOf(entity.getIdent()));
    	em.remove(entity);
    }

}
