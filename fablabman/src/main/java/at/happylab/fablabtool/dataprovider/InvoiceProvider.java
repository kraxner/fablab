package at.happylab.fablabtool.dataprovider;

import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.converter.CustomDateConverter;
import at.happylab.fablabtool.dao.InvoiceDAO;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;

/**
 * @author Robert Koch
 *
 */

public class InvoiceProvider extends SortableDataProvider<Invoice> implements Serializable {

	private static final long serialVersionUID = 5913664092473911762L;
	
	@Inject
	private EntityManager em;

	@Inject
	private InvoiceDAO invoiceDAO;
	
	private Membership member;
	
	private Date fromFilter;
	private Date toFilter;
	private String filter;
	private transient List<Invoice> filtered;
	
	public InvoiceProvider()
	{
		setSort("invoiceNumber", true);
		this.member = null;
		initFilter();
	}
	
	/**
	 * This method is used to set the default filter of the invoice panel (of a membership)
	 */
	private void initFilter(){
		this.toFilter = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(toFilter);
		c.add(Calendar.YEAR,-1);
		this.fromFilter = c.getTime();
	}
	
	/**
	 * This method is used to set the default filter of the invoice list page to one month.
	 */
	public void setFilter(){
		this.toFilter = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(toFilter);
		c.add(Calendar.MONTH,-1);
		this.fromFilter = c.getTime();
	}
	
	private String getFrom(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.S");
		return sdf.format(fromFilter);
	}
	
	private String getTo(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.S");
		return sdf.format(toFilter);
	}
	
	public void setMember(Membership member)
	{
		this.member=member;
	}
	
	/**
	 * Returns a sorted iterator. Has to be edited if new columns should be sortable.
	 */
	public Iterator<Invoice> iterator(int first, int count) {
		List<Invoice> data = getFiltered();
		
		Collections.sort(data, new Comparator<Invoice>() {
			public int compare(Invoice o1, Invoice o2) {
				int dir = getSort().isAscending() ? 1 : -1;
				if ("relatedTo.memberId".equals(getSort().getProperty())) {
					return dir * (int)(o1.getRelatedTo().getMemberId() - o2.getRelatedTo().getMemberId());
				} else if("invoiceNumber".equals(getSort().getProperty())) {
					return dir * (int)(o1.getInvoiceNumber().compareTo(o2.getInvoiceNumber()));
				} else if("paymentMethod".equals(getSort().getProperty())) {
					return dir * (o1.getPaymentMethod().compareTo(o2.getPaymentMethod()));
				} else if("date".equals(getSort().getProperty())) {
					return dir * (o1.getDate().compareTo(o2.getDate()));
				} else if("dueDate".equals(getSort().getProperty())) {
					return dir * (o1.getDueDate().compareTo(o2.getDueDate()));
				} else if("payedAt".equals(getSort().getProperty())) {
					if((o1.getPayedAt() != null) && (o2.getPayedAt() != null)){
						return dir * (o1.getPayedAt().compareTo(o2.getPayedAt()));
					}
					return 0;
				} else if("first".equals(getSort().getProperty())) {
				  	return dir * (getFirstName(o1).compareTo(getFirstName(o2)));
				} else if("last".equals(getSort().getProperty())) {
					return dir * (getLastName(o1).compareTo(getLastName(o2)));
				} else if("company".equals(getSort().getProperty())) {
					return dir * (getCompanyName(o1).compareTo(getCompanyName(o2)));
				} else if("amount".equals(getSort().getProperty())) {
					return dir * (getAmount(o1).compareTo(getAmount(o2)));
				} else {
					return dir * (o1.getState().compareTo(o2.getState()));
				}
			}
		});
		return data.subList(first, Math.min(first + count, data.size())).iterator();
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
		return getFiltered().size();
	}
	
