package at.happylab.fablabtool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("extrakey")
public class ExtraKey extends KeyCard implements Serializable {
	private static final long serialVersionUID = -3234000243062893652L;
	
	@OneToMany
	List<AccessGrant> accessGrants = new ArrayList<AccessGrant>();
	
	public ExtraKey(){
		
	}

	public List<AccessGrant> getAccessGrants() {
		return accessGrants;
	}

	public void setAccessGrants(List<AccessGrant> accessGrants) {
		this.accessGrants = accessGrants;
	}

}
