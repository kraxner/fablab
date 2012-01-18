package at.happylab.fablabtool.web.membership;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;

public class InternalCommentsPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private MembershipManagement membershipMgmt;
	private Membership membership;

	class InternalCommentForm extends Form<Membership> {
		private static final long serialVersionUID = 1L;

		public InternalCommentForm(String id, final Membership membership) {
			super(id, new CompoundPropertyModel<Membership>(membership));
			
			add(new TextArea<String>("internalComment"));
			
			add(new Button("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					membershipMgmt.storeMembership(membership);;
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
	public InternalCommentsPanel(String id, Membership member,  MembershipManagement membershipMgmt) {
		super(id);
		
		this.membership = member;
		this.membershipMgmt = membershipMgmt;
		
		InternalCommentForm form = new InternalCommentForm("form", membership);
		add(form);
	}

}