	/**
	 * Writes the relevant data for the "Bankdatenträger" in the passed Printwriter in .csv format.
	 * Only invoices with state OPEN and Paymentmethod DEBIT are selected for the export.
	 * If exported this way the state of the invoice is changed to PAID and the payedAt date is set to the
	 * dueDate.
	 * @param writer the Printwriter the data are appended to.
	 */
	public void bankExport(PrintWriter writer){
		List<Invoice> list = em.createQuery("SELECT i FROM Invoice i WHERE paymentmethod='DEBIT' AND state='OPEN'",Invoice.class)
			.getResultList();
		if(list.size() > 0){
			CustomBigDecimalConverter cbdc = new CustomBigDecimalConverter();
			CustomDateConverter cdc = new CustomDateConverter();
			StringBuffer line = new StringBuffer("Mitgl.Nr.;Mitgliedsname;Kontoinhaber;Ktonr.;BLZ;BIC;IBAN;Betrag;Text;AG-Kto.;AG-BLZ;AG-Name;Transakt.Typ;Datum");
			writer.append(line);
			writer.append("\r\n");
			for(Invoice inv : list){
				line = new StringBuffer();
				Membership mem = inv.getRelatedTo();
				line.append(mem.getMemberId()+";");
				line.append(mem.getName()+";");
				line.append(mem.getBankDetails().getName()+";");
				line.append(mem.getBankDetails().getAccountNumber()+";");
				line.append(mem.getBankDetails().getBankCode()+";");
				line.append(mem.getBankDetails().getBic()+";");
				line.append(mem.getBankDetails().getIban()+";");
				line.append(cbdc.convertToString(getAmount(inv), null)+";");
				line.append(inv.getInvoiceNumber()+";");
				line.append("10306777;32000;INNOC/Happylab;EZ;");
				line.append(cdc.convertToString(inv.getDueDate(), null)+";");
				writer.append(line);
				writer.append("\r\n");
				inv.setState(InvoiceState.PAID);
				inv.setPayedAt(inv.getDueDate());
				invoiceDAO.store(inv);
			}
			invoiceDAO.commit();
		}
	}
	
	/**
	 * Writes all the invoice data in the passed Printwriter in .csv format
	 * @param writer the Printwriter the data are appended to.
	 */
	public void fullExport(PrintWriter writer){
		List<Invoice> list = em.createQuery("SELECT i FROM Invoice i",Invoice.class).getResultList();
		if(list.size() > 0){
			CustomBigDecimalConverter cbdc = new CustomBigDecimalConverter();
			CustomDateConverter cdc = new CustomDateConverter();
			StringBuffer line = new StringBuffer("Mitgliedsnummer;Vorname;Nachname;Firma;Rechnungsnummer;Betrag;Zahlungsart;Rechnungsdatum;Fälligkeitsdatum;Zahlungseingangsdatum;Status");
			writer.append(line);
			writer.append("\r\n");
			for(Invoice inv : list){
				line = new StringBuffer();
				Membership mem = inv.getRelatedTo();
				line.append(mem.getMemberId()+";"); // Membership Id
				if(mem.isPrivateMembership()){ // Firstname
					line.append(mem.getUsers().get(0).getFirstname()+";");
				} else {
					line.append(";");
				}
				if(mem.isPrivateMembership()){ // Firstname
					line.append(mem.getUsers().get(0).getLastname()+";");
				} else {
					line.append(mem.getContactPerson()+";");
				}
				if(mem.isPrivateMembership()){ // Companyname
					line.append(";");
				} else {
					line.append(mem.getCompanyName()+";");
				}
				line.append(inv.getInvoiceNumber()+";"); // Invoice number
				line.append(cbdc.convertToString(getAmount(inv), null)+";"); // Invoice Amount
				line.append(inv.getPaymentMethod()+";"); // Payment method
				line.append(cdc.convertToString(inv.getDate(), null)+";"); // Invoice date
				line.append(cdc.convertToString(inv.getDueDate(), null)+";"); // Invoice due date
				line.append(cdc.convertToString(inv.getPayedAt(), null)+";"); // Invoice payed at date
				line.append(inv.getState()); // Invoice state
				writer.append(line);
				writer.append("\r\n");
			}
		}
	}
	
