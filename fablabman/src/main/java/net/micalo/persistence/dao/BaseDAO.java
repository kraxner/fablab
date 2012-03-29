package net.micalo.persistence.dao;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.IIdentifiableEntity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for data access objects which can handle entities implementing {@link IIdentifiableEntity}
 * 
 * @author Michael Kraxner
 * @param <T>
 *
 */
public class BaseDAO<T extends IIdentifiableEntity<?>> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(BaseDAO.class);
	
	@Inject protected EntityManager em;
	
	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDAO() {
		if (entityClass == null) {
			this.entityClass = (Class<T>) ((ParameterizedType) getClass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}
	
	/**
	 * Creates a DAO for the given entityClass
	 * Connection to persistence layer is retrieved via injection
	 *  
	 * @param entityClass
	 */
	public BaseDAO(Class<T> entityClass){
		this.entityClass = entityClass;
	}
	
	/**
	 * Creates a DAO for the given entityClass, and uses the provided <param>em</param> for persistence   
	 *  
	 * @param entityClass
	 * @param em
	 */
	public BaseDAO(Class<T> entityClass, EntityManager em){
		this.entityClass = entityClass;
		this.em = em;
	}
	/**
	 * Stores the given entity
	 * Note:
	 *  - Starts a transaction, if none is active
	 *  - Does NOT commit transaction after storing the entity
	 *  
	 * @param entity
	 */
	public void store(T entity){
		onBeforeStore(entity);
		
		if (! em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		
		em.persist(entity);
	}
	
    public T load(Serializable id) {
    	log.info("load: entityClass = " + String.valueOf(entityClass));
    	return em.find(entityClass, id);
    }
    
	public void remove(T entity) {
    	log.info("removing entity " + String.valueOf(entityClass) + " id: " + String.valueOf(entity.getIdent()));
    	em.remove(entity);
    }
    
    public List<T> getAll() {
    	return em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
    }
    
    public void commit(){
    	if (em.getTransaction().isActive()) {
        	log.debug("commiting transaction: " + em.toString()); 
    		em.getTransaction().commit();
    	}
    }
    
    /**
     * Called before storing the entity.
     * Implement this method with custom behavior, such as cleaning up values or for validation tasks. 
     */
    protected void onBeforeStore(T entity) {
    	
    }

}
