package at.happylab.fablabtool.dataprovider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class AccessGrantsFromKeycardProvider extends
		SortableDataProvider<AccessGrant> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	private KeyCard kc;

	public void setKeyCard(KeyCard k) {
		this.kc = k;
	}

	public Iterator<AccessGrant> iterator(int first, int count) {
		
		return this.kc.getAccessgrants().iterator();
		
//		return em
//				.createQuery(
//						"FROM KeyCard_AccessGrant WHERE KeyCard_ID="
//								+ kc.getId()).setFirstResult(first)
//				.setMaxResults(count).getResultList().iterator();
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
		return kc.getAccessgrants().size();
	}

}