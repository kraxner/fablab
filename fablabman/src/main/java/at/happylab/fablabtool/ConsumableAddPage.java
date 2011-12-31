package at.happylab.fablabtool;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.model.Consumable;


public class ConsumableAddPage extends BasePage {

	@Inject
	private EntityManager em;
	private Consumable consumable;

	public ConsumableAddPage(Consumable cons) {
		navigation.selectStammdaten();
		consumable = cons;

		add(new ConsumableForm("form", cons, getDefaultModel()));
	}

	class ConsumableForm extends Form {
		public ConsumableForm(String s, Consumable cons, IModel m) {
			super(s, new CompoundPropertyModel(cons));

			final TextField<String> name = new TextField<String>("name");
			name.setRequired(true);
			name.add(StringValidator.maximumLength(50));
			add(name);

			final TextField<BigDecimal> price = new TextField<BigDecimal>("pricePerUnit");
			price.setRequired(true);
			add(price);
			
			final TextField<String> unit = new TextField<String>("unit");
			unit.setRequired(true);
			unit.add(StringValidator.maximumLength(50));
			add(unit);
			
		}

		public void onSubmit() {
			em.getTransaction().begin();
			em.persist(consumable);
			em.getTransaction().commit();

			setResponsePage(new ConsumableListPage());
		}
	}

}
