package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;


import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.EntityManagerProducer;
import at.happylab.fablabtool.model.User;

public class UserProvider extends SortableDataProvider implements Serializable{
	
	private static final long serialVersionUID = 2361648224662400716L;

	@Inject
	private EntityManager em;
	
	private List<User> list = new ArrayList<User>();
	
		class SortableDataProviderComparator implements Comparator<User>, Serializable {
			private static final long serialVersionUID = -2398295952601030952L;

			public int compare(final User o1, final User o2) {
				PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
				PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());

				int result = model1.getObject().compareTo(model2.getObject());

				if (!getSort().isAscending()) {
					result = -result;
				}

				return result;
			}

		}

		private SortableDataProviderComparator comparator = new SortableDataProviderComparator();

		public UserProvider() {
//			EntityManagerProducer emProducer = new EntityManagerProducer();
//			em = emProducer.getEm();
			// The default sorting
			//setSort("name.first", true);
			User u = new User();
			u.setUsername("mkraxner");
			u.setPassword("geheim");
			em.persist(u);
		}

		public Iterator<User> iterator(final int first, final int count) {
			// In this example the whole list gets copied, sorted and sliced; in real applications typically your database would deliver a sorted and limited list 

			// Get the data
			list = em.createQuery("select u from User u").getResultList();

			// Sort the data
			//Collections.sort(newList, comparator);

			// Return the data for the current page - this can be determined only after sorting
			return list.iterator();//newList.subList(first, first + count).iterator();
		}

		public IModel<User> model(final Object object) {
			return new AbstractReadOnlyModel<User>() {
				@Override
				public User getObject() {
					return (User) object;
				}
			};
		}

		public int size() {
			return list.size();
		}

}

