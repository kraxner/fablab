package at.happylab.fablabtool.web;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;

/**
 * Homepage
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	private String rfid;

	// TODO Add any page properties or variables here
	public HomePage() {
		add(new Label("message", "Hello world, happy lab user!"));
		
		Form<String> form = new Form<String>("main");

		final Component inputRfId = new TextField<Serializable>("inputRfid",
				new Model<Serializable>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return rfid;
					}

					@Override
					public void setObject(Serializable object) {
						if (object == null) {
							rfid = "";
						} else {
							rfid = object.toString();
						}
					}
				});
		
		form.add(inputRfId);

		Component checkLink = new ExternalLink("checkAccess",
				"services/keycard//access", "Check Access");
		checkLink.setOutputMarkupId(true);
		form.add(checkLink);

		final Component updateButton = new AjaxButton("updateLink") {

			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form<?> arg1) {
				String url = "services/keycard/" + rfid + "/access";
				Component newLink = new ExternalLink("checkAccess", url,
						"Check Access for " + rfid).setOutputMarkupId(true);
				arg1.addOrReplace(newLink);
				arg0.addComponent(arg1);
			}
		};
		form.add(updateButton);
		add(form);
	}

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public HomePage(final PageParameters parameters) {

		// Add the simplest type of label
		add(new Label("message", "Hello world, happy lab user!"));

		// TODO Add your page's components here
	}

}
