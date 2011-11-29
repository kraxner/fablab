package at.happylab.fablabtool.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Membership {
	private long id;
	private boolean confirmed;
	private Date entryDate;
	private Date leavingDate;
	private DebitInfo bankDetails;
	private String comment;
	private String internalComment;
	private int maxUser;
	private PaymentMethod paymentMethod;
	private MembershipType type;
}
