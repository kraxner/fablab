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
	
	/**
	 * TODO explain how this should work
	 */
	private int billingCycle;
	
	/**
	 * TODO explain how this should work
	 */
	private int cancelationPeriod;
	
	/**
	 * FIXME what is this, of which type?
	 * TODO explain how this should be used
	 */
	private int cancelationPeriodAdvance;
	
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

	public int getBillingCycle() {
		return billingCycle;
	}

	public void setBillingCycle(int billingCycle) {
		this.billingCycle = billingCycle;
	}

	public int getCancelationPeriod() {
		return cancelationPeriod;
	}

	public void setCancelationPeriod(int cancelationPeriod) {
		this.cancelationPeriod = cancelationPeriod;
	}

	public PackageType getType() {
		return type;
	}

	public void setType(PackageType type) {
		this.type = type;
	}

}
