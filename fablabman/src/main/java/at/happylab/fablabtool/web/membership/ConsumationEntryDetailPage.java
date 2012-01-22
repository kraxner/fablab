package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.ConsumableManagement;
import at.happylab.fablabtool.beans.ConsumationEntryManagement;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Consumable;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class ConsumationEntryDetailPage extends AdminBasePage {
	
	private ConsumationEntry entry;
	private Membership member;

	@Inject
	private ConsumableManagement consumableMgmt;
	
	@Inject
	private MembershipManagement membershipMgmt;

	@Inject
	private ConsumationEntryManagement consumationEntryMgmt;

	public ConsumationEntryDetailPage(Membership member,
			MembershipManagement membershipMgmt, ConsumationEntry entry) {

		this.entry= entry;
		this.member = member;
		

		entry.setConsumedBy(member);

		if (entry.getId() == 0)
			add(new Label("pageHeader", "Neue Buchung"));
		else
			add(new Label("pageHeader", "Buchung bearbeiten"));
		
		add(new ConsumationEntryForm("form"));
	}
	
	class ConsumationEntryForm extends Form<ConsumationEntry> {
		private static final long serialVersionUID = -7480286477673641461L;

		public ConsumationEntryForm(String s) {
			super(s, new CompoundPropertyModel<ConsumationEntry>(entry));
			
			final RequiredTextField<Date> date = new RequiredTextField<Date>("date");
			add(date);
			
			final RequiredTextField<String> text = new RequiredTextField<String>("text", String.class);
			add(text);

			final RequiredTextField<BigDecimal> price = new RequiredTextField<BigDecimal>("price", BigDecimal.class);
			add(price);
			
			final TextField<Double> unit = new TextField<Double>("unit");
			add(unit);
			
			final DropDownChoice<Consumable> availableConsumables = new DropDownChoice<Consumable>("consumedItem", consumableMgmt.getAllConsumables()) {
				private static final long serialVersionUID = -385671748734684239L;

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}

				protected void onSelectionChanged(final Consumable c) {
					text.setModelValue(new String[] { c.getName() } );

					price.setModelValue(new String[] { c.getPricePerUnit().toString() } );
					unit.setModelValue(new String[] { c.getUnit() });

//					price.setModelValue(new String[] { c.getPricePerUnit().toPlainString().replace(",", "") } );
				}
			};
			add(availableConsumables);
			
			final RequiredTextField<Integer> quantity = new RequiredTextField<Integer>("quantity");
			add(quantity);
			
			final Button btnSaveEntry = new Button("submit", Model.of("Save")) {
				private static final long serialVersionUID = 1L;
				
				public void onSubmit() {
					consumationEntryMgmt.storeConsumationEntry(entry);
					setResponsePage(new MembershipDetailPage(member, membershipMgmt, 2));
				}
				
			};
			add(btnSaveEntry);
			
			final Button btnDeleteEntry = new Button("deleteEntry", Model.of("Löschen")) {
				private static final long serialVersionUID = -9206366064931940268L;

				public void onSubmit() {
					consumationEntryMgmt.removeEntry(entry);
					setResponsePage(new MembershipDetailPage(member, membershipMgmt, 2)); // Panel Buchungen laden
				}
			};
			
			if (entry.getId() > 0) 
				btnDeleteEntry.setVisible(true);
			else 
				btnDeleteEntry.setVisible(false);
			
			add(btnDeleteEntry);
			
		}
	}
	
}
