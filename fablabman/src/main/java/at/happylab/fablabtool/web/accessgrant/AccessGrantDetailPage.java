package at.happylab.fablabtool.web.accessgrant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.StringValidator;
import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.model.AccessGrant;

public class AccessGrantDetailPage extends BasePage {

	@Inject
	private EntityManager em;
	private AccessGrant ag;

	public AccessGrantDetailPage(AccessGrant a) {
		navigation.selectStammdaten();
		ag = a;

		add(new AccessGrantForm("form", a, getDefaultModel()));
	}

	class AccessGrantForm extends Form {
		public AccessGrantForm(String s, AccessGrant k, IModel m) {
			super(s, new CompoundPropertyModel(k));

			final RequiredTextField<String> name = new RequiredTextField<String>(
					"Name");
			name.add(StringValidator.maximumLength(50));
			add(name);

			DropDownChoice<Integer> DayOfWeek = new DropDownChoice<Integer>(
					"DayOfWeek");
			DayOfWeek.setChoices(new LoadableDetachableModel<List<Integer>>() {
				private static final long serialVersionUID = 4420436576098934666L;

				public List<Integer> load() {
					List<Integer> list = new ArrayList<Integer>();
					list.add(Calendar.MONDAY);
					list.add(Calendar.TUESDAY);
					list.add(Calendar.WEDNESDAY);
					list.add(Calendar.THURSDAY);
					list.add(Calendar.FRIDAY);
					list.add(Calendar.SATURDAY);
					list.add(Calendar.SUNDAY);
					return list;
				}
			});
			add(DayOfWeek);

			final DateTextField timeFrom = new DateTextField("TimeFrom" , "HH:mm");
			add(timeFrom);

			final DateTextField timeUntil = new DateTextField("TimeUntil", "HH:mm");
			add(timeUntil);

		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(ag);
			em.getTransaction().commit();

			setResponsePage(new AccessGrantListPage());
		}
	}
}
