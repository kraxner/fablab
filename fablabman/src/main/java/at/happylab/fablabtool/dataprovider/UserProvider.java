/**
 * 
 */
package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.User;

/**
 * @author micalo
 *
 */
public class UserProvider extends SortableDataProvider<User> implements
		Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private EntityManager em;

	@SuppressWarnings("unchecked")
	public Iterator<? extends User> iterator(int first, int count) {
		return em.createQuery("FROM User")
				.setFirstResult(first).setMaxResults(count)
				.getResultList().iterator();
	}

	public int size() {
		Long count=(Long) em.createQuery("select count(*) from User").getSingleResult();
		return count.intValue();
	}

	public IModel<User> model(final User object) {
		return new LoadableDetachableModel<User>() {
			private static final long serialVersionUID = 2245677208590656096L;
			protected User load() {
				return object;
			}
		};
	}
}
