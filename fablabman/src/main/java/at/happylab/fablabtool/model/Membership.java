package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Membership implements Serializable{
	
	private static final long serialVersionUID = 2316667102976971988L;

	@Id @GeneratedValue
	private long id;
	
	private boolean confirmed;
	
	private Date entryDate;
	
	private Date leavingDate;

	@Embedded
	private DebitInfo bankDetails;
	
	@Embedded
	private Address address;
	
	private String comment;
	
	private String internalComment;
	
	/**
	 * The maximal number of Users allowed for this membership
	 */
	private int maxUser;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	private MembershipType type;

	/**
	 * The users of this membership.
	 * The number of users must not exceed {@link #maxUser}
	 */
	@OneToMany(mappedBy="membership",cascade=CascadeType.ALL)
	private List<User> users = new ArrayList<User>();
	
	public Membership() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getLeavingDate() {
		return leavingDate;
	}

	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

	public DebitInfo getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(DebitInfo bankDetails) {
		this.bankDetails = bankDetails;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInternalComment() {
		return internalComment;
	}

	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}

	public int getMaxUser() {
		return maxUser;
	}

	public void setMaxUser(int maxUser) {
		this.maxUser = maxUser;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public MembershipType getType() {
		return type;
	}

	public void setType(MembershipType type) {
		this.type = type;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

}
