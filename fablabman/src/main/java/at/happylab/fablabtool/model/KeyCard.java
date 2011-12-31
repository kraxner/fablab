package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class KeyCard implements Serializable{
	
	private static final long serialVersionUID = -1408771679299915583L;

	@Id @GeneratedValue
	private long id;
	
	private String rfid;
	private boolean active;
	private String description;
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<AccessGrant> accessgrants = new ArrayList<AccessGrant>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<AccessGrant> getAccessgrants() {
		return accessgrants;
	}
	public void setAccessgrants(List<AccessGrant> accessgrants) {
		this.accessgrants = accessgrants;
	}


}
