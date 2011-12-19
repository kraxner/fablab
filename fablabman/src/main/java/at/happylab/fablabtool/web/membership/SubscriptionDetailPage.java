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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.Subscription;

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

		add(new SubscriptionForm("form", this.subs, cancelSubscription));

	}

	class SubscriptionForm extends Form<Object> {
		private static final long serialVersionUID = -416444319008642513L;

		public SubscriptionForm(String s, final Subscription sub,
				final boolean cancelSubscription) {
			super(s, new CompoundPropertyModel<Object>(sub));

			final RequiredTextField<BigDecimal> PriceOverruled = new RequiredTextField<BigDecimal>(
					"priceOverruled", BigDecimal.class);
			add(PriceOverruled);

			final DropDownChoice<Package> availablePackages = new DropDownChoice<Package>(
					"booksPackage", packageMgmt.getAllPackages()) {

				private static final long serialVersionUID = -385671748734684239L;

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}

				protected void onSelectionChanged(final Package p) {
					sub.setPriceOverruled(p.getPrice());

					PriceOverruled.setModelValue(new String[] { p.getPrice()
							.toPlainString().replace(",", "") });
				}
			};

			add(availablePackages);

			final DateTextField validFrom = new DateTextField("ValidFrom");
			validFrom.setRequired(true);
			add(validFrom);

			@SuppressWarnings("serial")
			MarkupContainer enclosure = new WebMarkupContainer(
					"cancelSubscription") {
				public boolean isVisible() {
					return cancelSubscription || (sub.getValidTo() != null);
				}
			};

			final DateTextField ValidTo = new DateTextField("ValidTo");
			enclosure.add(ValidTo);
			add(enclosure);

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
			
			final Link btnCancelCancelation = new Link("cancelCancelation") {
	            public void onClick() {
	            	sub.setValidTo(null);
	            	onSubmit();
	            }
	        };
	        btnCancelCancelation.setVisible(sub.getValidTo() != null);
	        
			add(btnCancelCancelation);
		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(subs);
			em.getTransaction().commit();

			setResponsePage(new MembershipDetailPage(member, membershipMgmt));
		}
	}
}
