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
import at.happylab.fablabtool.model.Membership;

public class ConsumationEntryProvider extends SortableDataProvider<ConsumationEntry> implements Serializable {

	private static final long serialVersionUID = 2931124304229390924L;

	@Inject
	private EntityManager em;

	private IModel<Membership> membershipModel;
	private IModel<Invoice> invoiceModel;
	
	public ConsumationEntryProvider()
	{
		this.invoiceModel = null;
		this.membershipModel = null;
	}
	
	public void setInvoiceModel(IModel<Invoice> model)
	{
		this.invoiceModel = model;
	}
	
	public void setMembershipModel(IModel<Membership> model) {
		this.membershipModel = model;
	}
	
	
	@SuppressWarnings("unchecked")
	public Iterator<ConsumationEntry> iterator(int first, int count) {
		if(invoiceModel == null && membershipModel == null) {
			return em.createQuery("FROM ConsumationEntry").setFirstResult(first)
					.setMaxResults(count).getResultList().iterator();
		}
		else {
			if (invoiceModel != null) {
				return invoiceModel.getObject().getIncludesConsumationEntries().iterator();
			}
			else {
				return em.createQuery("FROM ConsumationEntry WHERE invoice_id is NULL AND consumedBy_id = " + membershipModel.getObject().getIdent()).setFirstResult(first)
						.setMaxResults(count).getResultList().iterator();
			}
		}
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
		
		if(invoiceModel == null && membershipModel == null) {
			count = (Long) em.createQuery("SELECT count(*) FROM ConsumationEntry")
				.getSingleResult();
		} 
		else {
			if (invoiceModel != null) {
				count = (long) invoiceModel.getObject().getIncludesConsumationEntries().size();
			}
			else {
				count = (Long) em.createQuery("SELECT count(*) FROM ConsumationEntry WHERE consumedBy_id = " + membershipModel.getObject().getId())
						.getSingleResult();
			}
		}
		return count.intValue();
	}

}
