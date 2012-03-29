package at.happylab.fablabtool.web.membership;

import javax.inject.Inject;

import net.micalo.wicket.model.SmartModel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;

import at.happylab.fablabtool.dao.MembershipDAO;
import at.happylab.fablabtool.model.Membership;

public class InternalCommentsPanel extends MembershipPanel {
	private static final long serialVersionUID = 1L;
	
	@Inject MembershipDAO membershipDAO;

	class InternalCommentForm extends Form<Membership> {
		private static final long serialVersionUID = 1L;

		public InternalCommentForm(String id, SmartModel<Membership> model) {
			super(id, new CompoundPropertyModel<Membership>(model));
			
			add(new TextArea<String>("internalComment"));
			
			add(new Button("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					membershipDAO.store(membershipModel.getObject());
					membershipDAO.commit();
				}
			});
			
		}
		
	}
	/**
	 * Constructs a panel including an edit-membership-form for the given membership
	 * 
	 * @param id
	 * @param membership
	 * @param membershipMgmt
	 */
	public InternalCommentsPanel(String id, SmartModel<Membership> model) {
		super(id, model);
		
		InternalCommentForm form = new InternalCommentForm("form", membershipModel);
		add(form);
	}

}
