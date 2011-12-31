package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Package implements Serializable{
	
	private static final long serialVersionUID = 355872449251526129L;
	
	public Package() {
		
	}
	
	public Package(String n) {
		name = n;
	}
	
	@Id @GeneratedValue
	private long id;
	
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
