package at.happylab.fablabtool.web.accessgrant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.PackageAddPage;
import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.dataprovider.KeycardProvider;
import at.happylab.fablabtool.dataprovider.PackageProvider;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class KeycardListPage extends BasePage {

	@Inject
	KeycardProvider keycardProvider;
	@Inject
	KeycardManagement keycardMgmt;

	public KeycardListPage() {
		// navigation.selectStammdaten();

		List<IColumn<KeyCard>> columns = new ArrayList<IColumn<KeyCard>>();
		columns.add(new PropertyColumn<KeyCard>(new Model<String>("ID"), "id",
				"id"));
		columns.add(new PropertyColumn<KeyCard>(new Model<String>("Aktiv"),
				"active", "active"));
		columns.add(new PropertyColumn<KeyCard>(new Model<String>("RFID"),
				"rfid", "rfid"));
		columns.add(new LinkPropertyColumn<KeyCard>(new Model<String>(
				"Bearbeiten"), new Model<String>("edit")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				KeyCard k = (KeyCard) model.getObject();
				setResponsePage(new KeycardDetailPage(k));

			}
		});
		columns.add(new LinkPropertyColumn<KeyCard>(new Model<String>(
				"Entfernen"), new Model<String>("delete")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				KeyCard k = (KeyCard) model.getObject();
				keycardMgmt.removeKeycard(k);
			}
		});

		DefaultDataTable<KeyCard> table = new DefaultDataTable<KeyCard>(
				"keycardTable", columns, keycardProvider, 5);
		add(table);

		add(new Label("keycardCount", keycardProvider.size() + " Datensätze"));

		add(new Link("addKeycard") {
			public void onClick() {
				setResponsePage(new KeycardDetailPage(new KeyCard()));
			}
		});

	}

}
