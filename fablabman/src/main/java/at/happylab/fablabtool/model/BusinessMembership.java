package at.happylab.fablabtool.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("business")
public class BusinessMembership extends Membership implements Serializable{
	private static final long serialVersionUID = 85182599585916002L;
	
	private String name;
	private String contactPerson;
	private String phone;
	
	public BusinessMembership() {
		setMaxUser(3);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
