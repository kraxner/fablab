package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.markup.html.tabs.AjaxTabbedPanelWithContext;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class MembershipDetailPage extends AdminBasePage {
	@Inject
	private MembershipManagement membershipMgmt;
	private Membership member;
	
	public MembershipDetailPage(PageParameters params) {
	    int id = params.getInt("id");
    	member = membershipMgmt.loadMembership(id);
	    int tab = params.getInt("tab");
	    addTabs(tab);
	}
	
	public MembershipDetailPage(Membership member, MembershipManagement membershipMgmt) {
		this.member = member;
		this.membershipMgmt = membershipMgmt;

		addTabs();
	}
	
	public MembershipDetailPage(Membership member, MembershipManagement membershipMgmt, int tab) {
		this.member = member;
		this.membershipMgmt = membershipMgmt;

		addTabs(tab);
	}
	
	private void addTabs(){
		addTabs(0);
	}
	
	private void addTabs(int tab) {
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Stammdaten")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new DataPanel(panelId, member, membershipMgmt);
			  }
		   });
		tabs.add(new AbstractTab(new Model<String>("Pakete")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new SubscriptionPanel(panelId, member, membershipMgmt);
			  }
		   });
		tabs.add(new AbstractTab(new Model<String>("Buchungen")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new EntryPanel(panelId, member, membershipMgmt);
			  }
		   });
		tabs.add(new AbstractTab(new Model<String>("Rechnungen")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new InvoicePanel(panelId, member);
			  }
		   });
		
		tabs.add(new AbstractTab(new Model<String>("Interne Kommentare")) {
			private static final long serialVersionUID = 1L;

			public Panel getPanel(String panelId) {
				return new InternalCommentsPanel(panelId, member, membershipMgmt);
			}
		});
		AjaxTabbedPanelWithContext panel = new AjaxTabbedPanelWithContext("tabs", tabs, new Model<String>(member.getName()));

		add(panel);
		panel.setSelectedTab(tab);
	}
	
	
	
}
