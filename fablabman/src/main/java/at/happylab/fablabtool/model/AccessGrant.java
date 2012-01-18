package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AccessGrant implements Serializable {
	
	private static final long serialVersionUID = -6434558465566036904L;

	@Id @GeneratedValue
	private long id;
	
	private String name;
	
	private DayOfWeek dayOfWeek;
	
	@Temporal(TemporalType.TIME)
	private Date timeFrom;
	
	@Temporal(TemporalType.TIME)	
	private Date timeUntil;
	
	public AccessGrant() {
		
	}
	
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
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public Date getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}
	public Date getTimeUntil() {
		return timeUntil;
	}
	public void setTimeUntil(Date timeUntil) {
		this.timeUntil = timeUntil;
	}
	
}
