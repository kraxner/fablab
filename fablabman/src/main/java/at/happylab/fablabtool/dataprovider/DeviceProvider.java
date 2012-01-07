package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.Device;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Package;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class DeviceProvider extends SortableDataProvider<Device> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public Iterator<Device> iterator(int first, int count) {
		return em.createQuery("FROM Device").setFirstResult(first)
				.setMaxResults(count).getResultList().iterator();
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