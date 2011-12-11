package at.happylab.fablabtool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Gender;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PackageType;

class PackageAddPage extends BasePage {

	@Inject
	private EntityManager em;
	private Package pkg;

	public PackageAddPage(Package p) {
		navigation.selectStammdaten();
		pkg = p;

		add(new PackageForm("form", p, getDefaultModel()));
	}

	class PackageForm extends Form {
		public PackageForm(String s, Package p, IModel m) {
			super(s, new CompoundPropertyModel(p));

			final TextField<String> name = new TextField<String>("name");
			name.setRequired(true);
			name.add(StringValidator.maximumLength(15));
			add(name);

			final TextArea<String> description = new TextArea<String>("description");
			description.setRequired(true);
			add(description);

			final RequiredTextField<BigDecimal> price = new RequiredTextField<BigDecimal>(
					"price");
			add(price);

			List<PackageType> l = new ArrayList<PackageType>();
			l.add(PackageType.MEMBERSHIP);
			l.add(PackageType.ACCESS);
			l.add(PackageType.STORAGE);
			DropDownChoice<PackageType> packageType = new DropDownChoice<PackageType>( "PackageType", new PropertyModel<PackageType>(p, "PackageType"), l);
			add(packageType);

			DropDownChoice<Integer> paymentCycle = new DropDownChoice<Integer>(
					"billingCycle",
					Arrays.asList(1, 3, 12));
			paymentCycle.setRequired(true);
			add(paymentCycle);
			
			DropDownChoice<Integer> cancellationPeriod = new DropDownChoice<Integer>(
					"cancelationPeriod",
					Arrays.asList(1, 3, 12));
			cancellationPeriod.setRequired(true);
			add(cancellationPeriod);
			
			final RequiredTextField<Integer> cancellationPeriodAdvance = new RequiredTextField<Integer>("cancelationPeriodAdvance");
			cancellationPeriodAdvance.setRequired(true);
			add(cancellationPeriodAdvance);

		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(pkg);
			em.getTransaction().commit();

			setResponsePage(new PackageList());
		}
	}
}
