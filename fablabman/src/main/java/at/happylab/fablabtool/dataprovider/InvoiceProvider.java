package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.Membership;

public class InvoiceProvider extends SortableDataProvider<Invoice> implements Serializable {

	private static final long serialVersionUID = 5913664092473911762L;
	
	@Inject
	private EntityManager em;

	private Membership member;
	
	
	public InvoiceProvider()
	{
		this.member=null;
	}
	
	public void setMember(Membership member)
	{
		this.member=member;
	}
	
	
	@SuppressWarnings("unchecked")
	public Iterator<Invoice> iterator(int first, int count) {
		if(member == null)
			return em.createQuery("FROM Invoice").setFirstResult(first)
					.setMaxResults(count).getResultList().iterator();
		else
			return em.createQuery("FROM Invoice WHERE relatedto_id = " + member.getId()).setFirstResult(first)
					.setMaxResults(count).getResultList().iterator();
	}
	
	public IModel<Invoice> model(final Invoice object) {
		return new LoadableDetachableModel<Invoice>() {
			private static final long serialVersionUID = 2245677208590656096L;
		
			protected Invoice load() {
				return object;
			}
		};
	}
	
	public int size() {
		Long count = 0L;
		
		if(member == null)
		{
			count = (Long) em.createQuery("SELECT count(*) FROM Invoice")
				.getSingleResult();
		}
		else
		{
			count = (Long) em.createQuery("SELECT count(*) FROM Invoice WHERE relatedto_id = " + member.getId())
					.getSingleResult();
		}
		return count.intValue();
	}

}
