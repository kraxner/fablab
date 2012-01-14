package at.happylab.fablabtool.web.access;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.AccessGrantManagement;
import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.dataprovider.AccessGrantProvider;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.DayOfWeek;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.panels.EnumPropertyColumn;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.util.CheckBoxColumn;
import at.happylab.fablabtool.web.util.DateTimeColumn;

public class AccessGrantListPage extends BasePage {

	@Inject
	AccessGrantProvider accessGrantProvider;
	@Inject
	AccessGrantManagement accessGrantMgmt;

	@Inject
	KeycardManagement keycardMgmt;

	private KeyCard keycard;

	/**
	 * I kc is null the ordinary list for the accesstime administration is
	 * presented. If not null, a table with a checkbox column is presented to
	 * the user to select access times for the Keycard.
	 * 
	 * @param kc
	 *            Keycard or null
	 */
	public AccessGrantListPage(final KeyCard kc) {

		this.keycard = kc;

		Form<AccessGrant> form = new AccessGrantTableForm("form");

		form.add(new Label("AccessGrantCount", accessGrantProvider.size() + " Datensätze"));

		add(form);
	}

	class AccessGrantTableForm extends Form<AccessGrant> {
		private static final long serialVersionUID = 1L;

		public AccessGrantTableForm(String id) {
			super(id);

			ArrayList<IColumn> columns = new ArrayList<IColumn>();

			if (keycard != null)
				columns.add(new CheckBoxColumn<AccessGrant>(Model.of("")) {
					private static final long serialVersionUID = 5408932668347521514L;

					@Override
					protected IModel<Boolean> newCheckBoxModel(final IModel<AccessGrant> rowModel) {
						return new AbstractCheckBoxModel() {
							private static final long serialVersionUID = 2836188137794528810L;

							@Override
							public void unselect() {
								List<AccessGrant> accessgrants = keycard.getAccessgrants();
								accessgrants.remove(rowModel.getObject());
								keycard.setAccessgrants(accessgrants);
							}

							@Override
							public void select() {
								List<AccessGrant> accessgrants = keycard.getAccessgrants();
								accessgrants.add(rowModel.getObject());
								keycard.setAccessgrants(accessgrants);
							}

							@Override
							public boolean isSelected() {
								List<AccessGrant> accessgrants = keycard.getAccessgrants();
								return accessgrants.contains(rowModel.getObject());
							}

							@Override
							public void detach() {
								rowModel.detach();
							}
						};
					}
				});
			else
				columns.add(new PropertyColumn<AccessGrant>(new Model<String>("ID"), "id", "id"));

			columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Name"), "name", "name"));
			columns.add(new EnumPropertyColumn<DayOfWeek>(new Model<String>("Wochentag"), "DayOfWeek", "DayOfWeek", DayOfWeek.class, this));
			columns.add(new DateTimeColumn<AccessGrant>(new Model<String>("Von"), "TimeFrom", "HH:mm"));
			columns.add(new DateTimeColumn<AccessGrant>(new Model<String>("Bis"), "TimeUntil", "HH:mm"));

			if (keycard == null) {
				columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
					private static final long serialVersionUID = 7317382835422354117L;

					@Override
					public void onClick(Item item, String componentId, IModel model) {
						AccessGrant ag = (AccessGrant) model.getObject();
						setResponsePage(new AccessGrantDetailPage(ag));
					}
				});
				columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>("Entfernen"), new Model<String>("delete")) {
					private static final long serialVersionUID = -3524734341372805625L;

					@Override
					public void onClick(Item item, String componentId, IModel model) {
						AccessGrant ag = (AccessGrant) model.getObject();
						accessGrantMgmt.removeAccessGrant(ag);
					}
				});
			}

			DefaultDataTable<AccessGrant> table = new DefaultDataTable("AccessGrantTable", columns, accessGrantProvider, 5);
			add(table);

			if (keycard == null)
				add(new Button("submit", Model.of("Neue Zugangszeit")));
			else
				add(new Button("submit", Model.of("Hinzufügen")));
		}

		public void onSubmit() {

			if (keycard == null) {
				setResponsePage(new AccessGrantDetailPage(new AccessGrant()));
			} else {
				keycardMgmt.storeKeyCard(keycard);
				setResponsePage(new KeycardDetailPage(keycard));
			}

		}

	}

}
