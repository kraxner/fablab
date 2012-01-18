package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.KeyCard;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class KeycardProvider extends SortableDataProvider<KeyCard> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public KeycardProvider() {
		setSort("rfid", true);
	}

	@SuppressWarnings("unchecked")
	public Iterator<KeyCard> iterator(int first, int count) {
		List<KeyCard> results = em.createQuery("FROM KeyCard").getResultList();

		Collections.sort(results, new Comparator<KeyCard>() {
			public int compare(KeyCard k1, KeyCard k2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("rfid".equals(getSort().getProperty())) {
					if (k1.getRfid() == null)
						k1.setRfid("");
					if (k2.getRfid() == null)
						k2.setRfid("");
					
					return dir * (k1.getRfid().compareTo(k2.getRfid()));
				} else {
					if (k1.getId() > k2.getId())
						return dir;
					else
						return -dir;
				}
			}
		});

		return results.subList(first, Math.min(first + count, results.size())).iterator();
	}

	public IModel<KeyCard> model(final KeyCard object) {
		return new LoadableDetachableModel<KeyCard>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected KeyCard load() {
				return object;
			}
		};
	}

	public int size() {
		Long count = (Long) em.createQuery("select count(*) from KeyCard").getSingleResult();

		return count.intValue();
	}

}