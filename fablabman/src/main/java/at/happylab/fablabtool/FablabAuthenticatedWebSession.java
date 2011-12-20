/**
 * 
 */
package at.happylab.fablabtool;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;

/**
 * @author Michael Kraxner
 *
 */
public class FablabAuthenticatedWebSession extends AuthenticatedWebSession {
	
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FablabAuthenticatedWebSession(Request request) {
		super(request);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(final String username, final String password) {
		return username.equals(password);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles() {
		if (isSignedIn()) {
            // If the user is signed in, they have these roles
            return new Roles(Roles.ADMIN);
        }
        return null;
     }

}
