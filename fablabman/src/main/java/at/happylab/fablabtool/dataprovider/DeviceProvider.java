package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.Device;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class DeviceProvider extends SortableDataProvider<Device> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;
	
	public DeviceProvider() {
		setSort("name", true);
	}

	@SuppressWarnings("unchecked")
	public Iterator<Device> iterator(int first, int count) {
		List<Device> results = em.createQuery("FROM Device").getResultList();

		Collections.sort(results, new Comparator<Device>() {
			public int compare(Device d1, Device d2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("deviceId".equals(getSort().getProperty())) {
					return dir * (d1.getDeviceId().compareTo(d2.getDeviceId()));
				} else if ("name".equals(getSort().getProperty())) {
					return dir * (d1.getName()).compareTo(d2.getName());
				} else {
					if (d1.getId() > d2.getId())
						return dir;
					else
						return -dir;
				}
			}
		});

		return results.subList(first, Math.min(first+count, results.size())).iterator();
	}

	public IModel<Device> model(final Device object) {
		return new LoadableDetachableModel<Device>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected Device load() {
				return object;
			}
		};
	}

	public int size() {
		Long count = (Long) em.createQuery("select count(*) from Device")
				.getSingleResult();

		return count.intValue();
	}

	
}