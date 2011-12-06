package at.happylab.fablabtool.web.membership;

import org.apache.wicket.markup.html.panel.Panel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;

public class MitgliedRechnungenPage extends Panel {
	private static final long serialVersionUID = -7129490579199414107L;
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	

	public MitgliedRechnungenPage(String id, Membership member,  MembershipManagement membershipMgmt) {
		super(id);
		
		this.member = member;
		this.membershipMgmt = membershipMgmt;
	}

}
