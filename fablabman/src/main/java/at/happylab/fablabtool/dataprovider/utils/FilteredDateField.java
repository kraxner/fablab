package at.happylab.fablabtool.dataprovider.utils;


public class FilteredDateField extends FilteredField{
	private static final long serialVersionUID = 1L;
	
	public FilteredDateField(String fieldname) {
		super(fieldname);
	}
	public String getSQLToStringExpr() {
		return "to_char(" + fieldname + ",  'DD.MM.YYYY')";
	}

}
