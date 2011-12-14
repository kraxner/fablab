package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.model.Package;

class SubscriptionDetailPage extends BasePage {

	@Inject
	private EntityManager em;
	private Subscription subs;
	private Membership member;

	@Inject
	private PackageManagement packageMgmt;
	@Inject
	private MembershipManagement membershipMgmt;

	@Inject
	private SubscriptionManagement subscriptionMgmt;

	public SubscriptionDetailPage(Membership member,
			MembershipManagement membershipMgmt, Subscription sub,
			boolean cancelSubscription) {

		this.subs = sub;
		this.member = member;

		subs.setBookedBy(member);

		if (cancelSubscription)
			sub.setValidTo(subscriptionMgmt.getEarliestCancelationDate(subs));

		if (this.subs.getPaymentMethod() == null)
			this.subs.setPaymentMethod(this.member.getPaymentMethod());

		add(new SubscriptionForm("form", sub, cancelSubscription));
	}

	public SubscriptionDetailPage(Membership member,
			MembershipManagement membershipMgmt, Subscription sub) {

		this(member, membershipMgmt, sub, false);

	}

	class SubscriptionForm extends Form {
		private static final long serialVersionUID = -7480286477673641461L;

		public SubscriptionForm(String s, final Subscription sub,
				final boolean cancelSubscription) {
			super(s, new CompoundPropertyModel<Object>(sub));

			add(new DropDownChoice<Package>("booksPackage",
					packageMgmt.getAllPackages()) {

				private static final long serialVersionUID = -385671748734684239L;

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}
				
				/*
				 * When selecting a package the price of the subscription is supposed to change.
				 */
				protected void onSelectionChanged(final Package p) {
					sub.setPriceOverruled(p.getPrice());	// TODO: Doesn't work this way
				}
			});

			final DateTextField validFrom = new DateTextField("ValidFrom");
			validFrom.setRequired(true);
			add(validFrom);

			MarkupContainer enclosure = new WebMarkupContainer(
					"cancelSubscription") {

				private static final long serialVersionUID = -2812361648172827138L;

				public boolean isVisible() {
					return cancelSubscription;
				}
			};
			
			final DateTextField ValidTo = new DateTextField("ValidTo");
			enclosure.add(ValidTo);
			add(enclosure);

			final RequiredTextField<BigDecimal> PriceOverruled = new RequiredTextField<BigDecimal>(
					"PriceOverruled");
			add(PriceOverruled);

			DropDownChoice<PaymentMethod> payMeth = new DropDownChoice<PaymentMethod>(
					"paymentMethod");
			payMeth.setChoices(new LoadableDetachableModel<List<PaymentMethod>>() {
				private static final long serialVersionUID = 4420436576098934666L;

				public List<PaymentMethod> load() {
					List<PaymentMethod> list = new ArrayList<PaymentMethod>(3);
					list.add(PaymentMethod.DEBIT);
					list.add(PaymentMethod.CASH_IN_ADVANCE);
					list.add(PaymentMethod.ON_ACCOUNT);
					return list;
				}
			});

			add(payMeth);

			add(new Button("submit"));
		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(subs);
			em.getTransaction().commit();

			setResponsePage(new MembershipDetailPage(member, membershipMgmt));
		}
	}
}
