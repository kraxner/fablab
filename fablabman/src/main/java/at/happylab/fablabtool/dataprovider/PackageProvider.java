package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.SelectOption;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.Package;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PackageProvider extends SortableDataProvider<Package> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;
	
	public PackageProvider() {
		setSort("name", true);
	}

	@SuppressWarnings("unchecked")
	public Iterator<Package> iterator(int first, int count) {
		List<Package> results = em.createQuery("FROM Package").getResultList();

		Collections.sort(results, new Comparator<Package>() {
			public int compare(Package p1, Package p2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("name".equals(getSort().getProperty())) {
					return dir * (p1.getName().compareTo(p2.getName()));
				} else if ("price".equals(getSort().getProperty())) {
					return dir * (p1.getPrice().compareTo(p2.getPrice()));
				} else if ("type".equals(getSort().getProperty())) {
					return dir * (p1.getPackageType().compareTo(p2.getPackageType()));
				} else if ("billingCycle".equals(getSort().getProperty())) {
					return dir * (p1.getBillingCycle().compareTo(p2.getBillingCycle()));
				} else if ("cancelationPeriodAdvance".equals(getSort().getProperty())) {
					return dir * (p1.getCancelationPeriodAdvance().compareTo(p2.getCancelationPeriodAdvance()));
				} else if ("cancelationPeriod".equals(getSort().getProperty())) {
					return dir * (new Integer(p1.getCancelationPeriod()).compareTo(new Integer(p2.getCancelationPeriod())));
				} else {
					if (p1.getId() > p2.getId())
						return dir;
					else
						return -dir;
				}
			}
		});

		return results.subList(first, Math.min(first+count, results.size())).iterator();
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