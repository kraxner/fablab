package at.happylab.fablabtool.dataprovider.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterExpressionBuilder implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<FilteredField> filteredFields;
	
	public FilterExpressionBuilder(){
		filteredFields = new ArrayList<FilteredField>();
	}
	
	public void addFilteredTextField(String fieldname) {
		filteredFields.add(new FilteredField(fieldname));
	}
	public void add(FilteredField field) {
		filteredFields.add(field);
	}
	
	public String getSQLFilterExpression(){
		 String result = "";
		 if (!filteredFields.isEmpty()) {
			 result = result + " (concat( ";
			 for (FilteredField field : filteredFields) {
				 String fieldExpr = field.getSQLToStringExpr();
				 result = result + " COALESCE("+ fieldExpr + ", ''), ',' ,";
			 }
	         result = result + "'') LIKE '%s' )";
		 }
         return result;
	}
	
}
