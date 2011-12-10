package at.happylab.fablabtool.web.membership;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.model.Membership;
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

	public SubscriptionDetailPage(Membership member,
			MembershipManagement membershipMgmt, Subscription sub) {

		this.subs = sub;
		this.member = member;

		subs.setBookedBy(member);

		add(new SubscriptionForm("form", sub, getDefaultModel()));
	}

	class SubscriptionForm extends Form {
		private static final long serialVersionUID = -7480286477673641461L;

		public SubscriptionForm(String s, Subscription sub, IModel m) {
			super(s);

			final DropDownChoice<Package> packages = new DropDownChoice<Package>(
					"booksPackage", packageMgmt.getAllPackages());
			packages.setRequired(true);
			add(packages);

			final DateTextField validFrom = new DateTextField("ValidFrom");
			validFrom.setRequired(true);
			add(validFrom);

			final DateTextField ValidTo = new DateTextField("ValidTo");
			ValidTo.setRequired(true);
			add(ValidTo);
			
			final RequiredTextField PriceOverruled = new RequiredTextField(
					"PriceOverruled");
			add(PriceOverruled);

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
