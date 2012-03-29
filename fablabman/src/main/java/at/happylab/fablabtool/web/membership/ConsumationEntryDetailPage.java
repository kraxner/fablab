package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.dao.BaseDAO;
import net.micalo.wicket.model.EntityNotFoundException;
import net.micalo.wicket.model.SmartModel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dao.MembershipDAO;
import at.happylab.fablabtool.model.Consumable;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.web.authentication.AdminBasePage;
import at.happylab.fablabtool.web.membership.SubscriptionDetailPage.SubscriptionForm;

public class ConsumationEntryDetailPage extends AdminBasePage {
	
	private SmartModel<ConsumationEntry> consumationEntryModel;
	
	@Inject private MembershipDAO membershipDAO;
	
	@Inject private EntityManager em;
	
	private BaseDAO<Consumable> consumableDAO = new BaseDAO<Consumable>(Consumable.class, em);
	private BaseDAO<ConsumationEntry> consumationEntryDAO = new BaseDAO<ConsumationEntry>(ConsumationEntry.class, em);

	public ConsumationEntryDetailPage(PageParameters params)
	{
		if (params.containsKey("id")) {
		    long id = params.getLong("id");
	    	
		    ConsumationEntry entry = consumationEntryDAO.load(id);
	    	if (entry == null) {
	    		throw new EntityNotFoundException("ConsumationEntry id: " + id);
	    	}
		    consumationEntryModel = new SmartModel<ConsumationEntry>(consumationEntryDAO, entry);
		    
		    add(new Label("pageHeader", "Buchung bearbeiten"));
		} 
		else if (params.containsKey("membershipId")) {
			// a new subscription for the given membership
			Membership membership = membershipDAO.load(params.getLong("membershipId"));
			if (membership == null) {
				throw new EntityNotFoundException("Cannot create new subscription, membership not found. id: " + params.getLong("membershipId"));
			}
			consumationEntryModel = new SmartModel<ConsumationEntry>(consumationEntryDAO, new ConsumationEntry(membership));
			
			add(new Label("pageHeader", "Neue Buchung"));
		}
		
		add(new ConsumationEntryForm("form"));
	}
	
	public ConsumationEntryDetailPage(SmartModel<ConsumationEntry> model) {
		this.consumationEntryModel = model;

		if (consumationEntryModel.getObject().getIdent() == null) {
			add(new Label("pageHeader", "Neue Buchung"));
		}
		else {
			add(new Label("pageHeader", "Buchung bearbeiten"));
		}
		
		add(new ConsumationEntryForm("form"));
	}
	
	class ConsumationEntryForm extends Form<ConsumationEntry> {
		private static final long serialVersionUID = -7480286477673641461L;

		public ConsumationEntryForm(String s) {
			super(s, new CompoundPropertyModel<ConsumationEntry>(consumationEntryModel));
			
			final RequiredTextField<Date> date = new RequiredTextField<Date>("date");
			add(date);
			
			final RequiredTextField<String> text = new RequiredTextField<String>("text", String.class);
			add(text);

			final RequiredTextField<BigDecimal> price = new RequiredTextField<BigDecimal>("price", BigDecimal.class);
			add(price);
			
			final TextField<Double> unit = new TextField<Double>("unit");
			add(unit);
			
			final DropDownChoice<Consumable> availableConsumables = new DropDownChoice<Consumable>("consumedItem", consumableDAO.getAll()) {
				private static final long serialVersionUID = 1L;

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
					ConsumationEntry entry = consumationEntryModel.getObject();
					
					consumationEntryDAO.store(entry);
					consumationEntryDAO.commit();
					setResponsePage(MembershipDetailPage.class, new PageParameters("id=" +  entry.getConsumedBy().getId() +",tab=2"));
				}
				
			};
			add(btnSaveEntry);
			
			final Button btnDeleteEntry = new Button("deleteEntry", Model.of("Löschen")) {
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					ConsumationEntry entry = consumationEntryModel.getObject();
					
					consumationEntryDAO.remove(entry);
					consumationEntryDAO.commit();
					setResponsePage(MembershipDetailPage.class, new PageParameters("id=" +  entry.getConsumedBy().getId() +",tab=2")); // Panel Buchungen laden
				}
			};
			
			if (consumationEntryModel.getObject().getIdent() != null) 
				btnDeleteEntry.setVisible(true);
			else 
				btnDeleteEntry.setVisible(false);
			
			add(btnDeleteEntry);
			
		}
	}
	
}
