package at.happylab.fablabtool.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.wicket.util.convert.converters.BigDecimalConverter;


public class CustomBigDecimalConverter extends BigDecimalConverter {

    @Override
    public BigDecimal convertToObject(String value, Locale locale) {
        // NB: this isn't universal & your mileage problably varies!
        // (Specifically, this breaks if '.' is used as thousands separator)
    	
    	System.out.println(locale.getLanguage());
    	
        if ("de".equals(locale.getLanguage())) {
            value = value.replace('.', ',');
        }
        return super.convertToObject(value, locale);
    }
    
    @Override
    public String convertToString(Object value, Locale locale) {
        NumberFormat fmt = getNumberFormat(locale);
        fmt.setMaximumFractionDigits(2); // By default this is 3.
        return fmt.format(value);
    }
}