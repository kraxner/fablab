package at.happylab.fablabtool.web.access;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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
		this.ag = a;

		if (ag.getId() == 0)
			add(new Label("pageHeader", "Neue Zutrittszeit"));
		else
			add(new Label("pageHeader", "Zutrittszeit bearbeiten"));

		add(new FeedbackPanel("feedback"));

		add(new AccessGrantForm("form"));
	}

	class AccessGrantForm extends Form<AccessGrant> {
		private static final long serialVersionUID = 8663869954282100890L;

		public AccessGrantForm(String s) {
			super(s, new CompoundPropertyModel<AccessGrant>(ag));

			final RequiredTextField<String> name = new RequiredTextField<String>("Name");
			name.add(StringValidator.maximumLength(50));
			add(name);

			DropDownChoice<DayOfWeek> dayOfWeek = new DropDownChoice<DayOfWeek>("DayOfWeek", Arrays.asList(DayOfWeek.values()), new EnumChoiceRenderer<DayOfWeek>());
			dayOfWeek.setRequired(true);
			add(dayOfWeek);

			final DateTextField timeFrom = new DateTextField("TimeFrom", "HH:mm");
			timeFrom.setRequired(true);
			add(timeFrom);

			final DateTextField timeUntil = new DateTextField("TimeUntil", "HH:mm");
			timeUntil.setRequired(true);
			add(timeUntil);

		}

		public void onSubmit() {
			accessGrantMgmt.storeAccessGrant(ag);
			setResponsePage(new AccessGrantListPage(null));
		}
	}
}
