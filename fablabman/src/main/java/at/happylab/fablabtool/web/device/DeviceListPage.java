package at.happylab.fablabtool.web.device;

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

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.DeviceManagement;
import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.dataprovider.DeviceProvider;
import at.happylab.fablabtool.dataprovider.KeycardProvider;
import at.happylab.fablabtool.model.Device;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.access.KeycardDetailPage;

public class DeviceListPage extends BasePage {

	@Inject
	DeviceManagement deviceMgmt;
	@Inject
	DeviceProvider deviceProvider;

	public DeviceListPage() {
		
		List<IColumn<Device>> columns = new ArrayList<IColumn<Device>>();
		columns.add(new LinkPropertyColumn<Device>(new Model<String>("Geräte ID"), "deviceId"){
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Device k = (Device) model.getObject();
				setResponsePage(new DeviceDetailPage(k));

			}
		});
		columns.add(new LinkPropertyColumn<Device>(new Model<String>("Name"), "name") {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Device k = (Device) model.getObject();
				setResponsePage(new DeviceDetailPage(k));

			}
		});
		columns.add(new LinkPropertyColumn<Device>(new Model<String>("Entfernen"), new Model<String>("delete")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Device k = (Device) model.getObject();
				deviceMgmt.removeDevice(k);
			}
		});

		add(new DefaultDataTable<Device>("deviceTable", columns, deviceProvider, 5));

		add(new Label("deviceCount", deviceProvider.size() + " Datensätze"));

		add(new Link("addDevice") {
			public void onClick() {
				setResponsePage(new DeviceDetailPage(new Device()));
			}
		});
		
		
	}

}
