package at.happylab.fablabtool.dataprovider.utils;

import java.io.Serializable;

public class FilteredField implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fieldname;
	
	public FilteredField(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getSQLToStringExpr() {
		return fieldname;
	}
}
