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

import at.happylab.fablabtool.dataprovider.SubscriptionProvider;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.membership.SubscriptionDetailPage;

public class SubscriptionListPage extends BasePage {

	@Inject
	SubscriptionProvider subscriptionProvider;

	public SubscriptionListPage() {

		List<IColumn<Subscription>> columns = new ArrayList<IColumn<Subscription>>();
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Membership ID"), "memberId", "bookedBy.memberId"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Firmenname"), "bookedBy.companyName", "bookedBy.companyName"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Vorname"), "firstname", "bookedBy.users[0].firstname"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Nachname"), "lastname", "bookedBy.users[0].lastname"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Paket"), "Package.name", "booksPackage.name"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("angemeldet seit"), "validFrom", "validFrom"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("angemeldet bis"), "validTo", "validTo"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Preis"), "priceOverruled", "priceOverruled"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("bezahlt bis"), "payedUntil", "payedUntil"));
		columns.add(new LinkPropertyColumn<Subscription>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = -4135397597596972629L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				
				setResponsePage(new SubscriptionDetailPage(s.getBookedBy(), s));
			}
		});


		DefaultDataTable<Subscription> table = new DefaultDataTable<Subscription>("subscriptionTable", columns, subscriptionProvider, 5);
		add(table);
		
		add(new Label("subscriptionCount", subscriptionProvider.size() + " Datensätze"));
		
		Link<String> goBackButton = new Link<String>("goBack") {
			private static final long serialVersionUID = -3527050342774869192L;

			public void onClick() {
				setResponsePage(new StammdatenPage());
			}
		};
		add(goBackButton);

	}

}
