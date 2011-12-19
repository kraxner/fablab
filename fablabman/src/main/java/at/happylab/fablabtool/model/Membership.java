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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Membership implements Serializable{
	
	private static final long serialVersionUID = 2316667102976971988L;

	@Id @GeneratedValue
	private long id;
	
	@Enumerated(EnumType.STRING)
	private MembershipType membershipType;
	
	private boolean confirmed;
	
	@Temporal(TemporalType.DATE)
	private Date entryDate;
	
	@Temporal(TemporalType.DATE)
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
	private MembershipStatus type;


	/**
	 * Only available in Business Membership
	 */
	private String companyName;
	/**
	 * Only available in Business Membership
	 */
	private String contactPerson;
	
	private String companyEmail;
	
	private String companyPhone;
	
	/**
	 * The users of this membership.
	 * The number of users must not exceed {@link #maxUser}
	 */
	@OneToMany(mappedBy="membership",cascade=CascadeType.ALL)
	private List<User> users = new ArrayList<User>();
	
	public Membership() {
		
	}
	
	/**
	 * Returns the name associated with this membership
	 * - for business memberships this is the {@link #companyName}
	 * - for non-profit memberships this is the {@link User#getFullname()} of the first user
	 * 
	 * @return
	 */
	public String getName(){
		if (membershipType == MembershipType.BUSINESS) {
			return companyName;
		} else {
			if (users.size()>0) {
				return users.get(0).getFullname();
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Returns the address associated to this membership
	 * - for business memberships this is the 
	 * - for non-profit memberships this is the User#get 
	 * @return
	 */
//	public Address getAddress(){
//		
//	}

	/**
	 * Adjusts the number of {@link #maxUser} according to the type of the membership.
	 * - Currently business memberships may have up to 3 users, private memberships only 1.
	 * - this does not touch the currently defined  {@link #users users}   
	 */
	public void adjustMaxUser() {
		if (membershipType == MembershipType.BUSINESS) {
			maxUser = 3;
		} else {
			maxUser = 1;
		}
	}
	/**
	 * Assigns all values of m to this object.
	 * 
	 * Note: users are not copied at the moment
	 * 
	 * @param m
	 */
	public void assign(Membership m) {
		confirmed = m.confirmed;
		entryDate = new Date(m.entryDate.getTime());
		leavingDate = new Date(m.leavingDate.getTime());
		bankDetails.assign(m.bankDetails);
		address.assign(m.address);
		comment = m.comment;
		internalComment = m.internalComment;
		maxUser = m.maxUser;
		paymentMethod = m.paymentMethod;
		type = m.type;

		// note that the users are not recreated
		users.addAll(m.users);
		
		companyName = m.companyName;
		contactPerson = m.contactPerson;
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

	public MembershipStatus getType() {
		return type;
	}

	public void setType(MembershipStatus type) {
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
	
	/**
	 * adds the user to this membership
	 * 
	 * @param u
	 */
	public void addUser(User u) {
		if ((u.getMembership() != this) &&(u.getMembership() != null)) {
			u.getMembership().removeUser(u);
		}
		u.setMembership(this);
		if (! users.contains(u)) {
			users.add(u);
		}
	}

	public void removeUser(User u) {
		if (users.remove(u)) {
			u.setMembership(null);
		}
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public MembershipType getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(MembershipType membershipType) {
		this.membershipType = membershipType;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
}
