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
	
	/**
	 * TODO: what kind of text?
	 */
	private String text;
	
	private int quantity;
	
	private BigDecimal price;
	
	@ManyToOne
	private Consumable consumedItem;
	
	@ManyToOne
	private Membership consumedBy;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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
	

}
