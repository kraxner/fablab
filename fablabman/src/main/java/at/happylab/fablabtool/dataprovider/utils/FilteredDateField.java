package at.happylab.fablabtool.dataprovider.utils;

import java.io.Serializable;

public class FilteredDateField extends FilteredField implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public FilteredDateField(String fieldname) {
		super(fieldname);
	}
	public String getSQLToStringExpr() {
		return "to_char(entrydate,  'DD.MM.YYYY')";
	}

}
