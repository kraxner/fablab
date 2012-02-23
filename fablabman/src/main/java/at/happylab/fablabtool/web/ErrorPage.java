package at.happylab.fablabtool.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.EntityManagerProducer;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

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
	
	@Inject EntityManager em;

	public ErrorPage(RuntimeException e) {
		add(new ErrorForm("form", e));
	}
	
	class ErrorForm extends Form<ErrorLog> {
		private static final long serialVersionUID = 1L;
		
		private ErrorLog errorLog;

		public ErrorForm(String id, RuntimeException e) {
			super(id);
			
			errorLog = new ErrorLog();
			if (e != null) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
			    errorLog.setCause(sw.toString());
			}
			
//			add(new Label("errormessageLabel",  new PropertyModel<ErrorLog>(errorLog, "cause")));
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
            if (em == null){
            	em = EntityManagerProducer.createContextualEntityManager();
            }
            if (! em.getTransaction().isActive()) {
            	em.getTransaction().begin();
            }
            em.persist(em.merge(errorLog));
            
            em.getTransaction().commit();
            setResponsePage(new MembershipListPage());
        }
	}
}
