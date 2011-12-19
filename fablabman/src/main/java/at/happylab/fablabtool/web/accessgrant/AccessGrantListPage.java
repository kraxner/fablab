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
import at.happylab.fablabtool.beans.AccessGrantManagement;
import at.happylab.fablabtool.dataprovider.AccessGrantProvider;
import at.happylab.fablabtool.dataprovider.KeycardProvider;
import at.happylab.fablabtool.dataprovider.PackageProvider;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class AccessGrantListPage extends BasePage {

	@Inject
	AccessGrantProvider accessGrantProvider;
	@Inject
	AccessGrantManagement accessGrantMgmt;

	public AccessGrantListPage() {
		//navigation.selectStammdaten();

		List<IColumn<AccessGrant>> columns = new
				ArrayList<IColumn<AccessGrant>>();
		columns.add(new PropertyColumn<AccessGrant>(new Model<String>("ID"), "id",
				"id"));
		columns.add( new PropertyColumn<AccessGrant>(new Model<String>("Name"),
				"name", "name"));
		columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Wochentag"),
				"DayOfWeek", "DayOfWeek"));
		columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Von"),
				"TimeFrom", "TimeFrom"));
		columns.add(new PropertyColumn<AccessGrant>(new Model<String>("Bis"),
				"TimeUntil", "TimeUntil"));
		columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>(
				"Bearbeiten"), new Model<String>("edit")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				AccessGrant g = (AccessGrant) model.getObject();
				setResponsePage(new AccessGrantDetailPage(g));
			}
		});
		columns.add(new LinkPropertyColumn<AccessGrant>(new Model<String>(
				"Entfernen"), new Model<String>("delete")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				AccessGrant a = (AccessGrant) model.getObject();
				accessGrantMgmt.removeAccessGrant(a);
			}
		});

		DefaultDataTable<AccessGrant> table = new DefaultDataTable<AccessGrant>(
				"AccessGrantTable", columns, accessGrantProvider, 5);
		add(table);
		
		add(new Label("AccessGrantCount", accessGrantProvider.size() + " Datensätze"));

		add(new Link("addAccessGrant") {
            public void onClick() {
                setResponsePage(new AccessGrantDetailPage(new AccessGrant()));
            }
        });

	}

	
	
}
