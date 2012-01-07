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
	private String accountNumber;
	private String bankCode;
	
	public DebitInfo() {
		name = "";
		iban = "";
		bic = "";
		accountNumber = "";
		bankCode = "";
	}
	public void assign(DebitInfo d) {
		name = d.name;
		iban = d.iban;
		bic = d.bic;
		accountNumber = d.accountNumber;
		bankCode = d.bankCode;
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
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

}
