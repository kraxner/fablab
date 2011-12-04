package at.happylab.fablabtool;

import java.io.Serializable;

public class SelectOption implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;

	public SelectOption(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public void setKey(String key){
		this.key = key;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public String getValue(){
		return this.value;
	}
}
