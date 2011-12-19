package at.happylab.fablabtool.web.accessgrant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Gender;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PackageType;

public class KeycardDetailPage extends BasePage {

	@Inject
	private EntityManager em;
	private KeyCard kc;

	public KeycardDetailPage(KeyCard k) {
		navigation.selectStammdaten();
		kc = k;

		add(new KeycardForm("form", k, getDefaultModel()));
	}

	class KeycardForm extends Form {
		public KeycardForm(String s, KeyCard k, IModel m) {
			super(s, new CompoundPropertyModel(k));
			
			final CheckBox activeYN = new CheckBox("active");
			add(activeYN);

			final TextArea<String> description = new TextArea<String>("description");
			description.setRequired(false);
			add(description);

			final RequiredTextField<String> rfid = new RequiredTextField<String>("rfid");
			rfid.add(StringValidator.maximumLength(50));
			add(rfid);


		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(kc);
			em.getTransaction().commit();

			setResponsePage(new KeycardListPage());
		}
	}
}
