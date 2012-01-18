/**
 * 
 */
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

import at.happylab.fablabtool.model.UserAdvanced;

/**
 * @author micalo
 *
 */
public class UserProviderAdvanced extends SortableDataProvider<UserAdvanced> implements
		Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private EntityManager em;
	
	public UserProviderAdvanced(){
		setSort("id", true);
	}

	@SuppressWarnings("unchecked")
	public Iterator<? extends UserAdvanced>iterator(int first, int count) {
		List<UserAdvanced> results = em.createQuery("FROM User").getResultList();
		
		if ((getSort() == null) || (getSort().getProperty() == null)) {
			setSort("id", true);
		}
		
		Collections.sort(results , new Comparator<UserAdvanced>() {
			public int compare(UserAdvanced o1, UserAdvanced o2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("firstname".equals(getSort().getProperty())) {
					String m1Val = o1 != null ? o1.getFirstname() : null;
					String m2Val = o2 != null ? o2.getFirstname() : null;
					return dir * (compareStrings(m1Val, m2Val));
				} else if ("lastname".equals(getSort().getProperty())) {
					String m1Val = o1 != null ? o1.getLastname() : null;
					String m2Val = o2 != null ? o2.getLastname() : null;
					return dir * (compareStrings(m1Val, m2Val));
				} else if ("username".equals(getSort().getProperty())) {
					String m1Val = o1.getMembership() != null ? o1.getMembership().getCompanyName() : null;
					String m2Val = o2.getMembership() != null ? o2.getMembership().getCompanyName() : null;
					return dir * (compareStrings(m1Val, m2Val));
				} else {
					if (o1.getId() > o2.getId())
						return dir;
					else
						return -dir;
				}
				
			}
			private int compareStrings(String a, String b) {
				if ((a == null) && (b == null)) {
					return 0;
				}
				if ((a != null) && (b == null)){
					return 1;
				} else if ((a == null) && (b != null)){
					return -1;
				}
				return b.compareTo(a);
			}
		});
		return results.subList(first, Math.min(first+count, results.size())).iterator();
	}

	public int size() {
		Long count=(Long) em.createQuery("select count(*) from User").getSingleResult();
		return count.intValue();
	}

	public IModel<UserAdvanced> model(final UserAdvanced object) {
		return new LoadableDetachableModel<UserAdvanced>() {
			private static final long serialVersionUID = 2245677208590656096L;
			protected UserAdvanced load() {
				return object;
			}
		};
	}
}
