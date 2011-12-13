package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;

public class ConsumationEntryProvider extends SortableDataProvider<ConsumationEntry> implements Serializable {

	private static final long serialVersionUID = 2931124304229390924L;

	@Inject
	private EntityManager em;

	private Invoice inv;
	
	
	public ConsumationEntryProvider()
	{
		this.inv=null;
	}
	
	public void setInvoice(Invoice inv)
	{
		this.inv=inv;
	}
	
	
	@SuppressWarnings("unchecked")
	public Iterator<ConsumationEntry> iterator(int first, int count) {
		if(inv == null)
			return em.createQuery("FROM consumationentry").setFirstResult(first)
					.setMaxResults(count).getResultList().iterator();
		else
			return inv.getIncludesConsumationEntries().iterator();
	}
	
	public IModel<ConsumationEntry> model(final ConsumationEntry object) {
		return new LoadableDetachableModel<ConsumationEntry>() {
			private static final long serialVersionUID = 2245677208590656096L;
		
			protected ConsumationEntry load() {
				return object;
			}
		};
	}
	
	public int size() {
		Long count = 0L;
		
		if(inv == null)
		{
			count = (Long) em.createQuery("SELECT count(*) FROM consumationentry")
				.getSingleResult();
		}
		else
		{
			count = (long) inv.getIncludesConsumationEntries().size();
		}
		return count.intValue();
	}

}
