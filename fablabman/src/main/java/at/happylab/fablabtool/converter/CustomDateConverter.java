package at.happylab.fablabtool.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.util.convert.converters.DateConverter;

public class CustomDateConverter extends DateConverter {

	private static final long serialVersionUID = 6244421578144655386L;
	
	@Override
    public Date convertToObject(String value, Locale locale) {
		return super.convertToObject(value, Locale.GERMAN);
    }
	
	@Override
    public String convertToString(Object value, Locale locale) {
		if(value == null){
			return "";
		}
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		format.setLenient(true);
		
		return format.format(value);
    }

}
