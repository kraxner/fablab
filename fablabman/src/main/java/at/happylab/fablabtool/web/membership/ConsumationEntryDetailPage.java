package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.web.membership.SubscriptionDetailPage.SubscriptionForm;

public class ConsumationEntryDetailPage extends BasePage {

	@Inject
	private EntityManager em;
	
	private ConsumationEntry entry;
	private Membership member;

	@Inject
	private PackageManagement packageMgmt;
	@Inject
	private MembershipManagement membershipMgmt;

	@Inject
	private SubscriptionManagement subscriptionMgmt;

	public ConsumationEntryDetailPage(Membership member,
			MembershipManagement membershipMgmt, ConsumationEntry entry) {

		this.entry= entry;
		this.member = member;

		entry.setConsumedBy(member);

		add(new ConsumationEntryForm("form", entry));
	}
	
	class ConsumationEntryForm extends Form {
		private static final long serialVersionUID = -7480286477673641461L;

		public ConsumationEntryForm(String s, final ConsumationEntry entry) {
			super(s, new CompoundPropertyModel<Object>(entry));
			
			final RequiredTextField<Date> date = new RequiredTextField<Date>("date");
			add(date);
			
			final RequiredTextField<String> text = new RequiredTextField<String>("text");
			add(text);
			
			final RequiredTextField<BigDecimal> price = new RequiredTextField<BigDecimal>("price");
			add(price);
			
			final RequiredTextField<Integer> quantity = new RequiredTextField<Integer>("quantity");
			add(quantity);
			
			add(new Button("submit"));
		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(entry);
			em.getTransaction().commit();

			setResponsePage(new MembershipDetailPage(member, membershipMgmt));
		}
	}
	
}
