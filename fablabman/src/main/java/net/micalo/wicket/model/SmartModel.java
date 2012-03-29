package net.micalo.wicket.model;

import java.io.Serializable;

import net.micalo.persistence.IIdentifiableEntity;
import net.micalo.persistence.dao.BaseDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.LoadableDetachableModel;

public class SmartModel<T extends IIdentifiableEntity<?>> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(SmartModel.class);
	
	private BaseDAO<T> dao;
	
	private Serializable ident;
	
	public SmartModel(BaseDAO<T> dao, T entity) {
		this.dao = dao;
		
		setObject(entity);
	}

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
	
	@Override
	protected void onAttach(){
		T entity = getObject();
		ident = entity.getIdent();
	}
	
	@Override
	protected void onDetach(){
		log.debug("onDetach: " + String.valueOf(ident));
		T entity = getObject();
		
		if (entity.getIdent() != null) {
			ident = entity.getIdent();
		}
	}
	
	@Override
	public void detach() {
		if (isAttached()) {
			if (ident != null) {
				log.debug("detaching: " + getObject().toString());
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
