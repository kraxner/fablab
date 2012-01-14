package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
	
	public ConsumableProvider() {
		setSort("name", true);
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<Consumable> iterator(int first, int count) {
		List<Consumable> results = em.createQuery("FROM Consumable").getResultList();

		Collections.sort(results, new Comparator<Consumable>() {
			public int compare(Consumable c1, Consumable c2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("name".equals(getSort().getProperty())) {
					return dir * (c1.getName().compareTo(c2.getName()));
				} else if ("pricePerUnit".equals(getSort().getProperty())) {
					return dir * (c1.getPricePerUnit().compareTo(c2.getPricePerUnit()));
				} else if ("unit".equals(getSort().getProperty())) {
					return dir * (c1.getUnit().compareTo(c2.getUnit()));
				} else {
					if (c1.getId() > c2.getId())
						return dir;
					else
						return -dir;
				}
			}
		});

		return results.subList(first, Math.min(first+count, results.size())).iterator();
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
