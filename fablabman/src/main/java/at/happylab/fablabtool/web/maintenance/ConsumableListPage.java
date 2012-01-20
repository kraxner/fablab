package at.happylab.fablabtool.web.maintenance;

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

import at.happylab.fablabtool.beans.ConsumableManagement;
import at.happylab.fablabtool.dataprovider.ConsumableProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.Consumable;
import at.happylab.fablabtool.web.BasePage;
import at.happylab.fablabtool.web.util.ConfirmDeletePage;

public class ConsumableListPage extends BasePage {

	@Inject
	ConsumableProvider consumableProvider;
	@Inject
	ConsumableManagement consumableMgmt;

	public ConsumableListPage() {

		List<IColumn<Consumable>> columns = new ArrayList<IColumn<Consumable>>();
		columns.add(new PropertyColumn<Consumable>(new Model<String>("ID"), "id", "id"));
		columns.add(new PropertyColumn<Consumable>(new Model<String>("Name"), "name", "name"));
		columns.add(new PropertyColumn<Consumable>(new Model<String>("Preis pro Einheit"), "pricePerUnit", "pricePerUnit"));
		columns.add(new PropertyColumn<Consumable>(new Model<String>("Einheit"), "unit", "unit"));
		columns.add(new LinkPropertyColumn<Consumable>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = -523422943144381848L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Consumable cons = (Consumable) model.getObject();
				setResponsePage(new ConsumableAddPage(cons));

			}
		});
		columns.add(new LinkPropertyColumn<Consumable>(new Model<String>("Entfernen"), new Model<String>("delete")) {
			private static final long serialVersionUID = -3524734341372805625L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, final IModel model) {
				
				setResponsePage(new ConfirmDeletePage("Wollen sie dieses Consumable wirklich löschen?") {
					private static final long serialVersionUID = 215242593335920710L;

					@Override
					protected void onConfirm() {
						Consumable c = (Consumable) model.getObject();
						consumableMgmt.removeConsumable(c);
						
						setResponsePage(ConsumableListPage.this);
					}

					@Override
					protected void onCancel() {
						setResponsePage(ConsumableListPage.this);
					}

				});
				
				
			}
		});
		DefaultDataTable<Consumable> table = new DefaultDataTable<Consumable>("consumableTable", columns, consumableProvider, 5);
		add(table);

		add(new Label("consumableCount", consumableProvider.size() + " Datensätze"));

		add(new Link<String>("addConsumable") {
			private static final long serialVersionUID = 877465087033681295L;

			public void onClick() {
				setResponsePage(new ConsumableAddPage(new Consumable()));
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
