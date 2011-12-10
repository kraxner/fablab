package at.happylab.fablabtool.web.membership;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.dataprovider.SubscriptionProvider;
import at.happylab.fablabtool.model.BusinessMembership;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class SubscriptionPanel extends Panel {

	private static final long serialVersionUID = -7129490579199414107L;

	@Inject
	SubscriptionProvider subscriptionsFromMembershipProvider;
	
	@Inject
	SubscriptionManagement subscriptionMgmt;

	public SubscriptionPanel(String id, final Membership member,
			final MembershipManagement membershipMgmt) {
		super(id);
		
		subscriptionsFromMembershipProvider.setMember(member);

		Form<String> form = new Form<String>("main");

		IColumn[] columns = new IColumn[8];
		columns[0] = new LinkPropertyColumn(new Model<String>("Nr"), "id", "id") {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				setResponsePage(new SubscriptionDetailPage(member, membershipMgmt, s));
				
			}
		};
		columns[1] = new PropertyColumn(new Model<String>("Paket"), "booksPackage.name", "booksPackage.name");
		columns[2] = new PropertyColumn(new Model<String>("angemeldet seit"), "validFrom", "validFrom");
		columns[3] = new PropertyColumn(new Model<String>("angemeldet bis"), "validTo", "validTo");
		columns[4] = new PropertyColumn(new Model<String>("Preis"), "priceOverruled", "priceOverruled");
		columns[5] = new PropertyColumn(new Model<String>("Zahlungsperiode"), "payedUntil", "payedUntil");
		columns[6] = new PropertyColumn(new Model<String>("bezahlt bis"), "payedUntil", "payedUntil");
		columns[7] = new LinkPropertyColumn(new Model<String>("Aktion"), new Model("kündigen")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				subscriptionMgmt.cancelSubscription(s);
			}
			 
		};
		
		form.add(new DefaultDataTable("subscriptionsTable", columns, subscriptionsFromMembershipProvider, 5));

		form.add(new Label("subscriptionsCount", subscriptionsFromMembershipProvider.size() + " Datensätze"));
		
		form.add(new Link("addSubscription") {
			private static final long serialVersionUID = 9170539765267094210L;

			public void onClick() {
                setResponsePage(new SubscriptionDetailPage(member, membershipMgmt, new Subscription()));
            }
        });

		add(form);

	}

}
