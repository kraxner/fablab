package net.micalo.wicket.model;

import java.io.Serializable;

import net.micalo.persistence.IIdentifiableEntity;
import net.micalo.persistence.dao.BaseDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * A model which is able to detach model objects and reload them again when required.
 * 
 * @author Michael Kraxner
 *
 * @param <T>
 */
public class SmartModel<T extends IIdentifiableEntity<?>> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(SmartModel.class);
	
	/**
	 * data access object used to reload the entity
	 */
	private BaseDAO<T> dao;
	
	/**
	 * Identifier of the entity - it will be serialized, so the entity can be reloaded again when required
	 * Note: the transient instance of the entity itself is managed by  {@link LoadableDetachableModel } 
	 */
	private Serializable ident;
	
	
	public SmartModel(BaseDAO<T> dao, T entity) {
		this.dao = dao;
		
		setObject(entity);
	}

	/**
	 * loads the entity via the set data access object, if an ident is set
	 */
	@Override
	protected T load() {
		if (ident != null) {
			log.debug("loading entity: " + String.valueOf(ident));
			T entity = dao.load(ident);
			if (entity != null) {
				return entity;
			} 
		}
		throw new EntityNotFoundException("id: " + String.valueOf(ident));
	}
	
	/**
	 * Reloads the entity.
	 * 
	 * @see LoadableDetachableModel#onAttach()
	 */
	@Override
	protected void onAttach(){
		T entity = getObject();
		ident = entity.getIdent();
	}
	
	/**
	 * Detaches the entity, keeps the {@link #ident identifier} for later use.
	 * @see LoadableDetachableModel#onDetach()
	 */
	@Override
	protected void onDetach(){
		log.debug("onDetach: " + String.valueOf(ident));
		T entity = getObject();
		
		if (entity.getIdent() != null) {
			// if there is an object, we have to store the identifier for reloading the instance later on 
			ident = entity.getIdent();
		}
	}
	
	/**
	 * @see LoadableDetachableModel#onDetach()
	 */
	@Override
	public void detach() {
		if (isAttached()) {
			if (ident != null) {
				log.debug("detaching: " + String.valueOf(getObject()));
				super.detach();
			}
		}
	}
	
	@Override
	public void setObject(final T object) {
		super.setObject(object);
		ident = object.getIdent();
	}
}
