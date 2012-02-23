package at.happylab.fablabtool.dao;

import java.util.Calendar;

import javax.persistence.EntityManager;

import net.micalo.persistence.dao.BaseDAO;
import at.happylab.fablabtool.model.Invoice;

public class InvoiceDAO extends BaseDAO<Invoice> {
	private static final long serialVersionUID = 1L;

	public InvoiceDAO(){
		super(Invoice.class); 
	}
	
	public InvoiceDAO(EntityManager em){
		super(Invoice.class, em); 
	}	
	
	@Override
	protected void onBeforeStore(Invoice invoice) {
		if (invoice.getInvoiceNumberShort() == 0) {
			// Rechnungsnummer im Format YYBNNNN
			Calendar cal = Calendar.getInstance();
			cal.setTime(invoice.getDate());
			int year = cal.get(Calendar.YEAR);
			
			Object result = em.createQuery("select MAX(invoiceNumberShort) from Invoice WHERE year(date) = " + year).getSingleResult();
			long lastInvoiceNumber = 0;
			if (result != null) {
				 lastInvoiceNumber = (Long)result;
			}
					
			invoice.setInvoiceNumberShort(lastInvoiceNumber+1);
		}		
	}	
}
