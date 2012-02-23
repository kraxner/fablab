package at.happylab.fablabtool.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import net.micalo.persistence.IdentifiableEntity;

@Entity
public class Consumable extends IdentifiableEntity{
	private static final long serialVersionUID = 1L;

	private String name;
	private BigDecimal pricePerUnit;
	private String unit;
	
	public Consumable() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String toString() {
		return getName();
	}

}
