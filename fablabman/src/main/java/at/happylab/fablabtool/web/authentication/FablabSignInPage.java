/**
 * 
 */
package at.happylab.fablabtool.web.authentication;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authentication.pages.SignInPage;

/**
 * @author Michael Kraxner
 */
public final class FablabSignInPage extends SignInPage {
	
	public FablabSignInPage(){
		super();
		
	}
	
	public FablabSignInPage(final PageParameters parameters) {
		super(parameters);
	}
}
