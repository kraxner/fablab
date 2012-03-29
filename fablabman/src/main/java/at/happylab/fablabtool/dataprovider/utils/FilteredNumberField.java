package at.happylab.fablabtool.dataprovider.utils;

public class FilteredNumberField extends FilteredField {
	private static final long serialVersionUID = 1L;

	public FilteredNumberField(String fieldname) {
		super(fieldname);
	}
	
	public String getSQLToStringExpr() {
		return "CONVERT(" + fieldname + " , SQL_VARCHAR )";
	}
}
