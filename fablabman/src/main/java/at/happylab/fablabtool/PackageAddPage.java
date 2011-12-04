package at.happylab.fablabtool;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.model.Package;

class PackageAddPage extends BasePage {
	
	public PackageAddPage(Package p) {

		add(new PackageForm("form", p, getDefaultModel()));
	}
	
	class PackageForm extends Form {
        public PackageForm(String s, Package p, IModel m) {
            super(s, new CompoundPropertyModel(p));
            
            final TextField name = new TextField("name");
            name.setRequired(true);
            name.add(StringValidator.maximumLength(15));
            add(name);
        }

        public void onSubmit() {
            //if (!peopleList.contains(person)) {
            //    peopleList.add(person);
           //}
            
        	//setResponsePage(new PersonList(peopleList));
        }
    }
}
