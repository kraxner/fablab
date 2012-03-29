package at.happylab.fablabtool.web.membership;

import java.util.Arrays;

import javax.inject.Inject;

import net.micalo.wicket.model.SmartModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import at.happylab.fablabtool.dao.MembershipDAO;
import at.happylab.fablabtool.dao.UserDAO;
import at.happylab.fablabtool.model.Gender;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipStatus;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.User;

public class DataPanel extends MembershipPanel{
	private static final long serialVersionUID = -5731106086301274951L;
	
	private static Log log = LogFactory.getLog(DataPanel.class);
	
	@Inject private UserDAO userDAO;
	@Inject private MembershipDAO membershipDAO;
	
	public DataPanel(String id, SmartModel<Membership> model) {
		super(id, model);
		add(new MemberForm("form", model));
	}

	/**
	 * Internal class for creating a edit-membership form
	 *  
	 * @author Michael Kraxner
	 *
	 */
	class MemberForm extends Form<Membership> {
		private static final long serialVersionUID = 1L;
		
		private final RepeatingView userListView;
		private final WebMarkupContainer container;
		private final AjaxButton addUserButton;

		/**
		 * Creates a edit-membership form with all its components for the given member
		 * 
		 * @param s
		 * @param member
		 */
		public MemberForm(String s,  SmartModel<Membership> model) {
			super(s, new CompoundPropertyModel<Membership>(model));
			
	        Membership membership = membershipModel.getObject();
			
//			FormComponentFeedbackBorder fbName = new FormComponentFeedbackBorder("feedbackName");
//			add(fbName);
			
			container = new WebMarkupContainer("container");
			container.setOutputMarkupId(true);

			container.add(new FeedbackPanel("feedback"));

			// select the type of the membership
			RadioChoice<MembershipType> memberTypeChoice = new RadioChoice<MembershipType>("membershipType", 
					Arrays.asList(MembershipType.values()), 
					new EnumChoiceRenderer<MembershipType>(this)).setSuffix("");
			
			memberTypeChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() { 
	            private static final long serialVersionUID = 1L; 

	            @Override 
	            protected void onUpdate(AjaxRequestTarget target) {
	            	// adjust the number of max allowed users for this type of membership 
	            	// - but we do not remove users beyond this number, this has to be done before persisting
	            	getModelObject().adjustMaxUser();
	                ;
	                target.addComponent(container);
	            } 
	        });
			// the choice is outside of the container!
			add(memberTypeChoice);

			// additional information about business membership
			final MarkupContainer businessContainer = new WebMarkupContainer("businessContainer") {
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isVisible() {
					configure();
					boolean isBusiness = getModelObject().getMembershipType() == MembershipType.BUSINESS;
					return isBusiness;
				}
			};
			businessContainer.add(new TextField<String>("companyName").setRequired(true));
			businessContainer.add(new TextField<String>("contactPerson").setRequired(true));
			businessContainer.add(new TextField<String>("companyEmail").add(EmailAddressValidator.getInstance()));
			businessContainer.add(new TextField<String>("companyPhone"));
			
			businessContainer.add(new TextField<String>("companyAddress.street"));	// in HTML code
			businessContainer.add(new TextField<String>("companyAddress.city"));
			businessContainer.add(new TextField<String>("companyAddress.zipCode"));

			businessContainer.setOutputMarkupPlaceholderTag(true);
			businessContainer.setOutputMarkupId(true);
			
			// user data
			userListView = new RepeatingView("users");
			userListView.setOutputMarkupId(true);
			userListView.setOutputMarkupPlaceholderTag(true);
			
			for (User u : membership.getUsers()) {
				buildUser(userListView, u);
			}
			businessContainer.add(userListView);
			
			addUserButton = new AjaxButton("addUserButton")
	        {
				private static final long serialVersionUID = 1L;

				@Override
	            protected void onSubmit(AjaxRequestTarget target, Form<?> f)
	            {
					User u = new User();
					
					membershipModel.getObject().addUser(u);
	                buildUser(userListView, u);
	                target.addComponent(container);
	            }
				
				@Override
				public boolean isVisible(){
					configure();
					// show this button only, if the maximum number of allowed users has not been reached yet
					Membership m = membershipModel.getObject();
					return m.getMaxUser() > m.getUsers().size();
				}
	        };
	        addUserButton.setOutputMarkupId(true);
	        addUserButton.setOutputMarkupPlaceholderTag(true);
	        businessContainer.add(addUserButton);

