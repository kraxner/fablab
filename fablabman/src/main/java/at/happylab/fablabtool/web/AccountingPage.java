package at.happylab.fablabtool.web;

import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.Billing;
import at.happylab.fablabtool.beans.ConsumationEntryManagement;
import at.happylab.fablabtool.beans.InvoiceManagement;
import at.happylab.fablabtool.dataprovider.ConsumationEntryProvider;
import at.happylab.fablabtool.dataprovider.MembershipProvider;
import at.happylab.fablabtool.dataprovider.SubscriptionProvider;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;

public class AccountingPage extends BasePage {

	@Inject
	SubscriptionProvider subscriptionProvider;
	
	@Inject
	private ConsumationEntryManagement consumationEntryMgmt;
	
	
	@Inject
	MembershipProvider membershipProvider;
	
	@Inject
	ConsumationEntryProvider entryFromMembershipProvider;
	
	@Inject 
	InvoiceManagement invoiceMgmt;
	
	public AccountingPage() {
		navigation.selectAufgaben();
		
		add(new CreateEntriesForm("createEntries"));
		add(new CreateInvoicesForm("createInvoices"));
	}
	
	private class CreateEntriesForm extends Form<Object> {
		private static final long serialVersionUID = 1L;
		
		private Date accountUntil;
		
		public CreateEntriesForm(String id) {
			super(id);
			
			// Defaultmäßig auf heutiges Datum
			accountUntil = new Date();
			
			add(new RequiredTextField<Date>("accountUntil", new PropertyModel<Date>(this, "accountUntil")));
			add(new Label("entryCount", ""));
		}
		
		public void onSubmit() {
			Iterator<Subscription> subscriptions = subscriptionProvider.iterator(0, subscriptionProvider.size());
			
			int count = 0;
			
			while (subscriptions.hasNext()) {
				ConsumationEntry entry = Billing.createEntryFromSubscription(subscriptions.next(), accountUntil);
				
				if (entry != null) {
					consumationEntryMgmt.storeConsumationEntry(entry);
					count++;
				}
			}
			
			addOrReplace(new Label("entryCount", "Es wurden " + count + " Buchungen erstellt."));
		}
		
		@SuppressWarnings("unused")
		public Date getAccountUntil() {
			return accountUntil;
		}

		@SuppressWarnings("unused")
		public void setAccountUntil(Date accountUntil) {
			this.accountUntil = accountUntil;
		}
		
	}
	
	private class CreateInvoicesForm extends Form<Object> {
		private static final long serialVersionUID = 1L;
		
		public CreateInvoicesForm(String id) {
			super(id);
			
			add(new Label("invoiceCount", ""));
		}
		
		public void onSubmit() {
			Iterator<Membership> members = membershipProvider.getMembersWithEntries();
			
			int count = 0;
			while (members.hasNext()) {
				Membership member = members.next();
				Invoice invoice = new Invoice(member);
				
				entryFromMembershipProvider.setMember(member);
				
				Iterator<ConsumationEntry> entries = entryFromMembershipProvider.iterator(0, entryFromMembershipProvider.size());
				while (entries.hasNext()) {
					invoice.addConsumationEntry(entries.next());
				}
				
				invoiceMgmt.storeInvoice(invoice);
				count++;
			}
			
			addOrReplace(new Label("invoiceCount", "Es wurden " + count + " Rechnungen erstellt."));
			
			// while alle members mit entries, die noch auf keine rechnung stehen
				// new invoice
				// alle entries hinzufügen
				// store
			
		}		
	}
	
}
