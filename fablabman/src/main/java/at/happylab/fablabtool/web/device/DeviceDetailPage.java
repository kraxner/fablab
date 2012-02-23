package at.happylab.fablabtool.web.device;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.dao.BaseDAO;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import at.happylab.fablabtool.model.Device;
import at.happylab.fablabtool.web.BasePage;

public class DeviceDetailPage extends BasePage {

	@Inject private EntityManager em;
	private BaseDAO<Device> deviceDAO = new BaseDAO<Device>(Device.class, em);
	
	private Device device;

	public DeviceDetailPage(Device d) {
		navigation.selectMasterData();
		
		this.device = d;
		
		if (device.getId() == 0)
			add(new Label("pageHeader", "Neues Gerät"));
		else
			add(new Label("pageHeader", "Gerät bearbeiten"));
		
		add(new FeedbackPanel("feedback"));

		add(new DeviceForm("form", getDefaultModel()));
	}

	class DeviceForm extends Form<Device> {

		private static final long serialVersionUID = 992333748937634463L;

		public DeviceForm(String s, IModel<?> m) {
			super(s, new CompoundPropertyModel<Device>(device));

			final TextField<String> deviceID = new TextField<String>("deviceId");
			add(deviceID);

			final RequiredTextField<String> name = new RequiredTextField<String>("name");
			add(name);
			
			final TextArea<String> description = new TextArea<String>("description");
			add(description);
			
			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new DeviceListPage(null));
				}
			};
			add(goBackButton);

		}

		public void onSubmit() {
			deviceDAO.store(device);
			deviceDAO.commit();
			setResponsePage(new DeviceListPage(null));
		}
	}
}
