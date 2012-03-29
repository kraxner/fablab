package at.happylab.fablabtool.web.access;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.dao.KeyCardDAO;
import at.happylab.fablabtool.dataprovider.AccessGrantsFromKeycardProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.DateTimeColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.web.BasePage;

public class KeycardDetailPage extends BasePage {

	private KeyCard keycard;

	@Inject private AccessGrantsFromKeycardProvider accessGrantsFromKeycardProvider;

	@Inject private KeyCardDAO keycardDAO;

	public KeycardDetailPage(KeyCard keycard) {
		this.keycard = keycard;

		if (keycard.getId() == 0)
			add(new Label("pageHeader", "Neue Keycard"));
		else
			add(new Label("pageHeader", "Keycard bearbeiten"));
		
		add(new FeedbackPanel("feedback"));
		
		add(new KeycardForm("form"));
	}

	class KeycardForm extends Form<KeyCard> {
		private static final long serialVersionUID = 8638737151881505884L;

		public KeycardForm(String s) {
			super(s, new CompoundPropertyModel<KeyCard>(keycard));

			accessGrantsFromKeycardProvider.setKeyCard(keycard);

			final CheckBox activeYN = new CheckBox("active");
			add(activeYN);

			final TextArea<String> description = new TextArea<String>("description");
			description.setRequired(false);
			add(description);

			final RequiredTextField<String> rfid = new RequiredTextField<String>("rfid");
			rfid.add(StringValidator.maximumLength(50));
			add(rfid);

			// Zutrittszeiten
			List<IColumn<AccessGrant>> columns = new ArrayList<IColumn<AccessGrant>>();
			columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Name"), "name", "name"));
			columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Wochentag"), "DayOfWeek", "DayOfWeek"));
			columns.add(new DateTimeColumn<AccessGrant>(new Model<String>("Von"), "TimeFrom", "HH:mm"));
			columns.add(new DateTimeColumn<AccessGrant>(new Model<String>("Bis"), "TimeUntil", "HH:mm"));
			columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>("Entfernen"), new Model<String>("delete")) {
				private static final long serialVersionUID = -9096631630737840648L;

				@SuppressWarnings("rawtypes")
				@Override
				public void onClick(Item item, String componentId, IModel model) {
					AccessGrant a = (AccessGrant) model.getObject();
					List<AccessGrant> lst = keycard.getAccessgrants();
					lst.remove(a);
					keycard.setAccessgrants(lst);
				}
			});

			DefaultDataTable<AccessGrant> table = new DefaultDataTable<AccessGrant>("AccessGrantsFromKeycardTable", columns, accessGrantsFromKeycardProvider, 20);
			add(table);

			/**
			 * Add new Accessgranttimes to the keycard.
			 */
			Link<String> addAccessGrantToKeyCard = new Link<String>("addAccessGrantToKeyCard") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new AccessGrantListPage(keycard));
				}
			};
			addAccessGrantToKeyCard.setEnabled(keycard.getId() != 0);

			add(addAccessGrantToKeyCard);
			
			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new KeycardListPage());
				}
			};
			add(goBackButton);
			
		}

		public void onSubmit() {
			keycardDAO.store(keycard);
			keycardDAO.commit();
			
			setResponsePage(KeycardListPage.class);
		}
	}
}
