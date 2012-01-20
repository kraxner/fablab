package at.happylab.fablabtool.web.access;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.dataprovider.KeycardProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.web.authentication.AdminBasePage;
import at.happylab.fablabtool.web.maintenance.StammdatenPage;
import at.happylab.fablabtool.web.util.ConfirmDeletePage;
import at.happylab.fablabtool.web.util.WarningPage;

public class KeycardListPage extends AdminBasePage {

	@Inject
	KeycardProvider keycardProvider;
	@Inject
	KeycardManagement keycardMgmt;

	public KeycardListPage() {

		List<IColumn<KeyCard>> columns = new ArrayList<IColumn<KeyCard>>();
		columns.add(new PropertyColumn<KeyCard>(new Model<String>("Aktiv"), "active", "active"));
		columns.add(new PropertyColumn<KeyCard>(new Model<String>("RFID"), "rfid", "rfid"));
		columns.add(new LinkPropertyColumn<KeyCard>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = 5612256017598665667L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				KeyCard k = (KeyCard) model.getObject();
				setResponsePage(new KeycardDetailPage(k));

			}
		});
		columns.add(new LinkPropertyColumn<KeyCard>(new Model<String>("Entfernen"), new Model<String>("delete")) {
			private static final long serialVersionUID = 4741807491393228633L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, final IModel model) {

				setResponsePage(new ConfirmDeletePage("Wollen sie diese Keycard wirklich löschen?") {
					private static final long serialVersionUID = 215242593335920710L;

					@Override
					protected void onConfirm() {
						KeyCard k = (KeyCard) model.getObject();

						try {
							keycardMgmt.removeKeycard(k);
							setResponsePage(KeycardListPage.this);
							
						} catch (Exception e) {
							// Fehlermeldung, falls die Keycard noch einem Mitglied zugeordnet ist.
							setResponsePage(new WarningPage("Diese Keycard kann nicht gelöscht werden.") {
								@Override
								protected void onConfirm() {
									setResponsePage(KeycardListPage.this);
								}
							});
						}
					}

					@Override
					protected void onCancel() {
						setResponsePage(KeycardListPage.this);
					}

				});

			}
		});

		add(new DefaultDataTable<KeyCard>("keycardTable", columns, keycardProvider, 5));

		add(new Label("keycardCount", keycardProvider.size() + " Datensätze"));

		add(new Link<String>("addKeycard") {
			private static final long serialVersionUID = 6855451272464273371L;

			public void onClick() {
				setResponsePage(new KeycardDetailPage(new KeyCard()));
			}
		});

		Link<String> goBackButton = new Link<String>("goBack") {
			private static final long serialVersionUID = -3527050342774869192L;

			public void onClick() {
				setResponsePage(new StammdatenPage());
			}
		};
		add(goBackButton);

	}

}
