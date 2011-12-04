package at.happylab.fablabtool.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DebitInfo implements Serializable{
	private static final long serialVersionUID = 2723095130207733502L;
	
	@Column(name="debitinfo_name")
	private String name;
	private String iban;
	private String bic;
	
	public DebitInfo() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}

}
