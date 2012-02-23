package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.dao.BaseDAO;
import net.micalo.wicket.model.EntityNotFoundException;
import net.micalo.wicket.model.SmartModel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dao.MembershipDAO;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class SubscriptionDetailPage extends AdminBasePage {

	private SmartModel<Subscription> subscriptionModel;

	@Inject private MembershipDAO membershipDAO;

	@Inject private EntityManager em;
	
	private BaseDAO<Package> packageDAO = new BaseDAO<Package>(Package.class, em);
	private BaseDAO<Subscription> subscriptionDAO = new BaseDAO<Subscription>(Subscription.class, em);

	public SubscriptionDetailPage(PageParameters params) {
		if (params.containsKey("id")) {
		    long id = params.getLong("id");
	    	Subscription subscription = subscriptionDAO.load(id);
	    	if (subscription == null) {
	    		throw new EntityNotFoundException("Subscription id: " + id);
	    	}
		    subscriptionModel = new SmartModel<Subscription>(subscriptionDAO, subscription);
		} else if (params.containsKey("membershipId")) {
			// a new subscription for the given membership
			Membership membership = membershipDAO.load(params.getLong("membershipId"));
			if (membership == null) {
				throw new EntityNotFoundException("Cannot create new subscription, membership not found. id: " + params.getLong("membershipId"));
			}
		    subscriptionModel = new SmartModel<Subscription>(subscriptionDAO, new Subscription(membership));
		}
		add(new Label("pageHeader", "Subscription bearbeiten"));
		add(new FeedbackPanel("feedback"));
		add(new SubscriptionForm("form"));
	}
	
	public SubscriptionDetailPage(SmartModel<Subscription> sub) {

		this.subscriptionModel = sub;
		
		if (subscriptionModel.getObject().getIdent() == null)
			add(new Label("pageHeader", "Neue Subscription"));
		else
			add(new Label("pageHeader", "Subscription bearbeiten"));
		
		add(new FeedbackPanel("feedback"));

		add(new SubscriptionForm("form"));
	}
	
	class SubscriptionForm extends Form<Subscription> {
		private static final long serialVersionUID = 1L;

		public SubscriptionForm(String s) {
			super(s, new CompoundPropertyModel<Subscription>(subscriptionModel));
			
			Subscription subscription = subscriptionModel.getObject();

			final RequiredTextField<BigDecimal> PriceOverruled = new RequiredTextField<BigDecimal>("priceOverruled", BigDecimal.class);
			add(PriceOverruled);

			final DropDownChoice<Package> availablePackages = new DropDownChoice<Package>("booksPackage", packageDAO.getAll()) {
				private static final long serialVersionUID = 1L;

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}

				protected void onSelectionChanged( Package p) {
					subscriptionModel.getObject().setPriceOverruled(p.getPrice());
					PriceOverruled.setModelValue(new String[] { p.getPrice().toPlainString().replace(",", "") });
				}
			};
			add(availablePackages);

			final DateTextField validFrom = new DateTextField("ValidFrom", "dd.MM.yyyy");

			validFrom.setRequired(true);
			add(validFrom);

			MarkupContainer enclosure = new WebMarkupContainer("cancelSubscription") {
				private static final long serialVersionUID = 1L;

				public boolean isVisible() {
					return (subscriptionModel.getObject().getValidTo() != null);
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
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					Subscription sub = subscriptionModel.getObject();
					subscriptionDAO.store(sub);
					subscriptionDAO.commit();
					setResponsePage(MembershipDetailPage.class, new PageParameters("id="+sub.getBookedBy().getId() + ",tab=1")); // Panel Pakete laden
				}
			};
			add(btnSave);

			/**
			 * Cancelation of Subscription
			 */
			final Button btnCancelCancelation;
			// FIXME: display a different label depending on validTo should be done differently
			if (subscription.getValidTo() == null) {
				btnCancelCancelation = new Button("cancelCancelation", Model.of("Paket kündigen")) {
					private static final long serialVersionUID = 1L;

					public void onSubmit() {
						subscriptionModel.getObject().setValidTo(subscriptionModel.getObject().getBooksPackage().getNextCancelationDate());
						// that is not necessary, is it? we are already on this page
						//setResponsePage(new SubscriptionDetailPage(member, subscriptionModel));
					}
				};

			} else {
				btnCancelCancelation = new Button("cancelCancelation", Model.of("Kündigung abbrechen")) {
					private static final long serialVersionUID = 1L;

					public void onSubmit() {
						subscriptionModel.getObject().setValidTo(null);
						// that is not necessary, is it? we are already on this page
						//setResponsePage(new SubscriptionDetailPage(member, subscriptionModel));
					}
				};

			}
			add(btnCancelCancelation);
			// Button nur bei einer bestehenden Subscription anzeigen
			btnCancelCancelation.setVisible(subscription.getId() > 0);

			final Button btnDeleteSubscription = new Button("deleteSubscription", Model.of("Löschen")) {
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					Subscription sub = subscriptionModel.getObject();
					subscriptionDAO.remove(sub);
					subscriptionDAO.commit();
					setResponsePage(MembershipDetailPage.class, new PageParameters("id="+sub.getBookedBy().getId() + ",tab=1")); // Panel Pakete laden
				}
			};

			btnDeleteSubscription.setVisible(subscription.getId() > 0);
			add(btnDeleteSubscription);
		}
	}
}
