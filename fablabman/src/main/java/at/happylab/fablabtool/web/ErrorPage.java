package at.happylab.fablabtool.web;

import javax.persistence.EntityManager;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.model.ErrorLog;
import at.happylab.fablabtool.session.FablabAuthenticatedWebSession;
import at.happylab.fablabtool.session.SessionScopeProducer;
import at.happylab.fablabtool.web.membership.MembershipListPage;

/**
 * Error page which is shown on RuntimeExceptions.
 *  
 * @author Michael Kraxner
 *
 */
public class ErrorPage extends BasePage {
	
	class ErrorForm extends Form<ErrorLog> {
		private static final long serialVersionUID = 1L;
		
		private ErrorLog errorLog;

		public ErrorForm(String id, RuntimeException e) {
			super(id);
			
			errorLog = new ErrorLog();
			if (e != null) {
				errorLog.setCause(e.getMessage());
			}
			
			add(new Label("errormessageLabel",  new PropertyModel<ErrorLog>(errorLog, "cause")));
			add(new TextArea<ErrorLog>("commentInput", new PropertyModel<ErrorLog>(errorLog, "comment")));
			add(new Button("submitButton"));
			
	        if ((getSession() instanceof FablabAuthenticatedWebSession) ) {
	        	SessionScopeProducer sessionScopeProducer = ((FablabAuthenticatedWebSession)getSession()).getSessionScopeProducer();
	        	if (sessionScopeProducer != null) {
	        		errorLog.setUser(sessionScopeProducer.getLoggedInUser().getUsername());
	        	}
	        }
		}
		
        @Override
        public void onSubmit() {
    		EntityManager em = null;
            if (getSession() instanceof FablabAuthenticatedWebSession) {
            	FablabAuthenticatedWebSession fablabSession = (FablabAuthenticatedWebSession)getSession();
            	if (fablabSession.getSessionScopeProducer() != null) {
            		em = fablabSession.getSessionScopeProducer().getEm();
            	}
            } 
            if (em == null){
            	em = SessionScopeProducer.getInstance().getEm();
            }
            if (! em.getTransaction().isActive()) {
            	em.getTransaction().begin();
            }
            em.persist(em.merge(errorLog));
            
            em.getTransaction().commit();
            setResponsePage(new MembershipListPage());
        	
        }
		
	}
	
	public ErrorPage(RuntimeException e) {
		add(new ErrorForm("form", e));
	}

}
