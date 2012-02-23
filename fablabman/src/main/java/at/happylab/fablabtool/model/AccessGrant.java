package at.happylab.fablabtool.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.micalo.persistence.IdentifiableEntity;

@Entity
public class AccessGrant extends IdentifiableEntity{
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private DayOfWeek dayOfWeek;
	
	@Temporal(TemporalType.TIME)
	private Date timeFrom;
	
	@Temporal(TemporalType.TIME)	
	private Date timeUntil;
	
	public AccessGrant() {
		
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
