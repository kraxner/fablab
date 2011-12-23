package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User extends WebUser implements Serializable {

	private static final long serialVersionUID = 5102948341740922385L;

	private String email;
	private String mobile;
	private String fax;
	
	
	/**
	 * TODO decide if we really want to use a Date type, 
	 *      or if we want to use a string representation instead (which would allows us to handle exotic birthdays) 
	 */
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	private String birthplace;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private BigDecimal rfidKeyDeposit;
	
	/**
	 * The rfid Keycard of the user
	 * at the moment a user can only have one keycard 
	 */
	@OneToOne
	private KeyCard keyCard;
	
	@ManyToOne
	private Membership membership;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Device> trainedForDevices = new ArrayList<Device>();

	private String phone;
	
	@Embedded
	private Address address;
	
	public User(){
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public BigDecimal getRfidKeyDeposit() {
		return rfidKeyDeposit;
	}

	public void setRfidKeyDeposit(BigDecimal rfidKeyDeposit) {
		this.rfidKeyDeposit = rfidKeyDeposit;
	}

	public KeyCard getKeyCard() {
		return keyCard;
	}

	public void setKeyCard(KeyCard keyCard) {
		this.keyCard = keyCard;
	}

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	public List<Device> getTrainedForDevices() {
		return trainedForDevices;
	}

	public void setTrainedForDevices(List<Device> trainedForDevices) {
		this.trainedForDevices = trainedForDevices;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
