package net.micalo.wicket.model;

import javax.inject.Inject;

import net.micalo.persistence.dao.BaseDAO;
import net.micalo.persistence.dao.IIdentifiableEntity;

import org.apache.wicket.model.LoadableDetachableModel;

public class SmartModel<T extends IIdentifiableEntity<I>, I> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 1L;
	
	@Inject private BaseDAO<T, I> dao;
	
	private I ident;

	@Override
	protected T load() {
		if (ident != null) {
			return dao.load(ident);
		} else {
			return null;
		}
	}

}
