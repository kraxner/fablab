package at.happylab.fablabtool.converter;

import java.text.NumberFormat;
import java.util.Locale;
import org.apache.wicket.util.convert.converters.DoubleConverter;


public class CustomDoubleConverter extends DoubleConverter {

    @Override
    public Double convertToObject(String value, Locale locale) {
        // NB: this isn't universal & your mileage problably varies!
        // (Specifically, this breaks if '.' is used as thousands separator)
    	
       value = value.replace('.', ',');
       return super.convertToObject(value, Locale.GERMAN);
    }
}