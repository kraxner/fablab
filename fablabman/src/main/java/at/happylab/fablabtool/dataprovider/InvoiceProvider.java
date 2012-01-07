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

import org.apache.log4j.Logger;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.converter.CustomDateConverter;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;

public class InvoiceProvider extends SortableDataProvider<Invoice> implements Serializable {

	private static final long serialVersionUID = 5913664092473911762L;
	
	@Inject
	private EntityManager em;

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
	
	
//	@SuppressWarnings("unchecked")
//	public Iterator<Invoice> iterator(int first, int count) {
//		if(member == null)
//			return em.createQuery("SELECT i FROM Invoice i WHERE date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'")
//					.setFirstResult(first).setMaxResults(count).getResultList().iterator();
//		else
//			return em.createQuery("SELECT i FROM Invoice i WHERE relatedto_id = " + member.getId() + " AND date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'")
//					.setFirstResult(first).setMaxResults(count).getResultList().iterator();
//	}
	
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
//				  	MembershipType type1 = o1.getRelatedTo().getMembershipType();
//					String name1;
//					if(type1.equals(MembershipType.PRIVATE)){
//						name1 = o1.getRelatedTo().getUsers().get(0).getFirstname();
//					} else {
//						name1 = "";
//					}
//					MembershipType type2 = o2.getRelatedTo().getMembershipType();
//					String name2;
//					if(type2.equals(MembershipType.PRIVATE)){
//						name2 = o2.getRelatedTo().getUsers().get(0).getFirstname();
//					} else {
//						name2 = "";
//					}
				  	return dir * (getFirstName(o1).compareTo(getFirstName(o2)));
				} else if("last".equals(getSort().getProperty())) {
//					MembershipType type1 = o1.getRelatedTo().getMembershipType();
//					String name1;
//					if(type1.equals(MembershipType.PRIVATE)){
//						name1 = o1.getRelatedTo().getUsers().get(0).getLastname();
//					} else {
//						name1 = o1.getRelatedTo().getContactPerson();
//					}
//					MembershipType type2 = o2.getRelatedTo().getMembershipType();
//					String name2;
//					if(type2.equals(MembershipType.PRIVATE)){
//						name2 = o2.getRelatedTo().getUsers().get(0).getLastname();
//					} else {
//						name2 = o2.getRelatedTo().getContactPerson();
//					}
					return dir * (getLastName(o1).compareTo(getLastName(o2)));
				} else if("company".equals(getSort().getProperty())) {
//					MembershipType type1 = o1.getRelatedTo().getMembershipType();
//					String name1;
//					if(type1.equals(MembershipType.PRIVATE)){
//						name1 = "";
//					} else {
//						name1 = o1.getRelatedTo().getCompanyName();
//					}
//					MembershipType type2 = o2.getRelatedTo().getMembershipType();
//					String name2;
//					if(type2.equals(MembershipType.PRIVATE)){
//						name2 = "";
//					} else {
//						name2 = o2.getRelatedTo().getCompanyName();
//					}
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
	
//	public int size() {
//		Long count = 0L;
//		
//		if(member == null)
//		{
//			count = (Long) em.createQuery("SELECT COUNT(*) FROM Invoice WHERE date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'")
//				.getSingleResult();
//		}
//		else
//		{
//			count = (Long) em.createQuery("SELECT COUNT(*) FROM Invoice WHERE relatedto_id = " + member.getId() + " AND date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'")
//					.getSingleResult();
//		}
//		return count.intValue();
//	}
	
	public int size() {
		return getFiltered().size();
	}
	
	@SuppressWarnings("unchecked")
	public void export(PrintWriter writer){
		List<Invoice> list = em.createQuery("SELECT i FROM Invoice i WHERE paymentmethod='DEBIT' AND state='OPEN'")
		.getResultList();
		if(list.size() > 0){
			StringBuffer line = new StringBuffer("Mitgl.Nr.;Name;Ktonr.;BLZ;BIC;IBAN;Betrag;Text;AG-Kto.;AG-BLZ;AG-Name;Transakt.Typ");
			writer.append(line);
			writer.append("\r\n");
			for(Invoice inv : list){
				line = new StringBuffer();
				Membership mem = inv.getRelatedTo();
				line.append(mem.getMemberId()+";");
				line.append(mem.getName()+";");
				line.append(mem.getBankDetails().getAccountNumber()+";");
				line.append(mem.getBankDetails().getBankCode()+";");
				line.append(mem.getBankDetails().getBic()+";");
				line.append(mem.getBankDetails().getIban()+";");
				line.append(getAmount(inv).toString().replace(".", ",")+";");
				line.append(""+";"); //TODO What Text should be displayed here?
				line.append("10306777;32000;INNOC/Happylab;EZ");
				writer.append(line);
				writer.append("\r\n");
				inv.setState(InvoiceState.PAID);
				storeInvoice(inv);
			}
		}
		writer.flush();
		writer.close();
	}
	
	public void storeInvoice(Invoice inv) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(inv);
		em.getTransaction().commit();
		Logger.getLogger("Invoicemanagement").info("number of Invoices: " + String.valueOf(em.createQuery("select count(i) from Invoice i ").getSingleResult()));
	}
	
	private List<Invoice> getFiltered() {
		if (filtered == null) {
			filtered = filter();
		}
		return filtered;
	}
	
	@SuppressWarnings("unchecked")
	private List<Invoice> filter() {
		CustomDateConverter cdc = new CustomDateConverter();
		List<Invoice> filtered;
		if(member == null)
			filtered = em.createQuery("SELECT i FROM Invoice i WHERE date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'")
					.getResultList();
		else
			filtered = em.createQuery("SELECT i FROM Invoice i WHERE relatedto_id = " + member.getId() + " AND date BETWEEN '" + getFrom() + "' AND '" + getTo() + "'")
					.getResultList();
		if (filter != null) {
			String upper = filter.toUpperCase();
			Iterator<Invoice> it = filtered.iterator();
			while (it.hasNext()) {
				Invoice inv = it.next();
				if (inv.getInvoiceNumber().toString().toUpperCase().indexOf(upper) < 0
					&& cdc.convertToString(inv.getDate(), null).toUpperCase().indexOf(upper) < 0
					&& getAmount(inv).toString().toUpperCase().indexOf(upper) < 0
					&& cdc.convertToString(inv.getDueDate(), null).toUpperCase().indexOf(upper) < 0
					&& Long.toString(inv.getRelatedTo().getMemberId()).toUpperCase().indexOf(upper) < 0
					&& getFirstName(inv).toUpperCase().indexOf(upper) < 0
					&& getLastName(inv).toUpperCase().indexOf(upper) < 0
					&& getCompanyName(inv).toUpperCase().indexOf(upper) < 0)
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
	
	private BigDecimal getAmount(Invoice inv){
		BigDecimal sum = new BigDecimal(0);
		for(ConsumationEntry ce : inv.getIncludesConsumationEntries()){
			sum = sum.add(ce.getSum());
		}
		return sum;
	}
	
	private String getFirstName(Invoice inv){
		MembershipType type1 = inv.getRelatedTo().getMembershipType();
		if(type1.equals(MembershipType.PRIVATE)){
			return inv.getRelatedTo().getUsers().get(0).getFirstname();
		} else {
			return "";
		}
	}
	
	private String getLastName(Invoice inv){
		MembershipType type2 = inv.getRelatedTo().getMembershipType();
		if(type2.equals(MembershipType.PRIVATE)){
			return inv.getRelatedTo().getUsers().get(0).getLastname();
		} else {
			return inv.getRelatedTo().getContactPerson();
		}
	}
	
	private String getCompanyName(Invoice inv){
		MembershipType type1 = inv.getRelatedTo().getMembershipType();
		if(type1.equals(MembershipType.PRIVATE)){
			return "";
		} else {
			return inv.getRelatedTo().getCompanyName();
		}
	}

}
