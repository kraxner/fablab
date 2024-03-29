package at.happylab.fablabtool.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.wicket.util.NonContextual;

import at.happylab.fablabtool.model.WebUser;

/**
 * Class responsible for creating/providing session scoped beans.
 * - entity manager
 * - currently logged in user
 *  
 * @author Michael Kraxner
 *
 */
@SessionScoped
public class SessionScopeProducer implements Serializable{
	private static final long serialVersionUID = 1L;


	private WebUser user = null;
	
	public static SessionScopeProducer getContextualInstance() {
		NonContextual<SessionScopeProducer> sessionScopeProducerRef;
		
	    BeanManager manager = new BeanManagerLocator().getBeanManager();
		sessionScopeProducerRef = NonContextual.of(SessionScopeProducer.class, manager);

		return sessionScopeProducerRef.newInstance().produce().inject().get(); 		
	}
	
//	/**
//	 * Responsible for creating an EntityManager instance
//	 * 
//	 * @return
//	 */
//	@Produces @SessionScoped
//	public EntityManager getEm(){
//		//emf = Persistence.createEntityManagerFactory("fablabman");
//		return emf.createEntityManager();
//	}
	
	
	
//	public void destroy(@Disposes EntityManager em) {
//		em.close();
//	}
//	
	/**
	 * Provides the currently logged in user
	 * @return
	 */
	@Produces @LoggedIn
	public WebUser getLoggedInUser(){
		return user;
	}
	
	public void setLoggedInUser(WebUser user) {
		this.user = user;
	}
}
