package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import javax.inject.Inject;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class SubscriptionDetailPage extends AdminBasePage {

	private Subscription subs;
	private Membership member;

	@Inject
	private PackageManagement packageMgmt;
	@Inject
	private MembershipManagement membershipMgmt;

	@Inject
	private SubscriptionManagement subscriptionMgmt;

	public SubscriptionDetailPage(Membership member, Subscription sub) {

		this.subs = sub;
		this.member = member;

		subs.setBookedBy(member);

		if (this.subs.getPaymentMethod() == null)
			this.subs.setPaymentMethod(this.member.getPaymentMethod());

		add(new SubscriptionForm("form"));

	}
	
	class SubscriptionForm extends Form<Object> {
		private static final long serialVersionUID = -416444319008642513L;

		public SubscriptionForm(String s) {
			super(s, new CompoundPropertyModel<Object>(subs));

			final RequiredTextField<BigDecimal> PriceOverruled = new RequiredTextField<BigDecimal>("priceOverruled", BigDecimal.class);
			add(PriceOverruled);

			final DropDownChoice<Package> availablePackages = new DropDownChoice<Package>("booksPackage", packageMgmt.getAllPackages()) {
				private static final long serialVersionUID = -385671748734684239L;

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}

				protected void onSelectionChanged(final Package p) {
					subs.setPriceOverruled(p.getPrice());

					PriceOverruled.setModelValue(new String[] { p.getPrice().toPlainString().replace(",", "") });
				}
			};
			add(availablePackages);

			final DateTextField validFrom = new DateTextField("ValidFrom", "dd.MM.yyyy");

			validFrom.setRequired(true);
			add(validFrom);

			MarkupContainer enclosure = new WebMarkupContainer("cancelSubscription") {
				private static final long serialVersionUID = 8577316899811450774L;

				public boolean isVisible() {
					return (subs.getValidTo() != null);
				}
			};

			final DateTextField ValidTo = new DateTextField("ValidTo", "dd.MM.yyyy");

			enclosure.add(ValidTo);
			add(enclosure);
			
			final DateTextField payedUntil = new DateTextField("payedUntil", "dd.MM.yyyy");
			add(payedUntil);

			DropDownChoice<PaymentMethod> payMeth = new DropDownChoice<PaymentMethod>("paymentMethod", Arrays.asList(PaymentMethod.values()), new EnumChoiceRenderer<PaymentMethod>());
			add(payMeth);

			final TextArea<String> details = new TextArea<String>("description");
			add(details);

			final Button btnSave = new Button("submit", Model.of("Speichern")) {
				private static final long serialVersionUID = -9206366064931940268L;

				public void onSubmit() {
					subscriptionMgmt.storeSubscription(subs);
					setResponsePage(new MembershipDetailPage(member, membershipMgmt, 1)); // Panel Pakete laden
				}
			};
			add(btnSave);

			/**
			 * Cancelation of Subscription
			 */
			if (subs.getValidTo() == null) {
				final Button btnCancelCancelation = new Button("cancelCancelation", Model.of("Paket kündigen")) {
					private static final long serialVersionUID = -9206366064931940268L;

					public void onSubmit() {
						subs.setValidTo(packageMgmt.getNextCancelationDate(subs.getBooksPackage()));
						setResponsePage(new SubscriptionDetailPage(member, subs));
					}
				};

				// Button nur bei einer bestehenden Subscription anzeigen
				btnCancelCancelation.setVisible(subs.getId() > 0);

				add(btnCancelCancelation);
			} else {
				final Button btnCancelCancelation = new Button("cancelCancelation", Model.of("Kündigung abbrechen")) {
					private static final long serialVersionUID = -9206366064931940268L;

					public void onSubmit() {
						subs.setValidTo(null);
						setResponsePage(new SubscriptionDetailPage(member, subs));
					}
				};

				// Button nur bei einer bestehenden Subscription anzeigen
				btnCancelCancelation.setVisible(subs.getId() > 0);

				add(btnCancelCancelation);
			}

			final Button btnDeleteSubscription = new Button("deleteSubscription", Model.of("Löschen")) {
				private static final long serialVersionUID = -9206366064931940268L;

				public void onSubmit() {
					subscriptionMgmt.removeMembership(subs);
					setResponsePage(new MembershipDetailPage(member, membershipMgmt, 1)); // Panel  Pakete laden
				}
			};

			btnDeleteSubscription.setVisible(subs.getId() > 0);
			add(btnDeleteSubscription);
		}
	}
}
