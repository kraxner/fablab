package at.happylab.fablabtool;

import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.convert.ConverterLocator;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.wicket.SeamComponentInstantiationListener;
import org.jboss.seam.wicket.SeamRequestCycle;
import org.jboss.seam.wicket.SeamWebRequestCycleProcessor;
import org.jboss.seam.wicket.util.NonContextual;

import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.converter.CustomDateConverter;
import at.happylab.fablabtool.session.FablabAuthenticatedWebSession;
import at.happylab.fablabtool.web.authentication.FablabSignInPage;
import at.happylab.fablabtool.web.membership.MembershipListPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class FabLabManApplication extends AuthenticatedWebApplication
{
	   private NonContextual<SeamComponentInstantiationListener> seamComponentInstantiationListener;
	   private NonContextual<SeamWebRequestCycleProcessor> seamWebRequestCycleProcessor;

	   /**
	    * Add our component instantiation listener
	    * 
	    * @see SeamComponentInstantiationListener
	    */
	   @Override
	   protected void internalInit() 
	   {
	      super.internalInit();
	      BeanManager manager = new BeanManagerLocator().getBeanManager();
	      this.seamComponentInstantiationListener = NonContextual.of(SeamComponentInstantiationListener.class,manager);
	      this.seamWebRequestCycleProcessor = NonContextual.of(getWebRequestCycleProcessorClass(),manager);
	      addComponentInstantiationListener(seamComponentInstantiationListener.newInstance().produce().inject().get());
	   }
	   
	   protected Class<? extends SeamWebRequestCycleProcessor> 
	   getWebRequestCycleProcessorClass() 
	   { 
	      return SeamWebRequestCycleProcessor.class;
	   }

	   /**
	    * Override to return our Seam-specific request cycle processor
	    * 
	    * @see SeamWebRequestCycleProcessor
	    */
	   @Override
	   protected IRequestCycleProcessor newRequestCycleProcessor()
	   {
	      return seamWebRequestCycleProcessor.newInstance().produce().inject().get();
	   }

	   /**
	    * Override to return our Seam-specific request cycle
	    * 
	    * @see SeamRequestCycle
	    */
	   @Override
	   public RequestCycle newRequestCycle(final Request request, final Response response)
	   {
	      return new SeamRequestCycle(this, (WebRequest) request, (WebResponse) response);
	   }
	   
	   @Override
	    protected Class<? extends AuthenticatedWebSession> getWebSessionClass()
	    {
	        return FablabAuthenticatedWebSession.class;
	    }

	    @Override
	    protected Class<? extends WebPage> getSignInPageClass()
	    {
	        return FablabSignInPage.class;
	    }

	    /**
	     * @see org.apache.wicket.Application#getHomePage()
	     */
	    @Override
	    public Class<? extends Page> getHomePage()
	    {
	        return MembershipListPage.class;
	    }

	    /**
	     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#init()
	     */
	    @Override
	    protected void init()
	    {
	        super.init();
	        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
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
