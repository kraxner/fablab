package at.happylab.fablabtool;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.convert.ConverterLocator;
import org.jboss.seam.wicket.SeamApplication;
import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.converter.CustomDateConverter;
import at.happylab.fablabtool.web.membership.MembershipListPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class FabLabManApplication extends SeamApplication
{    
	
	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class<MembershipListPage> getHomePage()
	{
		return MembershipListPage.class;
	}
	
    @Override
    protected void init() {
    	super.init();
    	 IResourceSettings resourceSettings = getResourceSettings();
         resourceSettings.addResourceFolder("");    	
    }	
    
    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator converterLocator = new ConverterLocator();
        
        converterLocator.set(BigDecimal.class, new CustomBigDecimalConverter());
        
        // TODO: besser definieren, welche Datumsfelder wo verwendet werden (siehe: http://stackoverflow.com/questions/8064878/global-registration-of-field-formats-in-wicket)
        converterLocator.set(Date.class, new CustomDateConverter());
        converterLocator.set(java.sql.Date.class, new CustomDateConverter());
        converterLocator.set(java.sql.Time.class, new CustomDateConverter());
        converterLocator.set(java.sql.Timestamp.class, new CustomDateConverter());
        
        return converterLocator;
    }

}
