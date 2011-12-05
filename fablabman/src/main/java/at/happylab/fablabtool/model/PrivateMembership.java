package at.happylab.fablabtool.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("private")
public class PrivateMembership extends Membership implements Serializable{
	
	private static final long serialVersionUID = -5213302148775205451L;

	public PrivateMembership() {
		setMaxUser(1);
		addUser(new User());
	}
	
	public String getName() {
		if (getUsers().isEmpty()) {
			return null;
		}
		User u = getUsers().get(0);
		if (u != null){
			return u.getFullname();
		}
		return null;
			
	}
	public void setName(String name){
		
	}

}
