package at.happylab.fablabtool;

import java.io.Serializable;

import at.happylab.fablabtool.model.Gender;

public class SelectOption<T> implements Serializable {
	/**
	 * FIXME currently not working!
	 */
	private static final long serialVersionUID = 1L;
	private T key;
	private String value;

	public SelectOption(T key, String value){
		this.key = key;
		this.value = value;
	}
	
	/*public SelectOption(String key, String value) {
		Gender localkey = Gender.FEMALE;

        for(Gender type : Gender.values()) {
            if(type.toString().equalsIgnoreCase(key)) {
                localkey = type;
                break;
            }
        }

		this.key = localkey;
		this.value = value;
	}*/

	public void setKey(T key){
		this.key = key;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public T getKey(){
		return this.key;
	}
	
	public String getValue(){
		return this.value;
	}
}
