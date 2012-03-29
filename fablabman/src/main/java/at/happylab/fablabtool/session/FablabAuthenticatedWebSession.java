/**
 * 
 */
package at.happylab.fablabtool.session;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.micalo.persistence.EntityManagerProducer;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;

import at.happylab.fablabtool.model.WebUser;

/**
 * Class responsible for user authentication for a session.
 * After successful authentication it provides the logged in user to other beans.
 *  
 * @author Michael Kraxner
 */
public class FablabAuthenticatedWebSession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 1L;
	
	@Inject private SessionScopeProducer sessionScopeProducer;
	
	public FablabAuthenticatedWebSession(Request request) {
		super(request);
	}
	
	/** 
	 * Tries authentication with the given credentials.
	 *  
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(final String username, final String password) {
		try {
			sessionScopeProducer = SessionScopeProducer.getContextualInstance();
			EntityManager em = EntityManagerProducer.createContextualEntityManager();
			
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			
			// sic! sql injection is a topic for us!
			Query qry = em.createQuery("select u from WebUser u where u.username=:username and u.password = :password");
			qry.setParameter("username", username);
			qry.setParameter("password", password);
			
			Object result = qry.getSingleResult();
			em.close();
			if (result != null) {
				sessionScopeProducer.setLoggedInUser((WebUser)result);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Returns an initialized instance of the SessionScopedProducer
	 * For beans which are created manually (using the new operator), and therefore injection is not available  
	 * 
	 * @return
	 */
	public SessionScopeProducer getSessionScopeProducer(){
		return sessionScopeProducer;
	}

	/** 
	 * Returns the roles of the currently logged in user
	 * 
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles() {
		if (isSignedIn() && (sessionScopeProducer != null)) {
            // If the user is signed in, they have these roles
			if (sessionScopeProducer.getLoggedInUser().isAdmin()) {
				return new Roles(Roles.ADMIN);
			} else {
				return new Roles(Roles.USER);
			}
        }
        return null;
    }
	

}
