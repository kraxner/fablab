package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.form.TextField;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.dataprovider.SubscriptionProvider;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class SubscriptionPanel extends Panel {

	private static final long serialVersionUID = -7129490579199414107L;

	@Inject
	SubscriptionProvider subscriptionsFromMembershipProvider;

	@Inject
	SubscriptionManagement subscriptionMgmt;

	public SubscriptionPanel(String id, final Membership member, final MembershipManagement membershipMgmt) {
		super(id);

		subscriptionsFromMembershipProvider.setMember(member);

		Form<String> form = new Form<String>("main");

		form.add(new CheckBox("showCancelledPackages", new Model<Boolean>()) {
			private static final long serialVersionUID = 9170539765267094210L;

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}

			public void onSelectionChanged(Object showCancelledSubscriptions) {
				subscriptionsFromMembershipProvider.setShowCancelledSubscriptions((Boolean) showCancelledSubscriptions);
			}
		});

		List<IColumn<String>> columns = new ArrayList<IColumn<String>>();
		columns.add(new PropertyColumn<String>(new Model<String>("Paket"), "booksPackage.name", "booksPackage.name"));
		columns.add(new PropertyColumn<String>(new Model<String>("angemeldet seit"), "validFrom", "validFrom"));
		columns.add(new PropertyColumn<String>(new Model<String>("angemeldet bis"), "validTo", "validTo"));
		columns.add(new PropertyColumn<String>(new Model<String>("bezahlt bis"), "payedUntil", "payedUntil"));
		columns.add(new PropertyColumn<String>(new Model<String>("Preis"), "priceOverruled", "priceOverruled"));
		columns.add(new LinkPropertyColumn<String>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = 1594610370135323737L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				setResponsePage(new SubscriptionDetailPage(member, s));
			}

		});

		form.add(new DefaultDataTable("subscriptionsTable", columns, subscriptionsFromMembershipProvider, 5));

		form.add(new Label("subscriptionsCount", subscriptionsFromMembershipProvider.size() + " Datensätze"));

		form.add(new Link<String>("addSubscription") {
			private static final long serialVersionUID = 9170539765267094210L;

			public void onClick() {
				Subscription s = new Subscription();
				s.setValidFrom(new Date());
				setResponsePage(new SubscriptionDetailPage(member, s));
				
				// TODO: Replace Methode einsetzen um Panel auszutauschen
				
			}
		});

		add(form);
		
		add(new CreateEntriesForm("createEntries"));
	}
	
	private class CreateEntriesForm extends Form<Object> {
		private static final long serialVersionUID = 1L;
		
		private Date accountUntil;
		
		public CreateEntriesForm(String id) {
			super(id);
			
			add(new TextField<Date>("accountUntil", new PropertyModel(this, "accountUntil")));
		}
		
		public void onSubmit() {
			
			Iterator<Subscription> subscriptions = subscriptionsFromMembershipProvider.iterator(0, 1);
			
			while (subscriptions.hasNext()) {
				subscriptions.next().createEntries(accountUntil);
			}
		}
		
		public Date getAccountUntil() {
			return accountUntil;
		}

		public void setAccountUntil(Date accountUntil) {
			this.accountUntil = accountUntil;
		}
		
	}

}
