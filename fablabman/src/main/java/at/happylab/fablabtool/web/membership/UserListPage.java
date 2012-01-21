package at.happylab.fablabtool.web.membership;

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

import at.happylab.fablabtool.dataprovider.UserProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.DateTimeColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.EnumPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.web.authentication.AdminBasePage;
import at.happylab.fablabtool.web.maintenance.MasterdataPage;

public class UserListPage extends AdminBasePage {
	
	@Inject
	UserProvider userProvider;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserListPage() {

		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new PropertyColumn<User>(new Model<String>("Vorname"), "firstname", "firstname"));
		columns.add(new PropertyColumn<User>(new Model<String>("Nachname"), "lastname", "lastname"));
		columns.add(new DateTimeColumn<User>(new Model<String>("Geburtstag"), "birthday", "dd.MM.yyyy"));
		columns.add(new EnumPropertyColumn<MembershipType>(new Model<String>("Mitgliedschaft"), "membership.type", "membership.type", MembershipType.class, this));
		columns.add(new PropertyColumn<User>(new Model<String>("Keycard"), "KeyCard.rfid"));
		
		columns.add(new LinkPropertyColumn<User>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = 5612256017598665667L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				User u = (User) model.getObject();
				setResponsePage(new UserDetailPage(u));
			}
		});

		add(new DefaultDataTable("userTable", columns, userProvider, 5));

		add(new Label("userCount", userProvider.size() + " Datensätze"));
		
		Link<String> goBackButton = new Link<String>("goBack") {
			private static final long serialVersionUID = -3527050342774869192L;

			public void onClick() {
				setResponsePage(new MasterdataPage());
			}
		};
		add(goBackButton);

	}


}
