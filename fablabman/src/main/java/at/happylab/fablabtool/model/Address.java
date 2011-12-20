package at.happylab.fablabtool.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable{
	private static final long serialVersionUID = -437232982157469881L;
	
	private String street;
	private String city;
	private String zipCode;
	
	public Address() {
		
	}
	public void assign(Address a) {
		street = a.street;
		city = a.city;
		zipCode = a.zipCode;
	}
	public Address(String street, String city, String zipCode) {
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	

}
