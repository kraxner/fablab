package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.WebUser;

public class WebUserProvider extends SortableDataProvider<WebUser> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager em;
	
	public WebUserProvider() {
		setSort("name", true);
	}
	
	public Iterator<WebUser> iterator(int first, int count) {
		List<WebUser> results = em.createQuery("FROM WebUser WHERE admin=true",WebUser.class).getResultList();
		return results.subList(first, Math.min(first+count, results.size())).iterator();
	}
	
	public IModel<WebUser> model(final WebUser object) {
		return new LoadableDetachableModel<WebUser>() {
			private static final long serialVersionUID = 2245677208590656096L;
		
			protected WebUser load() {
				return object;
			}
		};
	}
	
	public int size() {
		Long count = (Long) em.createQuery("select count(*) from WebUser WHERE admin=true").getSingleResult();
		return count.intValue();
	}
}
