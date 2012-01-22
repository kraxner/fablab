package at.happylab.fablabtool.web.maintenance;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.beans.WebUserManagement;
import at.happylab.fablabtool.model.WebUser;
import at.happylab.fablabtool.web.BasePage;

public class WebUserAddPage extends BasePage {

	@Inject
	private WebUserManagement webUserMgmt;
	
	private WebUser webUser;

	public WebUserAddPage(WebUser webUser) {
		navigation.selectMasterData();
		this.webUser = webUser;

		if (webUser.getId() == 0)
			add(new Label("pageHeader", "Neuer Administrator"));
		else
			add(new Label("pageHeader", "Administrator bearbeiten"));
		
		add(new FeedbackPanel("feedback"));

		add(new WebUserForm("form", webUser));
		
		
	}

	class WebUserForm extends Form<WebUser> {
		private static final long serialVersionUID = -7376832711919411830L;

		public WebUserForm(String s, WebUser cons) {
			super(s, new CompoundPropertyModel<WebUser>(cons));

			final TextField<String> firstname = new TextField<String>("firstname");
			firstname.setRequired(true);
			firstname.add(StringValidator.maximumLength(50));
			add(firstname);
			
			final TextField<String> lastname = new TextField<String>("lastname");
			lastname.setRequired(true);
			lastname.add(StringValidator.maximumLength(50));
			add(lastname);
			
			final TextField<String> username = new TextField<String>("username");
			username.setRequired(true);
			username.add(StringValidator.maximumLength(50));
			add(username);
			
			final PasswordTextField password = new PasswordTextField("password");
			password.setRequired(true);
			password.add(StringValidator.maximumLength(50));
			add(password);

			final PasswordTextField cpassword = new PasswordTextField("cpassword",Model.of(""));
			cpassword.setRequired(true);
			cpassword.add(StringValidator.maximumLength(50));
			add(cpassword);
			
			add(new EqualPasswordInputValidator(password, cpassword));
			
			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new WebUserListPage());
				}
			};
			add(goBackButton);

		}

		public void onSubmit() {
			webUser.setAdmin(true);
			webUserMgmt.storeWebUser(webUser);
			setResponsePage(new WebUserListPage());
		}
	}
}
