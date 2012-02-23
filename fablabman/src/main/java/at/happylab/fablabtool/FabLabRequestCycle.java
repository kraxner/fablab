package at.happylab.fablabtool;

import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.jboss.seam.wicket.SeamRequestCycle;

import at.happylab.fablabtool.web.ErrorPage;

public class FabLabRequestCycle extends SeamRequestCycle {
	
	
//	@Inject private EntityManager em;
	
	private boolean endConversation;    

	public FabLabRequestCycle(WebApplication application, WebRequest request,
			Response response) {
		super(application, request, response);
//		em = SessionScopeProducer.getInstance().getEm();
	}

	@Override
	public Page onRuntimeException(Page page, RuntimeException e) {
//        if (em != null) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            em.close();
//        }
		
		if (e instanceof PageExpiredException) {
			return super.onRuntimeException(page, e);
		} else {
			System.out.println(e.toString());
			return new ErrorPage(e);
		}
	}
	
	 @Override
	    protected void onEndRequest() {
	        super.onEndRequest();
//	        if (em != null) {
//	            if (em.getTransaction().isActive()) {
//	                em.getTransaction().commit();
//	            }
//	            em.close();
//	        }
	        if (endConversation) {
	            getRequest().getPage().getPageMap().remove();
	        }
	    }


	 public void endConversation() {
	        endConversation = true;
	    }
}
