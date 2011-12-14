package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.Invoice;

public class InvoiceManagement implements Serializable{

	private static final long serialVersionUID = 3931879103811519930L;

	@Inject	private EntityManager em;
	
	
	public InvoiceManagement(EntityManager em)
	{	
		this.em = em;
	}
	
	
	public InvoiceManagement()
	{	

	}
	
	public void storeInvoice(Invoice inv) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(inv);
		em.getTransaction().commit();
		Logger.getLogger("Invoicemanagement").info("number of Invoices: " + String.valueOf(em.createQuery("select count(i) from Invoice i ").getSingleResult()));
	}
	
	/**
	 * Removes an invoice and all dependent objects
	 * 
	 * @param member
	 */
	public void removeInvoice(Invoice inv) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(inv);
		em.getTransaction().commit();
	}
	
	public Invoice loadInvoice(long id) {
		return em.find(Invoice.class, id);
	}

}
