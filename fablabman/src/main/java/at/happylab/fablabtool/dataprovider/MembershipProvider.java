package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.Membership;

public class MembershipProvider extends SortableDataProvider<Membership> implements
		Serializable {

	private static final long serialVersionUID = 8746292597567302072L;
	
	@Inject private EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	public Iterator<Membership> iterator(int first, int count) {
		return em.createQuery("FROM Membership")
				.setFirstResult(first).setMaxResults(count)
				.getResultList().iterator();
	}

	public IModel<Membership> model(final Membership object) {
		return new LoadableDetachableModel<Membership>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected Membership load() {
				return object;
			}
		};
	}

	public int size() {
		Long count=(Long) em.createQuery("select count(*) from Membership").getSingleResult();
		return count.intValue();
	}

}
