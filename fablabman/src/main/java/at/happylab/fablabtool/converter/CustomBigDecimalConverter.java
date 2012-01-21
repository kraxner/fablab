package at.happylab.fablabtool.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.wicket.util.convert.converters.BigDecimalConverter;


public class CustomBigDecimalConverter extends BigDecimalConverter {

	private static final long serialVersionUID = 1L;

	@Override
    public BigDecimal convertToObject(String value, Locale locale) {
        // NB: this isn't universal & your mileage problably varies!
        // (Specifically, this breaks if '.' is used as thousands separator)
    	
       value = value.replace('.', ',');
       return super.convertToObject(value, Locale.GERMAN);
    }
    
    @Override
    public String convertToString(Object value, Locale locale) {
        NumberFormat fmt = getNumberFormat(Locale.GERMAN);
        fmt.setMaximumFractionDigits(2); // By default this is 3.
        fmt.setMinimumFractionDigits(2);
       
        return fmt.format(value);
    }
}