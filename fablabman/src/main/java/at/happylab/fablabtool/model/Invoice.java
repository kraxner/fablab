package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Invoice implements Serializable{
	private static final long serialVersionUID = -3982998472620768008L;

	@Id @GeneratedValue
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	
	private String recipient;
	
	@Embedded
	private Address address;
	
	/**
	 * TODO explain: what is the difference to {@link #date}
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date generatedAt;
	
	
	@Temporal(TemporalType.DATE)
	private Date settlementPeriodFrom;

	@Temporal(TemporalType.DATE)
	private Date settlementPeriodTo;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date payedAt;
	
	private String comment;
	
	/**
	 * FIXME find a better name for this relationship 
	 */
	@ManyToOne
	private Membership relatedTo;
	
	@OneToMany(mappedBy="invoice", cascade=CascadeType.ALL)
	private List<ConsumationEntry> includesConsumationEntries;
	
	@Enumerated(EnumType.STRING)
	private InvoiceState state;
	
	private long invoiceNumberShort = 0;
	
	public Invoice(){
		includesConsumationEntries = new ArrayList<ConsumationEntry>();
		state = InvoiceState.OPEN;
		
		date = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 14); // TODO: getting real due dates.
		dueDate = cal.getTime();
	}
	
	public Invoice (Membership member) {
		this();
		
		setRelatedTo(member);
	
/*		
<<<<<<< HEAD
		// Copy default values from member
		setRecipient(member.getName());
		setPaymentMethod(member.getPaymentMethod());
		//setAddress(new Address(member.getAddress()));
=======
*/
		// copy default values from member
		recipient = member.getName();
		address = member.getAddress();
		paymentMethod = member.getPaymentMethod();
		
		date = new Date();
		state = InvoiceState.OPEN;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(Date generatedAt) {
		this.generatedAt = generatedAt;
	}

	public Date getSettlementPeriodFrom() {
		return settlementPeriodFrom;
	}

	public void setSettlementPeriodFrom(Date settlementPeriodFrom) {
		this.settlementPeriodFrom = settlementPeriodFrom;
	}

	public Date getSettlementPeriodTo() {
		return settlementPeriodTo;
	}

	public void setSettlementPeriodTo(Date settlementPeriodTo) {
		this.settlementPeriodTo = settlementPeriodTo;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Date getPayedAt() {
		return payedAt;
	}

	public void setPayedAt(Date payedAt) {
		this.payedAt = payedAt;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Membership getRelatedTo() {
		return relatedTo;
	}

	public void setRelatedTo(Membership relatedTo) {
		this.relatedTo = relatedTo;
	}

	public List<ConsumationEntry> getIncludesConsumationEntries() {
		return includesConsumationEntries;
	}

	public void setIncludesConsumationEntries(List<ConsumationEntry> includesConsumationEntries) {
		
		// remove old Entries
		final List<ConsumationEntry> clone = new ArrayList<ConsumationEntry>(includesConsumationEntries);
		for (ConsumationEntry e : clone) {
			removeConsumationEntry(e);
		}
		
		// add new Entries
		for (ConsumationEntry e : includesConsumationEntries) {
			addConsumationEntry(e);
		}
	}
	
	public void addConsumationEntry(ConsumationEntry e) {
		getIncludesConsumationEntries().add(e);
		e.setInvoice(this);
	}
	
	public void removeConsumationEntry(ConsumationEntry e) {
		getIncludesConsumationEntries().remove(e);
		e.setInvoice(null);
	}
	
	public InvoiceState getState() {
		return state;
	}
	
	public void setState(InvoiceState state) {
		this.state = state;
	}
	
	public String getInvoiceNumber() {
		DecimalFormat format = new DecimalFormat("0000");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
		
		return dateFormat.format(date) + "B" + format.format(invoiceNumberShort);
	}

	public long getInvoiceNumberShort() {
		return invoiceNumberShort;
	}

	public void setInvoiceNumberShort(long invoiceNumberShort) {
		this.invoiceNumberShort = invoiceNumberShort;
	}
}
