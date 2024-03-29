package at.happylab.fablabtool.web.maintenance;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.micalo.persistence.dao.BaseDAO;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.model.Consumable;
import at.happylab.fablabtool.web.BasePage;

public class ConsumableAddPage extends BasePage {

	@Inject private EntityManager em;
	private BaseDAO<Consumable> consumableDAO = new BaseDAO<Consumable>(Consumable.class, em);
	
	private Consumable consumable;

	public ConsumableAddPage(Consumable cons) {
		navigation.selectMasterData();
		consumable = cons;

		if (consumable.getId() == 0)
			add(new Label("pageHeader", "Neues Consumable"));
		else
			add(new Label("pageHeader", "Consumable bearbeiten"));
		
		add(new FeedbackPanel("feedback"));

		add(new ConsumableForm("form", cons));
	}

	class ConsumableForm extends Form<Consumable> {
		private static final long serialVersionUID = -7376832711919411830L;

		public ConsumableForm(String s, Consumable cons) {
			super(s, new CompoundPropertyModel<Consumable>(cons));

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
			
			Link<String> goBackButton = new Link<String>("goBack") {
				private static final long serialVersionUID = -3527050342774869192L;

				public void onClick() {
					setResponsePage(new ConsumableListPage());
				}
			};
			add(goBackButton);

		}

		public void onSubmit() {
			consumableDAO.store(consumable);
			consumableDAO.commit();
			setResponsePage(new ConsumableListPage());
		}
	}

}
