package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 5102948341740922385L;

	@Id
	@GeneratedValue
	private long id;
	
	private String username;
	private String password;
	
	private String firstname;
	private String lastname;
	
	private String email;
	private String mobile;
	private String fax;
	
	/**
	 * TODO decide if we really want to use a Date type, 
	 *      or if we want to use a string representation instead (which would allows us to handle exotic birthdays) 
	 */
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
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
	
	public User(){
		
	}
	
	public String getFullname(){
		
		String name = lastname;
		if (name != null) {
			if (firstname != null) {
				name = firstname + " " + name;
			}
		} else {
			name = firstname;
		}
		return name;
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
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

}
