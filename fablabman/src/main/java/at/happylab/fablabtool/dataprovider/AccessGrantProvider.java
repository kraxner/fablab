package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Package;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class AccessGrantProvider extends SortableDataProvider<AccessGrant> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;
	
	

	@SuppressWarnings("unchecked")
	public Iterator<AccessGrant> iterator(int first, int count) {
		return em.createQuery("FROM AccessGrant").setFirstResult(first)
				.setMaxResults(count).getResultList().iterator();
	}

	public IModel<AccessGrant> model(final AccessGrant object) {
		return new LoadableDetachableModel<AccessGrant>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected AccessGrant load() {
				return object;
			}
		};
	}

	public int size() {
		Long count = (Long) em.createQuery("select count(*) from AccessGrant")
				.getSingleResult();

		return count.intValue();
	}

	
}