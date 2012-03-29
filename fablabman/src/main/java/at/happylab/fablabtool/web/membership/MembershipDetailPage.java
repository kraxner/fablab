package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.micalo.wicket.model.SmartModel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dao.MembershipDAO;
import at.happylab.fablabtool.markup.html.tabs.AjaxTabbedPanelWithContext;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class MembershipDetailPage extends AdminBasePage {
	
	@Inject	private MembershipDAO membershipDAO;
	private SmartModel<Membership> membershipModel;
	
	public MembershipDetailPage(PageParameters params) {
		if (params.containsKey("id")) {
		    long id = params.getLong("id");
	    	Membership membership = membershipDAO.load(id);
		    membershipModel = new SmartModel<Membership>(membershipDAO, membership);
		} else {
			Membership m = new Membership();
			m.addUser(new User());
			m.setMembershipType(MembershipType.PRIVATE);
			membershipModel = new SmartModel<Membership>(membershipDAO, m);
		}
	    int tab = 0;
	    if (params.containsKey("tab")) {
	    	tab = params.getInt("tab");
	    }
	    addTabs(tab);
	}
	
//	public MembershipDetailPage(Membership member, MembershipDAO membershipMgmt) {
//		this.member = member;
//		this.membershipDAO = membershipMgmt;
//
//		addTabs();
//	}
//	
//	public MembershipDetailPage(Membership member, MembershipDAO membershipMgmt, int tab) {
//		this.member = member;
//		this.membershipDAO = membershipMgmt;
//
//		addTabs(tab);
//	}
	
	private void addTabs(){
		addTabs(0);
	}
	
	private void addTabs(int tab) {
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Stammdaten")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new DataPanel(panelId, membershipModel);
			  }
		   });
		tabs.add(new AbstractTab(new Model<String>("Pakete")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new SubscriptionPanel(panelId, membershipModel);
			  }
		   });
		tabs.add(new AbstractTab(new Model<String>("Buchungen")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new EntryPanel(panelId, membershipModel);
			  }
		   });
		tabs.add(new AbstractTab(new Model<String>("Rechnungen")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new InvoicePanel(panelId, membershipModel.getObject());
			  }
		   });
		
		tabs.add(new AbstractTab(new Model<String>("Interne Kommentare")) {
			private static final long serialVersionUID = 1L;

			public Panel getPanel(String panelId) {
				return new InternalCommentsPanel(panelId, membershipModel.getObject());
			}
		});
		AjaxTabbedPanelWithContext panel = new AjaxTabbedPanelWithContext("tabs", tabs, new Model<String>(membershipModel.getObject().getName()));

		add(panel);
		panel.setSelectedTab(tab);
	}
	
	
	
}
