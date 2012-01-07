package at.happylab.fablabtool;

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

import at.happylab.fablabtool.dataprovider.ConsumableProvider;
import at.happylab.fablabtool.dataprovider.PackageProvider;
import at.happylab.fablabtool.model.Consumable;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class ConsumableListPage extends BasePage {

	@Inject
	ConsumableProvider consumableProvider;

	public ConsumableListPage() {
		navigation.selectStammdaten();

		List<IColumn<Consumable>> columns = new
				ArrayList<IColumn<Consumable>>();
		columns.add(new PropertyColumn<Consumable>(new Model<String>("ID"), "id",
				"id"));
		columns.add(new PropertyColumn<Consumable>(new Model<String>("Name"),
				"name", "name"));
		columns.add( new PropertyColumn<Consumable>(new Model<String>("Preis pro Einheit"),
				"pricePerUnit", "pricePerUnit"));
		columns.add(new PropertyColumn<Consumable>(new Model<String>("Einheit"),
				"unit", "unit"));
		columns.add(new LinkPropertyColumn<Consumable>(new Model<String>(
				"Bearbeiten"), new Model<String>("edit")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Consumable cons = (Consumable) model.getObject();
				setResponsePage(new ConsumableAddPage(cons));

			}
		});

		DefaultDataTable<Consumable> table = new DefaultDataTable<Consumable>("consumableTable", columns, consumableProvider, 5);
		add(table);
		
		add(new Label("consumableCount", consumableProvider.size() + " Datensätze"));

		add(new Link("addConsumable") {
            public void onClick() {
                setResponsePage(new ConsumableAddPage(new Consumable()));
            }
        });

	}

}
