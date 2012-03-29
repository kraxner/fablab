package at.happylab.fablabtool.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import net.micalo.persistence.IdentifiableEntity;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
//INSERT INTO WEBUSER (username, password, firstname, lastname, admin) values ('mk', 'mk', 'Michael', 'Kraxner', '1')
public class WebUser extends IdentifiableEntity {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
	private String firstname;
	private String lastname;
	
	/**
	 * very simple role management
	 */
	private boolean admin;
	
	public WebUser() {
		setAdmin(false);
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

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
