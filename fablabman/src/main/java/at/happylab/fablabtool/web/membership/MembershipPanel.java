package at.happylab.fablabtool.web.membership;

import net.micalo.wicket.model.SmartModel;

import org.apache.wicket.markup.html.panel.Panel;

import at.happylab.fablabtool.model.Membership;

public class MembershipPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	protected SmartModel<Membership> membershipModel;

	public MembershipPanel(String id, SmartModel<Membership> model) {
		super(id, model);
		membershipModel = model;
	}
	


}
