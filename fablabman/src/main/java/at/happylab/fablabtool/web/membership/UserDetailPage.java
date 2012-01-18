package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.beans.UserManagement;
import at.happylab.fablabtool.dataprovider.TrainedDevicesFromUserProvider;
import at.happylab.fablabtool.model.Device;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.authentication.AdminBasePage;
import at.happylab.fablabtool.web.device.DeviceListPage;

public class UserDetailPage extends AdminBasePage {

	@Inject
	UserManagement userMgmt;

	@Inject
	KeycardManagement keycardMgmt;

	private User member;

	public UserDetailPage(User user) {
		this.member = user;

		add(new FeedbackPanel("feedback"));

		add(new UserDetailForm("form"));
	}

	class UserDetailForm extends Form<User> {
		private static final long serialVersionUID = 8638737151881505884L;

		public UserDetailForm(String s) {
			super(s, new CompoundPropertyModel<User>(member));

			final CheckBox activeYN = new CheckBox("keyCard.active");

			if (member.getKeyCard() == null)
				activeYN.setEnabled(false);
			else if (member.getKeyCard().getRfid() == null)
				activeYN.setEnabled(false);
			else
				activeYN.setEnabled(true);

			add(activeYN);

			final TextField<String> rfid = new TextField<String>("keyCard.rfid");
			add(rfid);

			List<IColumn<Device>> columns = new ArrayList<IColumn<Device>>();

			columns.add(new PropertyColumn<Device>(new Model<String>("Geräte ID"), "deviceId", "deviceId"));
			columns.add(new PropertyColumn<Device>(new Model<String>("Name"), "name", "name"));
			columns.add(new LinkPropertyColumn<Device>(new Model<String>("Entfernen"), new Model<String>("delete")) {
				private static final long serialVersionUID = -2707789802150151990L;

				@SuppressWarnings("rawtypes")
				@Override
				public void onClick(Item item, String componentId, final IModel model) {
					Device a = (Device) model.getObject();
					List<Device> lst = member.getTrainedForDevices();
					lst.remove(a);
					member.setTrainedForDevices(lst);
				}
			});

			DefaultDataTable<Device> table = new DefaultDataTable<Device>("TrainedDevicesFromUserTable", columns, new TrainedDevicesFromUserProvider(member), 20);
			add(table);

			Link<String> addDeviceToUser = new Link<String>("addDeviceToUser") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new DeviceListPage(member));
				}
			};
			add(addDeviceToUser);

			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new UserListPage());
				}
			};
			add(goBackButton);

		}

		public void onSubmit() {

			if (member.getKeyCard() == null)
				member.setKeyCard(null);
			else
				keycardMgmt.storeKeyCard(member.getKeyCard());

			userMgmt.storeUser(member);
			
			setResponsePage(new UserListPage());

		}
	}

}
