package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ConsumationEntry implements Serializable{
	private static final long serialVersionUID = -8117193946412652454L;

	@Id @GeneratedValue
	private long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private String text;
	private double quantity;
	private BigDecimal price;
	private String unit;
	
	@ManyToOne
	private Consumable consumedItem;
	
	@ManyToOne
	private Membership consumedBy;
	
	@ManyToOne(optional=true)
	private Invoice invoice;

	// initialize with default values
	public ConsumationEntry() {
		quantity = 1;
		date = new Date();
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getSum() {
		return price.multiply(BigDecimal.valueOf(quantity));
	}

	public Consumable getConsumedItem() {
		return consumedItem;
	}

	public void setConsumedItem(Consumable consumedItem) {
		this.consumedItem = consumedItem;
	}

	public Membership getConsumedBy() {
		return consumedBy;
	}

	public void setConsumedBy(Membership consumedBy) {
		this.consumedBy = consumedBy;
	}
	
	public Invoice getInvoice() {
		return invoice;
	}
	
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public void setUnit(String u) {
		this.unit = u;
	}
	
	public String getUnit() {
		return this.unit;
	}

}
