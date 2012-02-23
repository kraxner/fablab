package at.happylab.fablabtool.model;

import javax.persistence.Entity;

import net.micalo.persistence.IdentifiableEntity;

@Entity
public class Device extends IdentifiableEntity{
	private static final long serialVersionUID = 5826674586901952593L;

	private String deviceId;
	private String name;
	private String description;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	
	

}
