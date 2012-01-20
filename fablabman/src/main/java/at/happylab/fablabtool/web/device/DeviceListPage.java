package at.happylab.fablabtool.web.device;

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

import at.happylab.fablabtool.beans.DeviceManagement;
import at.happylab.fablabtool.dataprovider.DeviceProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.CheckBoxColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.Device;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.web.BasePage;
import at.happylab.fablabtool.web.maintenance.StammdatenPage;
import at.happylab.fablabtool.web.membership.UserDetailPage;
import at.happylab.fablabtool.web.util.ConfirmDeletePage;
import at.happylab.fablabtool.web.util.WarningPage;

public class DeviceListPage extends BasePage {

	@Inject
	DeviceManagement deviceMgmt;
	@Inject
	DeviceProvider deviceProvider;

	private User member;

	public DeviceListPage(final User member) {

		this.member = member;

		add(new DeviceListForm("form"));

	}

	class DeviceListForm extends Form<User> {
		private static final long serialVersionUID = 2689282504086229133L;

		public DeviceListForm(String id) {
			super(id, new CompoundPropertyModel<User>(member));

			List<IColumn<Device>> columns = new ArrayList<IColumn<Device>>();

			if (member != null)
				columns.add(new CheckBoxColumn<Device>(Model.of("")) {
					private static final long serialVersionUID = 5408932668347521514L;

					@Override
					protected IModel<Boolean> newCheckBoxModel(final IModel<Device> rowModel) {
						return new AbstractCheckBoxModel() {
							private static final long serialVersionUID = 2836188137794528810L;

							@Override
							public void unselect() {
								List<Device> devices = member.getTrainedForDevices();
								devices.remove(rowModel.getObject());
								member.setTrainedForDevices(devices);
							}

							@Override
							public void select() {
								List<Device> devices = member.getTrainedForDevices();
								devices.add(rowModel.getObject());
								member.setTrainedForDevices(devices);
							}

							@Override
							public boolean isSelected() {
								List<Device> devices = member.getTrainedForDevices();
								return devices.contains(rowModel.getObject());
							}

							@Override
							public void detach() {
								rowModel.detach();
							}
						};
					}
				});

			columns.add(new PropertyColumn<Device>(new Model<String>("Name"), "name", "name"));
			columns.add(new PropertyColumn<Device>(new Model<String>("Geräte ID"), "deviceId", "deviceId"));

			if (member == null) {
				columns.add(new LinkPropertyColumn<Device>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
					private static final long serialVersionUID = -2707789802150151990L;

					@SuppressWarnings("rawtypes")
					@Override
					public void onClick(Item item, String componentId, final IModel model) {
						Device k = (Device) model.getObject();
						setResponsePage(new DeviceDetailPage(k));
					}
				});
				columns.add(new LinkPropertyColumn<Device>(new Model<String>("Entfernen"), new Model<String>("delete")) {
					private static final long serialVersionUID = -2707789802150151990L;

					@SuppressWarnings("rawtypes")
					@Override
					public void onClick(Item item, String componentId, final IModel model) {
						setResponsePage(new ConfirmDeletePage("Wollen sie dieses Gerät wirklich löschen?") {
							private static final long serialVersionUID = 215242593335920710L;

							@Override
							protected void onConfirm() {
								Device k = (Device) model.getObject();
								try {
									deviceMgmt.removeDevice(k);
									setResponsePage(DeviceListPage.this);
									
								} catch (Exception e) {
									// Fehlermeldung, falls das Gerät noch einem Mitglied als trained device zugeordnet ist.
									setResponsePage(new WarningPage("Dieses Gerät kann nicht gelöscht werden.") {
										@Override
										protected void onConfirm() {
											setResponsePage(DeviceListPage.this);
										}
									});
								}
								
							}

							@Override
							protected void onCancel() {
								setResponsePage(DeviceListPage.this);
							}

						});
					}
				});
			}

			add(new DefaultDataTable<Device>("deviceTable", columns, deviceProvider, 20));

			add(new Label("deviceCount", deviceProvider.size() + " Datensätze"));

			if (member == null)
				add(new Button("submit", Model.of("Neues Gerät")));
			else
				add(new Button("submit", Model.of("Hinzufügen")));

			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new StammdatenPage());
				}
			};
			add(goBackButton);

		}

		public void onSubmit() {
			if (member == null) {
				// neues Gerät
				setResponsePage(new DeviceDetailPage(new Device()));
			} else {
				setResponsePage(new UserDetailPage(member));
			}
		}
	}
}
