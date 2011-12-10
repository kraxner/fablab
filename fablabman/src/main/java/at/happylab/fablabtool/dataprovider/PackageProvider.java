package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.SelectOption;
import at.happylab.fablabtool.model.Package;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PackageProvider extends SortableDataProvider<Package> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public Iterator<Package> iterator(int first, int count) {
		return em.createQuery("FROM Package").setFirstResult(first)
				.setMaxResults(count).getResultList().iterator();
	}

	public IModel<Package> model(final Package object) {
		return new LoadableDetachableModel<Package>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected Package load() {
				return object;
			}
		};
	}

	public int size() {
		Long count = (Long) em.createQuery("select count(*) from Package")
				.getSingleResult();

		return count.intValue();
	}

	
}