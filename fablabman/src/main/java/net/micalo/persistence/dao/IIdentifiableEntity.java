package net.micalo.persistence.dao;

/**
 * Interface for objects which are identifiable by an id
 *  
 * @author micalo
 *
 * @param <T> type of the id
 */
public interface IIdentifiableEntity<T> {
	public T getIdent();
	public void setIdent(T id);
	
}
