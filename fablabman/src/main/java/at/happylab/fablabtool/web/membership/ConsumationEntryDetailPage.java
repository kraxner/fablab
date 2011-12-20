package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class ConsumationEntryDetailPage extends AdminBasePage {

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
