package at.happylab.fablabtool.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import net.micalo.persistence.IdentifiableEntity;

@Entity
public class Package extends IdentifiableEntity{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private BigDecimal price;
	
	@Enumerated(EnumType.STRING)
	private TimePeriod billingCycle;
	
	private int cancelationPeriod;
	
	@Enumerated(EnumType.STRING)
	private TimePeriod cancelationPeriodAdvance;
	
	@Enumerated(EnumType.STRING)
	private PackageType type;

	public Package() {
		
	}
	
	public Package(String n) {
		name = n;
	}	
	
	
	/**
	 * Methode liefert das nächstmögliche Kündigungsdatum eines Paketes zurück.
	 * 
	 * @return das errechnete Kündigungsdatum
	 * 
	 * @author Johannes Bauer
	 */
	public Date getNextCancelationDate() {
		return getNextCancelationDate(new GregorianCalendar());
	}

	public Date getNextCancelationDate(GregorianCalendar c) {

		/**
		 * Zum aktuellen Datum die Kündigungsfrist in Monaten dazuzählen.
		 */
		c.add(Calendar.MONTH, getCancelationPeriod());

		/**
		 * Kündigung nur zu bestimmten Zeiten möglich nur am Ende von
		 * Monat/Quartal/Jahr
		 */
		if (getCancelationPeriodAdvance() == TimePeriod.MONTHLY) {
			//c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));

		} else if (getCancelationPeriodAdvance() == TimePeriod.QUARTER) {

			if (c.get(Calendar.MONTH) <= Calendar.MARCH)
				c.set(Calendar.MONTH, Calendar.MARCH);
			else if (c.get(Calendar.MONTH) <= Calendar.JUNE)
				c.set(Calendar.MONTH, Calendar.JUNE);
			else if (c.get(Calendar.MONTH) <= Calendar.SEPTEMBER)
				c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			else if (c.get(Calendar.MONTH) <= Calendar.DECEMBER)
				c.set(Calendar.MONTH, Calendar.DECEMBER);

		} else if (getCancelationPeriodAdvance() == TimePeriod.ANNUAL) {
			c.set(Calendar.MONTH, Calendar.DECEMBER);
		}

		/**
		 * Kündigung immer nur am letzten Tag eines Monats
		 */
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));

		return c.getTime();
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public TimePeriod getBillingCycle() {
		return billingCycle;
	}

	public void setBillingCycle(TimePeriod billingCycle) {
		this.billingCycle = billingCycle;
	}

	public int getCancelationPeriod() {
		return cancelationPeriod;
	}

	public void setCancelationPeriod(int cancelationPeriod) {
		this.cancelationPeriod = cancelationPeriod;
	}

	public PackageType getPackageType() {
		return type;
	}

	public void setPackageType(PackageType type) {
		this.type = type;
	}
	
	public String toString() {
		return this.name;
	}

	public TimePeriod getCancelationPeriodAdvance() {
		return cancelationPeriodAdvance;
	}

	public void setCancelationPeriodAdvance(TimePeriod cancelationPeriodAdvance) {
		this.cancelationPeriodAdvance = cancelationPeriodAdvance;
	}

}
