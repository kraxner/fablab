/**
 * 
 */
package at.happylab.fablabtool.web.authentication;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authentication.pages.SignInPage;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

import at.happylab.fablabtool.BasePage;

/**
 * @author Michael Kraxner
 *
 */
public final class FablabSignInPage extends SignInPage {
	
	public FablabSignInPage(){
		super();
		
	}
	
	public FablabSignInPage(final PageParameters parameters) {
		super(parameters);
	}
}
