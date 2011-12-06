package at.happylab.fablabtool;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;

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
            
            final TextField name = new TextField("name");
            name.setRequired(true);
            name.add(StringValidator.maximumLength(15));
            add(name);
            
            final TextArea description = new TextArea("description");
            add(description);
            
            final TextField<BigDecimal> price = new TextField<BigDecimal>("price");
            add(price);
            
        }

        public void onSubmit() {
        	em.getTransaction().begin();
    		em.persist(pkg);
    		em.getTransaction().commit();
        	
    		setResponsePage(new PackageList());
        }
    }
}
