package at.happylab.fablabtool.dataprovider.utils;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.PropertyModel;

/**
 * Generic comparator for SortableDataProviders
 * 
 * Note: use this only if sorting in Database is not possible!
 * else consider something like:
 * 
 * 	String orderBy = getSort().getProperty() + " " + (getSort().isAscending()?" ASC " : " DESC ");
 *	List<User> results = em.createQuery("FROM User order by " + orderBy).getResultList();
 * 
 * 
 * @author Michael Kraxner
 *
 * @param <T>
 */
public class SortableDataProviderComparator<T> implements Serializable,
		Comparator<T> {
	private static final long serialVersionUID = 1L;
	
	private SortParam sortParam;
	
	public SortableDataProviderComparator(SortParam sortParam) {
		this.sortParam = sortParam;
	}

	public int compare( T o1, T o2) {
		PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, sortParam.getProperty());
		PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, sortParam.getProperty());

		int result = 0;
		if (model1.getObject() != null) {
			if (model2.getObject() != null) {
				result = model1.getObject().compareTo(model2.getObject());
			} else {
				return 1;
			}
		} else {
			if (model2.getObject() != null) {
				return 0;					
			}
		}

		if (!sortParam.isAscending()) {
			result = -result;
		}
		return result;
	}

}
