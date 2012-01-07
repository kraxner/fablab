package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.Consumable;

public class ConsumableProvider extends SortableDataProvider<Consumable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public Iterator<Consumable> iterator(int first, int count) {
	return em.createQuery("FROM Consumable").setFirstResult(first)
			.setMaxResults(count).getResultList().iterator();
	}
	
	public IModel<Consumable> model(final Consumable object) {
	return new LoadableDetachableModel<Consumable>() {
		private static final long serialVersionUID = 2245677208590656096L;
	
		protected Consumable load() {
			return object;
		}
	};
	}
	
	public int size() {
	Long count = (Long) em.createQuery("select count(*) from Consumable")
			.getSingleResult();
	
	return count.intValue();
	}

}
