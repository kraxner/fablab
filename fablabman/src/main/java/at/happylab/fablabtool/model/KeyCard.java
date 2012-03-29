package at.happylab.fablabtool.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import net.micalo.persistence.IdentifiableEntity;

@Entity
public class KeyCard extends IdentifiableEntity{
	
	private static final long serialVersionUID = -1408771679299915583L;

	private String rfid;
	private boolean active;
	private String description;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="KEYCARD_ACCESSGRANT",
		joinColumns={@JoinColumn(name="KEYCARD_ID", referencedColumnName="ID")},
		inverseJoinColumns={@JoinColumn(name="ACCESSGRANT_ID", referencedColumnName="ID")})
	private List<AccessGrant> accessgrants = new ArrayList<AccessGrant>();
	
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
