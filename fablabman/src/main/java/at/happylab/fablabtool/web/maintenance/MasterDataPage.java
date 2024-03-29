package at.happylab.fablabtool.web.maintenance;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import at.happylab.fablabtool.web.BasePage;
import at.happylab.fablabtool.web.HomePage;
import at.happylab.fablabtool.web.access.AccessGrantListPage;
import at.happylab.fablabtool.web.access.KeycardListPage;
import at.happylab.fablabtool.web.device.DeviceListPage;
import at.happylab.fablabtool.web.maintenance.data.CreateTestDataPage;
import at.happylab.fablabtool.web.membership.UserListPage;

public class MasterDataPage extends BasePage {

	public MasterDataPage() {
		navigation.selectMasterData();
		add(new Label("masterdataLabel","Stammdaten"));
		
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
		
		add(new Link<String>("SubscriptionLink") {
			private static final long serialVersionUID = -5635826134514651087L;

			public void onClick() {
		        setResponsePage(new SubscriptionListPage());
		    }
		});
		
		add(new Link<String>("DevicesLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new DeviceListPage(null));
		    }
		});
		
		add(new Link<String>("UsersLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new UserListPage());
		    }
		});
		
		add(new Link<String>("WebUserLink") {
			private static final long serialVersionUID = 1829678788524180514L;

			public void onClick() {
		        setResponsePage(new WebUserListPage());
		    }
		});
		
		add(new Link<String>("AccessTestLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new HomePage());
		    }
		});
		add(new Link<String>("CreateTestDataLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        setResponsePage(new CreateTestDataPage());
		    }
		});
		
		add(new Link<String>("RuntimeExceptionLink") {
			private static final long serialVersionUID = -1585556011311826709L;

			public void onClick() {
		        throw new RuntimeException("Diese Runtime exception wurde absichtlich ausgelöst.");
		    }
		});
		
		
	}

}
