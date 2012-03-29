package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.dao.BaseDAO;
import net.micalo.wicket.model.SmartModel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.Billing;
import at.happylab.fablabtool.dataprovider.SubscriptionProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;

public class SubscriptionPanel extends MembershipPanel {
	private static final long serialVersionUID = 1L;

	@Inject private SubscriptionProvider subscriptionsFromMembershipProvider;
	
	@Inject private EntityManager em;
	private BaseDAO<ConsumationEntry> consumationEntryDAO = new BaseDAO<ConsumationEntry>(ConsumationEntry.class, em);

	public SubscriptionPanel(String id, SmartModel<Membership> model) {
		super(id, model);

		subscriptionsFromMembershipProvider.setMembershipModel(model);

		Form<String> form = new Form<String>("main");

		form.add(new CheckBox("showCancelledPackages", new Model<Boolean>()) {
			private static final long serialVersionUID = 1L;

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}

			public void onSelectionChanged(Object showCancelledSubscriptions) {
				subscriptionsFromMembershipProvider.setShowCancelledSubscriptions((Boolean) showCancelledSubscriptions);
			}
		});

		List<IColumn<String>> columns = new ArrayList<IColumn<String>>();
		columns.add(new PropertyColumn<String>(new Model<String>("ID"), "id", "id"));
		columns.add(new PropertyColumn<String>(new Model<String>("Paket"), "booksPackage.name", "booksPackage.name"));
		columns.add(new PropertyColumn<String>(new Model<String>("angemeldet seit"), "validFrom", "validFrom"));
		columns.add(new PropertyColumn<String>(new Model<String>("angemeldet bis"), "validTo", "validTo"));
		columns.add(new PropertyColumn<String>(new Model<String>("bezahlt bis"), "payedUntil", "payedUntil"));
		columns.add(new PropertyColumn<String>(new Model<String>("Preis"), "priceOverruled", "priceOverruled"));
		columns.add(new LinkPropertyColumn<String>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = 1594610370135323737L;

			
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Subscription s = (Subscription) model.getObject();
				setResponsePage(SubscriptionDetailPage.class, new PageParameters("id=" + s.getId()));
			}

		});

		form.add(new DefaultDataTable("subscriptionsTable", columns, subscriptionsFromMembershipProvider, 5));

		form.add(new Label("subscriptionsCount", subscriptionsFromMembershipProvider.size() + " Datensätze"));

		form.add(new Link<String>("addSubscription") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(SubscriptionDetailPage.class, new PageParameters("membershipId=" + membershipModel.getObject().getId()));
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
			
			// Defaultmäßig auf heutiges Datum
			accountUntil = new Date();
			
			add(new RequiredTextField<Date>("accountUntil", new PropertyModel(this, "accountUntil")));
		}
		
		public void onSubmit() {
			
			Iterator<Subscription> subscriptions = subscriptionsFromMembershipProvider.iterator(0, subscriptionsFromMembershipProvider.size());
			
			while (subscriptions.hasNext()) {
				// passing all subscriptions looks strange, but Billing.createEntryFromSubscription returns null, if the subscription should not be billed
				ConsumationEntry entry = Billing.createEntryFromSubscription(subscriptions.next(), accountUntil); 
				
				if (entry != null) {
					consumationEntryDAO.store(entry);
					consumationEntryDAO.commit();
				}
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
