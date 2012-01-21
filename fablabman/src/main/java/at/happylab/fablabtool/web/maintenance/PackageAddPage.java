package at.happylab.fablabtool.web.maintenance;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PackageType;
import at.happylab.fablabtool.model.TimePeriod;
import at.happylab.fablabtool.web.BasePage;

public class PackageAddPage extends BasePage {

	@Inject
	private PackageManagement packageMgmt;
	private Package pkg;

	public PackageAddPage(Package p) {
		navigation.selectMasterData();
		pkg = p;

		if (pkg.getId() == 0)
			add(new Label("pageHeader", "Neues Paket"));
		else
			add(new Label("pageHeader", "Paket bearbeiten"));
		
		add(new FeedbackPanel("feedback"));

		add(new PackageForm("form", p));
	}

	class PackageForm extends Form<Package> {
		private static final long serialVersionUID = 9185780707344670737L;

		public PackageForm(String s, Package p) {
			super(s, new CompoundPropertyModel<Package>(p));

			final TextField<String> name = new TextField<String>("name");
			name.setRequired(true);
			name.add(StringValidator.maximumLength(50));
			add(name);

			final TextArea<String> description = new TextArea<String>("description");
			description.setRequired(false);
			add(description);

			final RequiredTextField<BigDecimal> price = new RequiredTextField<BigDecimal>("price");
			add(price);

			DropDownChoice<PackageType> packageType = new DropDownChoice<PackageType>("PackageType", Arrays.asList(PackageType.values()), new EnumChoiceRenderer<PackageType>());
			packageType.setRequired(true);
			add(packageType);

			DropDownChoice<TimePeriod> paymentCycle = new DropDownChoice<TimePeriod>("billingCycle", Arrays.asList(TimePeriod.values()), new EnumChoiceRenderer<TimePeriod>());
			paymentCycle.setRequired(true);
			add(paymentCycle);

			DropDownChoice<TimePeriod> cancelationPeriodAdvance = new DropDownChoice<TimePeriod>("cancelationPeriodAdvance", Arrays.asList(TimePeriod.values()), new EnumChoiceRenderer<TimePeriod>());
			cancelationPeriodAdvance.setRequired(true);
			add(cancelationPeriodAdvance);

			final RequiredTextField<Integer> cancellationPeriod = new RequiredTextField<Integer>("cancelationPeriod");
			cancellationPeriod.setRequired(true);
			add(cancellationPeriod);
			
			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new MasterDataPage());
				}
			};
			add(goBackButton);

		}

		public void onSubmit() {
			packageMgmt.storePackage(pkg);
			setResponsePage(new PackageList());
		}
	}
}
