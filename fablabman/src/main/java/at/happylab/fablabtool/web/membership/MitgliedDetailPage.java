package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.web.membership.MitgliedDatenPage.MemberForm;

public class MitgliedDetailPage extends BasePage {
	@Inject
	private MembershipManagement membershipMgmt;
	private Membership member;
	

	public MitgliedDetailPage(PageParameters params) {
	    int id = params.getInt("id");
	    this.member = membershipMgmt.loadMembership(id);
		
	    addTabs();
	}
	public MitgliedDetailPage(Membership member, MembershipManagement membershipMgmt) {
		this.member = member;
		this.membershipMgmt = membershipMgmt;

		addTabs();
	}
	
	private void addTabs() {
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("daten")) {
			private static final long serialVersionUID = 7504247263312822569L;

			public Panel getPanel(String panelId) {
				  return new MitgliedDatenPage(panelId, member, membershipMgmt);
			  }
		   });
				
		
		add(new AjaxTabbedPanel("tabs", tabs));
	}
	
	
	
}
