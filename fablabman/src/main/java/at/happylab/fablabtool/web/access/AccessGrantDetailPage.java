package at.happylab.fablabtool.web.access;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;

import org.apache.wicket.validation.validator.StringValidator;
import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.AccessGrantManagement;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.DayOfWeek;

public class AccessGrantDetailPage extends BasePage {

	@Inject
	private AccessGrant ag;
	
	@Inject
	private AccessGrantManagement accessGrantMgmt;

	public AccessGrantDetailPage(AccessGrant a) {
		navigation.selectStammdaten();
		ag = a;

		add(new AccessGrantForm("form", a));
	}

	class AccessGrantForm extends Form<AccessGrant> {
		private static final long serialVersionUID = 8663869954282100890L;

		public AccessGrantForm(String s, AccessGrant k) {
			super(s, new CompoundPropertyModel<AccessGrant>(k));

			final RequiredTextField<String> name = new RequiredTextField<String>(
					"Name");
			name.add(StringValidator.maximumLength(50));
			add(name);

			DropDownChoice<DayOfWeek> dayOfWeek = new DropDownChoice<DayOfWeek>("DayOfWeek",
					Arrays.asList(DayOfWeek.values()), 
					new EnumChoiceRenderer<DayOfWeek>());
			
			add(dayOfWeek);

			final DateTextField timeFrom = new DateTextField("TimeFrom" , "HH:mm");
			add(timeFrom);

			final DateTextField timeUntil = new DateTextField("TimeUntil", "HH:mm");
			add(timeUntil);
			
		}

		public void onSubmit() {
			accessGrantMgmt.storeAccessGrant(ag);
			setResponsePage(new AccessGrantListPage(null));
		}
	}
}