	private List<Invoice> getFiltered() {
		if (filtered == null) {
			filtered = filter();
		}
		return filtered;
	}
	
	/**
	 * Filters all invoices of a given member, if the member is set, or all invoices of all members between
	 * the fromFilter and the toFilter Dates and also by the filter String. 
	 * @return a filtered list of invoices
	 */
	private List<Invoice> filter() {
		List<Invoice> filtered;
		if(member == null)
			filtered = em.createQuery("SELECT i FROM Invoice i WHERE date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'",Invoice.class)
					.getResultList();
		else
			filtered = em.createQuery("SELECT i FROM Invoice i WHERE relatedto_id = " + member.getIdent() + " AND date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'",Invoice.class)
					.getResultList();
		if (filter != null) {
			CustomDateConverter cdc = new CustomDateConverter();
			String upper = filter.toUpperCase();
			Iterator<Invoice> it = filtered.iterator();
			while (it.hasNext()) {
				Invoice inv = it.next();
				if (Long.toString(inv.getRelatedTo().getMemberId()).toUpperCase().indexOf(upper) < 0
					&& getFirstName(inv).toUpperCase().indexOf(upper) < 0
					&& getLastName(inv).toUpperCase().indexOf(upper) < 0
					&& getCompanyName(inv).toUpperCase().indexOf(upper) < 0
					&& inv.getInvoiceNumber().toString().toUpperCase().indexOf(upper) < 0
					&& getAmount(inv).toString().toUpperCase().indexOf(upper) < 0
					&& cdc.convertToString(inv.getDate(), null).toUpperCase().indexOf(upper) < 0
					&& cdc.convertToString(inv.getDueDate(), null).toUpperCase().indexOf(upper) < 0
					&& cdc.convertToString(inv.getPayedAt(), null).toUpperCase().indexOf(upper) < 0)
				{
					it.remove();
				}
			}
		}
		return filtered;
	}
	
	@Override
	public void detach() {
		filtered = null;
		super.detach();
	}
	
	/**
	 * Helper method to get the total amount of an invoice
	 * @param inv the invoice of which the total amount is to be calculated
	 * @return the total amount of the passed invoice
	 */
	private BigDecimal getAmount(Invoice inv){
		BigDecimal sum = new BigDecimal(0);
		for(ConsumationEntry ce : inv.getIncludesConsumationEntries()){
			sum = sum.add(ce.getSum());
		}
		return sum;
	}
	
	/**
	 * Helper method to fill the firstname column
	 * @param inv the invoice of which we want the firstname
	 * @return the firstname of the membership that has to pay the invoice (empty if business membership)
	 */
	private String getFirstName(Invoice inv){
		MembershipType type1 = inv.getRelatedTo().getMembershipType();
		if(type1.equals(MembershipType.PRIVATE)){
			return inv.getRelatedTo().getUsers().get(0).getFirstname();
		} else {
			return "";
		}
	}
	
	/**
	 * Helper method to fill the lastname column
	 * @param inv the invoice of which we want the lastname
	 * @return the lastname of the membership that has to pay the invoice (full name of the contact person
	 *  if business membership)
	 */
	private String getLastName(Invoice inv){
		MembershipType type2 = inv.getRelatedTo().getMembershipType();
		if(type2.equals(MembershipType.PRIVATE)){
			return inv.getRelatedTo().getUsers().get(0).getLastname();
		} else {
			return inv.getRelatedTo().getContactPerson();
		}
	}
	
	/**
	 * Helper method to fill the companyname column
	 * @param inv the invoice of which we want the companyname
	 * @return the name of the company that has to pay the invoice (empty for non-profit membership)
	 */
	private String getCompanyName(Invoice inv){
		MembershipType type1 = inv.getRelatedTo().getMembershipType();
		if(type1.equals(MembershipType.PRIVATE)){
			return "";
		} else {
			return inv.getRelatedTo().getCompanyName();
		}
	}

}
