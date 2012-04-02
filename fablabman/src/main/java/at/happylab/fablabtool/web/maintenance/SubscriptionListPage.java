package at.happylab.fablabtool.web.maintenance;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dataprovider.SubscriptionProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.EnumPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.web.BasePage;
import at.happylab.fablabtool.web.membership.MembershipDetailPage;
import at.happylab.fablabtool.web.membership.SubscriptionDetailPage;

public class SubscriptionListPage extends BasePage {

	@Inject private	SubscriptionProvider subscriptionProvider;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SubscriptionListPage() {
		navigation.selectSubscriptions();
		
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new LinkPropertyColumn<Subscription>(new Model<String>("Mitglid Nr."), "memberId", "bookedBy.memberId") {
			private static final long serialVersionUID = -4135397597596972629L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				setResponsePage(MembershipDetailPage.class,  new PageParameters("id=" + s.getBookedBy().getId()) );
			}
		});
		columns.add(new LinkPropertyColumn<Subscription>(new Model<String>("Vorname"), "firstname", "bookedBy.users[0].firstname"){
			private static final long serialVersionUID = -4135397597596972629L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				setResponsePage(MembershipDetailPage.class,  new PageParameters("id=" + s.getBookedBy().getId()) );
			}
		});
		columns.add(new LinkPropertyColumn<Subscription>(new Model<String>("Nachname"), "lastname", "bookedBy.users[0].lastname"){
			private static final long serialVersionUID = -4135397597596972629L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				setResponsePage(MembershipDetailPage.class,  new PageParameters("id=" + s.getBookedBy().getId()) );
			}
		});
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Firmenname"), "bookedBy.companyName", "bookedBy.companyName"));
		columns.add(new EnumPropertyColumn<MembershipType>(new Model<String>("Art"), "type", "bookedBy.membershipType", MembershipType.class, this));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Paket"), "Package.name", "booksPackage.name"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("angemeldet seit"), "validFrom", "validFrom"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("angemeldet bis"), "validTo", "validTo"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("Preis"), "priceOverruled", "priceOverruled"));
		columns.add(new PropertyColumn<Subscription>(new Model<String>("bezahlt bis"), "payedUntil", "payedUntil"));
		columns.add(new LinkPropertyColumn<Subscription>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = -4135397597596972629L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				
				setResponsePage(SubscriptionDetailPage.class, new PageParameters("id="+s.getId()));
			}
		});

		DefaultDataTable<Subscription> table = new DefaultDataTable("subscriptionTable", columns, subscriptionProvider, 50);
		add(table);
		
		add(new Label("subscriptionCount", subscriptionProvider.size() + " Datensätze"));
		
		Link<String> goBackButton = new Link<String>("goBack") {
			private static final long serialVersionUID = -3527050342774869192L;

			public void onClick() {
				setResponsePage(new MasterDataPage());
			}
		};
		add(goBackButton);

	}

}
