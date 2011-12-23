package at.happylab.fablabtool.converter;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

/**
 * Generic Converter for enums.
 * 
 * {@link IConverter} implementation that makes it easy to work with java 5 enums. 
 * This converter will attempt to lookup strings used for the display value using the given Localizer.
 * <p>
 * display value resource key format: {@code <enum.getSimpleClassName()>.<enum.name()>}
 * </p>
 * <p>
 * id value format: {@code <enum.name()>}
 * </p>
 * 
 * 
 * Important: For drop downs and choice boxes use {@link EnumChoiceRenderer} instead! 
 * 
 * It is based on EnumChoiceRenderer, thanks to igor.vaynberg for that!
 * 
 * @author Michael Kraxner
 *
 * @param <T>
 */
public class EnumConverter<T extends Enum<T>> implements IConverter{
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(EnumConverter.class);
	
	private Class<T> forClass;

	/**
	 * maps translated strings back to corresponding values.
	 * 
	 * FIXME: Not sure if this is a good idea - what happens if the user changes the locale?  
	 */
	private Map<String, T> translateBack = new ConcurrentHashMap<String, T>(); 
	
	/**
	 * component used to resolve i18n resources for this renderer.
	 */
	private final Component resourceSource;
	
	/**
	 * Creates a converter which uses the given <code>localizer</code> for translation of the values.
	 * 
	 * @param forClass
	 * @param resourceSource
	 */
	public EnumConverter(Class<T> forClass, Component resourceSource){
		this.resourceSource = resourceSource;
		this.forClass = forClass; 
	}

	
    public T convertToObject(String value, Locale locale){
    	T result = translateBack.get(value);
    	if (result == null) {
    		result = Enum.valueOf(forClass , value);
    	}
    	return result;
    }
    
    public String convertToString(Object object, Locale locale) {
    	if (!(object instanceof Enum<?>)) {
    		throw new IllegalArgumentException("A non-enum value was passed as argument: " + String.valueOf(object));
    	}
    	@SuppressWarnings("unchecked")
		T enumToConvert = (T)object;
    	
		String key = resourceKey(enumToConvert);
		String value;
		
		try {
			value = resourceSource.getString(key);
		} catch (MissingResourceException e) {
			log.error("enum is missing resource string for : " + key);
			value = enumToConvert.toString(); 
		}
		value = postprocess(value); 
		translateBack.put(value, enumToConvert);
		return value; 
    }
    
    
	/**
	 * Translates the {@code object} into resource key that will be used to lookup the value shown
	 * to the user
	 * 
	 * @param object
	 * @return resource key
	 */
	protected String resourceKey(T object)
	{
		return object.getDeclaringClass().getSimpleName() + "." + object.name();
	}

	/**
	 * Postprocesses the {@code value} after it is retrieved from the localizer. Default
	 * implementation escapes any markup found in the {@code value}.
	 * 
	 * @param value
	 * @return postprocessed value
	 */
	protected String postprocess(String value)
	{
		return Strings.escapeMarkup(value).toString();
	}    
}
