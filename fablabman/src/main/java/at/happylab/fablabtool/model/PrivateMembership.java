package at.happylab.fablabtool.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("private")
public class PrivateMembership extends Membership {
	
	public PrivateMembership() {
		setMaxUser(1);
	}

}
