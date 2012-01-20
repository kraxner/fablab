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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.AccessGrantManagement;
import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.dataprovider.AccessGrantProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.CheckBoxColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.DateTimeColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.EnumPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.DayOfWeek;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.web.BasePage;
import at.happylab.fablabtool.web.maintenance.StammdatenPage;
import at.happylab.fablabtool.web.util.ConfirmDeletePage;

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
	 * presented. If NOT null, a table with a checkbox column is presented to
	 * the user to select access times for the Keycard.
	 * 
	 * @param kc
	 *            Keycard or null
	 */
	public AccessGrantListPage(final KeyCard kc) {

		this.keycard = kc;

		add(new AccessGrantTableForm("form"));

	}

	class AccessGrantTableForm extends Form<KeyCard> {
		private static final long serialVersionUID = 1L;

		public AccessGrantTableForm(String id) {
			super(id, new CompoundPropertyModel<KeyCard>(keycard));

			@SuppressWarnings("rawtypes")
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

			columns.add(new PropertyColumn<AccessGrant>(new Model<String>("ID"), "id", "id"));
			columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Name"), "name", "name"));
			columns.add(new EnumPropertyColumn<DayOfWeek>(new Model<String>("Wochentag"), "DayOfWeek", "DayOfWeek", DayOfWeek.class, this));
			columns.add(new DateTimeColumn<AccessGrant>(new Model<String>("Von"), "TimeFrom", "HH:mm"));
			columns.add(new DateTimeColumn<AccessGrant>(new Model<String>("Bis"), "TimeUntil", "HH:mm"));

			if (keycard == null) {
				columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
					private static final long serialVersionUID = 7317382835422354117L;

					@SuppressWarnings("rawtypes")
					@Override
					public void onClick(Item item, String componentId, IModel model) {
						AccessGrant ag = (AccessGrant) model.getObject();
						setResponsePage(new AccessGrantDetailPage(ag));
					}
				});
				columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>("Entfernen"), new Model<String>("delete")) {
					private static final long serialVersionUID = -3524734341372805625L;

					@SuppressWarnings("rawtypes")
					@Override
					public void onClick(Item item, String componentId, final IModel model) {
						setResponsePage(new ConfirmDeletePage("Wollen sie diese Zugangszeit wirklich löschen?") {
							private static final long serialVersionUID = 215242593335920710L;

							@Override
							protected void onConfirm() {
								AccessGrant ag = (AccessGrant) model.getObject();
								accessGrantMgmt.removeAccessGrant(ag);

								setResponsePage(AccessGrantListPage.this);
							}

							@Override
							protected void onCancel() {
								setResponsePage(AccessGrantListPage.this);
							}
						});
					}
				});
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			DefaultDataTable<AccessGrant> table = new DefaultDataTable("AccessGrantTable", columns, accessGrantProvider, 5);
			add(table);

			add(new Label("AccessGrantCount", accessGrantProvider.size() + " Datensätze"));

			if (keycard == null)
				add(new Button("submit", Model.of("Neue Zugangszeit")));
			else
				add(new Button("submit", Model.of("Hinzufügen")));
			

			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					if (keycard == null) {
						setResponsePage(new StammdatenPage());
						
					} else {
						setResponsePage(new KeycardDetailPage(keycard));
					}
				}
			};
			add(goBackButton);
			
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
