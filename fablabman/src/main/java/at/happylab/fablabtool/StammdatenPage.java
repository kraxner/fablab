package at.happylab.fablabtool;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import at.happylab.fablabtool.web.access.AccessGrantListPage;
import at.happylab.fablabtool.web.access.KeycardListPage;
import at.happylab.fablabtool.web.device.DeviceListPage;

public class StammdatenPage extends BasePage {

	public StammdatenPage() {
		navigation.selectStammdaten();
		add(new Label("stammdatenLabel","Stammdaten"));
		
		add(new Link<String>("PackageLink") {
			private static final long serialVersionUID = -7089980845681924976L;

			public void onClick() {
		        setResponsePage(new PackageList());
		    }
		});
		
		add(new Link<String>("KeycardLink") {
			private static final long serialVersionUID = -1555070003525175626L;

			public void onClick() {
		        setResponsePage(new KeycardListPage());
		    }
		});
		
		add(new Link<String>("AccessGrantLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new AccessGrantListPage(null));
		    }
		});
		
		add(new Link<String>("ConsumableLink") {
			private static final long serialVersionUID = -5635826134514651087L;

			public void onClick() {
		        setResponsePage(new ConsumableListPage());
		    }
		});
		
		add(new Link<String>("DevicesLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new DeviceListPage());
		    }
		});
		
		add(new Link<String>("AccessTestLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new HomePage());
		    }
		});
	}

}