	        // add enclosure to container
	        container.add(businessContainer);
	        
	        // and non-profit container
			if (membership.getUsers().size() == 0) {
				membership.addUser(new User());
			}
			User user = membership.getUsers().get(0);
			final MarkupContainer nonProfitContainer = new WebMarkupContainer("non-profitContainer", new CompoundPropertyModel<User>(new SmartModel<User>(userDAO ,user) )) {
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isVisible() {
					configure();
					boolean isPrivate = membershipModel.getObject().getMembershipType() == MembershipType.PRIVATE;
					return isPrivate;
				}
			};
			addUserFields(nonProfitContainer);
			nonProfitContainer.setOutputMarkupId(true);
			nonProfitContainer.setOutputMarkupPlaceholderTag(true);
	        container.add(nonProfitContainer);
			
			// common information about membership
			DropDownChoice<MembershipStatus> memType = new DropDownChoice<MembershipStatus>("status",
					Arrays.asList(MembershipStatus.values()),
					new EnumChoiceRenderer<MembershipStatus>(this));
			memType.setRequired(true);
			//fbName.add(memType);
            container.add(memType);
            container.add(new DateTextField("entryDate", "dd.MM.yyyy"));
            
            DropDownChoice<PaymentMethod> payMeth = new DropDownChoice<PaymentMethod>("paymentMethod",
            		Arrays.asList(PaymentMethod.values()),
            		new EnumChoiceRenderer<PaymentMethod>(this));
            container.add(payMeth);
            container.add(new TextField<String>("bankDetails.name"));
            container.add(new TextField<String>("bankDetails.iban"));
            container.add(new TextField<String>("bankDetails.bic"));
            container.add(new TextArea<String>("comment"));
			
            // and finally add the configured container itself
			add(container);
			
			
			add(new Button("submit"));
		}
		
		/**
		 * 
		 * @see org.apache.wicket.Component#detachChildren()
		 */
		@Override
		public void detachModels()
		{
			log.debug("detaching models...");
			super.detachModels();
		}		
		/**
		 * 
		 * @see org.apache.wicket.Component#detachChildren()
		 */
		@Override
		protected void onSubmit() {
			membershipDAO.store(membershipModel.getObject());
			membershipDAO.commit();
			//membershipModel.detach();
		}

		/**
		 * Adds User related fields to the given <code>parent</code> component.
		 * 
		 * @param parent
		 */
		private void addUserFields(MarkupContainer parent) {
			
			parent.add(new TextField<Object>("firstname"));
			parent.add(new TextField<Object>("lastname"));
			
			// NOTE: we have to provide a model here. 
			DropDownChoice<Gender> gender = new DropDownChoice<Gender>("gender",
					Arrays.asList(Gender.values()), 
					new EnumChoiceRenderer<Gender>());
			parent.add(gender);
            parent.add(new DateTextField("birthday"));
			parent.add(new TextField<String>("email").add(EmailAddressValidator.getInstance()));
			parent.add(new TextField<String>("phone"));
			parent.add(new TextField<String>("address.street"));	// in HTML code
			parent.add(new TextField<String>("address.city"));
			parent.add(new TextField<String>("address.zipCode"));
		}
		
		/**
		 * Creates a new component for displaying a user
		 * and adds it to the component-list view
		 *  
		 * @param view
		 * @param user
		 * @return
		 */
		private Component buildUser(final RepeatingView view, final User user) {
			// important: do NOT use the id of the entity itself, as it can be reset by the repeater
			// (this will result into problems with hibernate!) let the repeater generate one instead 
			final WebMarkupContainer item = new WebMarkupContainer(view.newChildId(), new CompoundPropertyModel<User>(user));
			
			item.setOutputMarkupId(true);
			view.add(item);
			item.add(new Label("fullname"));
			addUserFields(item);
			
			item.add(new AjaxButton("removeUserButton")
	        {
				private static final long serialVersionUID = 1L;

				@Override
	            protected void onSubmit( AjaxRequestTarget target, Form<?> f)
				{
					// remove the related component from the view
					view.remove(item);
					// and also the user from the list
					membershipModel.getObject().removeUser(user);
	                target.addComponent(container);
	            }
				
				@Override
				public boolean isVisible(){
					// we want to keep at least one user
					return membershipModel.getObject().getUsers().size() > 1;
				}
	        });
			
			return item;
		}		
	}
}
