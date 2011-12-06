package at.happylab.fablabtool;

import org.apache.wicket.settings.IResourceSettings;
import org.jboss.seam.wicket.SeamApplication;

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

}
