package net.micalo.persistence;

import java.io.Serializable;

/**
 * Interface for objects which are identifiable by an id
 *  
 * @author micalo
 *
 * @param <T> type of the id
 */
public interface IIdentifiableEntity<T extends Serializable> {
	public T getIdent();
	public void setIdent(T id);
	
}
