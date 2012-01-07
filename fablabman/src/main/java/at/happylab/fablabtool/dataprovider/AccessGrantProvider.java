package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.AccessGrant;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class AccessGrantProvider extends SortableDataProvider<AccessGrant> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public AccessGrantProvider() {
		setSort("name", true);
	}

	@SuppressWarnings("unchecked")
	public Iterator<AccessGrant> iterator(int first, int count) {
		List<AccessGrant> results = em.createQuery("FROM AccessGrant").getResultList();

		Collections.sort(results, new Comparator<AccessGrant>() {
			public int compare(AccessGrant ag1, AccessGrant ag2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("TimeFrom".equals(getSort().getProperty())) {
					return dir * (ag1.getTimeFrom().compareTo(ag2.getTimeFrom()));
				} else if ("TimeUntil".equals(getSort().getProperty())) {
					return dir * (ag1.getTimeUntil().compareTo(ag2.getTimeUntil()));
				} else if ("DayOfWeek".equals(getSort().getProperty())) {
					return dir * (ag1.getDayOfWeek().compareTo(ag2.getDayOfWeek()));
				} else if ("name".equals(getSort().getProperty())) {
					return dir * (ag1.getName().compareTo(ag2.getName()));
				} else {
					if (ag1.getId() > ag2.getId())
						return dir;
					else
						return -dir;
				}
			}
		});

		return results.subList(first, Math.min(first+count, results.size())).iterator();
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
		Long count = (Long) em.createQuery("select count(*) from AccessGrant").getSingleResult();

		return count.intValue();
	}

}