package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	public ConsumationEntry createEntry(Date end) {
		
		Calendar cal = Calendar.getInstance();
		int months = 0;
		Date newPayedUntilDate = new Date();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String text = "";
		
		// Sollte des Paket noch nicht aktiv sein, nicht abrechnen
		if (validFrom.after(end)) {
			return null;
		}
		
		// Sollte des Paket schon vor dem End-Datum gekündigt worden sein, nur bis zu dem Kündigungsdatum abrechnen
		if (validTo != null && validTo.before(end)) {
			end = validTo;
		}
		
		if (payedUntil == null) {
			// es wird zum ersten mal abgerechnet
			// daher aliquot (erstes Monat zählt voll) bis Ende der Abrechnungsperiode
			
			cal.setTime(validFrom);
			int validFromMonth = cal.get(Calendar.MONTH);
			int validFromYear  = cal.get(Calendar.YEAR);
			
			cal.setTime(end);
			int calculateUntilMonth = cal.get(Calendar.MONTH);
			int calculateUntilYear  = cal.get(Calendar.YEAR);
			
			int diffInMonths = 0;
			diffInMonths = (calculateUntilYear - validFromYear)*12;
			diffInMonths += (calculateUntilMonth - validFromMonth);
			
			switch (booksPackage.getBillingCycle()) {
				case MONTHLY:
					months = diffInMonths + 1; // erstes Monat wird voll verrechnet
					break;
				
				case QUARTER:
					// auf nächstes Vielfaches von 3 aufrunden
					//months = (3-validFromMonth+1) + (diffInMonths/3)*3;
					break;
				
				case ANNUAL:
					// auf nächstes Vielfaches von 12 aufrunden
					months = (12-validFromMonth) + (calculateUntilYear-validFromYear)*12;
					break;
				
				default:
					break;
			}
			
			// Neues payedUntil Datum (letzer des jeweiligen Monats)
			cal.setTime(validFrom);
			cal.add(Calendar.MONTH, months);
			cal.set(Calendar.DAY_OF_MONTH, 0);
			newPayedUntilDate = cal.getTime();
			
			text = dateFormat.format(validFrom) + " - " + dateFormat.format(newPayedUntilDate);
		}
		else {
			// es wurde bereits abgerechnet
			// daher bis Ende des jeweiligen Monats abrechnen
			
			cal.setTime(payedUntil);
			int payedUntilMonth = cal.get(Calendar.MONTH);
			int payedUntilYear  = cal.get(Calendar.YEAR);

			cal.setTime(end);
			int calculateUntilMonth = cal.get(Calendar.MONTH);
			int calculateUntilYear  = cal.get(Calendar.YEAR);
			
			int diffInMonths = 0;
			diffInMonths = (calculateUntilYear - payedUntilYear)*12;
			diffInMonths += (calculateUntilMonth - payedUntilMonth);

			switch (booksPackage.getBillingCycle()) {
				case MONTHLY:
					months = diffInMonths;
					break;
				
				case QUARTER:
					// auf nächstes Vielfaches von 3 aufrunden
					months = ((diffInMonths+2)/3)*3;
					break;
				
				case ANNUAL:
					// auf nächstes Vielfaches von 12 aufrunden
					months = ((diffInMonths+11)/12)*12;
					break;
				
				default:
					break;
			}
			
			// new payed Until Date
			cal.setTime(payedUntil);
			cal.add(Calendar.MONTH, months);
			newPayedUntilDate = cal.getTime();
			
			cal.setTime(payedUntil);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Date payFromDate = cal.getTime();
			
			text = dateFormat.format(payFromDate) + " - " + dateFormat.format(newPayedUntilDate);
		}
		
		if (months <= 0) {
			return null;
		}
		
		ConsumationEntry entry = new ConsumationEntry();
		entry.setConsumedBy(getBookedBy());
		entry.setText(booksPackage.getName() + " (" + text + ")");
		entry.setDate(new Date());
		entry.setPrice(getPriceOverruled());
		entry.setUnit("Monat");
		entry.setQuantity(months);
		
		setPayedUntil(newPayedUntilDate);
		
		return entry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
