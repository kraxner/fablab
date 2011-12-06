package at.happylab.fablabtool.web.membership;

import org.apache.wicket.markup.html.panel.Panel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;

public class MitgliedPaketePage extends Panel {
	private static final long serialVersionUID = -9180787774643758400L;
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	

	public MitgliedPaketePage(String id, Membership member,  MembershipManagement membershipMgmt) {
		super(id);
		
		this.member = member;
		this.membershipMgmt = membershipMgmt;
	}

}
