package at.happylab.fablabtool;

import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.jboss.seam.wicket.SeamRequestCycle;

import at.happylab.fablabtool.web.ErrorPage;

public class FabLabRequestCycle extends SeamRequestCycle {

	public FabLabRequestCycle(WebApplication application, WebRequest request,
			Response response) {
		super(application, request, response);
	}

	@Override
	public Page onRuntimeException(Page page, RuntimeException e) {
		if (e instanceof PageExpiredException) {
			return super.onRuntimeException(page, e);
		} else {
			System.out.println(e.toString());
			return new ErrorPage(e);
		}
	}

}
