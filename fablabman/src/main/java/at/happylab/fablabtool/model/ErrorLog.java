package at.happylab.fablabtool.model;

import javax.persistence.Entity;
import javax.persistence.Lob;

import net.micalo.persistence.IdentifiableEntity;

@Entity
public class ErrorLog  extends IdentifiableEntity{
	private static final long serialVersionUID = 1L;
	
	private String comment;
	
	@Lob
	private String cause;
	
	private String user;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
