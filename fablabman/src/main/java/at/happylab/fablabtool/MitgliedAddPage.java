package at.happylab.fablabtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.User;

public class MitgliedAddPage extends BasePage{
	
	@Inject private MembershipManagement membershipMgmt;
	
	public MitgliedAddPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
                return new User();
            }
        }));
		init();
	}
	
	private void init(){
		add(new FeedbackPanel("feedback"));
		add(new UserForm("form", getDefaultModel()));
	}
	
	private class UserForm extends Form {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2239827660818352563L;

		public UserForm(String id, IModel m) {
            super(id, m);
            
            final TextField firstname = new TextField("firstname");
            firstname.setRequired(true);
            firstname.add(StringValidator.maximumLength(15));
            add(firstname);

            final TextField lastname = new TextField("lastname");
            lastname.setRequired(true);
            lastname.add(StringValidator.maximumLength(20));
            add(lastname);
            
            SelectOption[] options = new SelectOption[] {new SelectOption("FEMALE", "weiblich"), new SelectOption("MALE", "männlich")};
            ChoiceRenderer choiceRenderer = new ChoiceRenderer("value", "key");
            add(new DropDownChoice("gender", new Model<String>(), Arrays.asList(options), choiceRenderer));
            
            /*DropDownChoice<String> gender = new DropDownChoice<String>("gender");
            gender.setChoices(new LoadableDetachableModel<List<String>>() {
				public List<String> load() {
                    List<String> l = new ArrayList<String>(2);
                    l.add("weiblich");
                    l.add("männlich");
                    return l;
                }
            });*/
            //add(gender);
            
            //TODO birthdate datatype decided?
            
            final TextField email = new TextField("email");
            email.setRequired(true);
            email.add(StringValidator.maximumLength(150));
            email.add(EmailAddressValidator.getInstance());
            add(email);

            final TextField mobile = new TextField("mobile");
            mobile.setRequired(true);
            mobile.add(StringValidator.maximumLength(20));
            add(mobile);
            
            final Component newMemberButton = new Button("newMember") {

    			@Override
    			public void onSubmit() {
    				User user;// = new User();
    				user = (User) getForm().getModelObject();
    				//user.setFirstname((String)firstname.getModelObject());
    				//user.setLastname((String)lastname.getModelObject());
    				membershipMgmt.addMembership(user);
    				setResponsePage(MitgliedAddPage.class);
    			}
    		};
    		add(newMemberButton);
        }
	}

}
