package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Subscription implements Serializable{
	private static final long serialVersionUID = -624431396684560629L;

	@Id @GeneratedValue
	private long id;
	
	private BigDecimal priceOverruled;
	
	@Temporal(TemporalType.DATE)
	private Date validFrom;

	@Temporal(TemporalType.DATE)
	private Date validTo;
	
	@Temporal(TemporalType.DATE)
	private Date payedUntil;
	
	private String description;

	@Embedded
	private PaymentMethod paymentMethod;
	
	@ManyToOne
	private Package booksPackage;
	
	@ManyToOne
	private Membership bookedBy;

	public Subscription(){
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getPriceOverruled() {
		return priceOverruled;
	}

	public void setPriceOverruled(BigDecimal priceOverruled) {
		this.priceOverruled = priceOverruled;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Date getPayedUntil() {
		return payedUntil;
	}

	public void setPayedUntil(Date payedUntil) {
		this.payedUntil = payedUntil;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Package getBooksPackage() {
		return booksPackage;
	}

	public void setBooksPackage(Package booksPackage) {
		this.booksPackage = booksPackage;
	}

	public Membership getBookedBy() {
		return bookedBy;
	}

	public void setBookedBy(Membership bookedBy) {
		this.bookedBy = bookedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
