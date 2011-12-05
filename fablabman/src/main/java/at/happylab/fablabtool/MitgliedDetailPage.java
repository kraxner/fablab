package at.happylab.fablabtool;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import at.happylab.fablabtool.MitgliedDatenPage.MemberForm;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;

public class MitgliedDetailPage extends BasePage {

	public MitgliedDetailPage(PageParameters params) {
		add(new BookmarkablePageLink("mitgliedDetailDatenLink", MitgliedDatenPage.class, params));
		add(new BookmarkablePageLink("mitgliedDetailPaketeLink", MitgliedPaketePage.class));
        add(new BookmarkablePageLink("mitgliedDetailBuchungenLink", MitgliedBuchungenPage.class));
        add(new BookmarkablePageLink("mitgliedDetailRechnungenLink", MitgliedRechnungenPage.class));
	}
	public MitgliedDetailPage(Membership member, MembershipManagement membershipMgmt) {
		PageParameters params = new PageParameters();
		params.put("id", member.getId());
		
		add(new BookmarkablePageLink("mitgliedDetailDatenLink", MitgliedDatenPage.class, params));
		add(new BookmarkablePageLink("mitgliedDetailPaketeLink", MitgliedPaketePage.class));
        add(new BookmarkablePageLink("mitgliedDetailBuchungenLink", MitgliedBuchungenPage.class));
        add(new BookmarkablePageLink("mitgliedDetailRechnungenLink", MitgliedRechnungenPage.class));
	}
	
	public MitgliedDetailPage() {
        /*add(new Link("mitgliedDetailDatenLink") {
		    @Override
		    public void onClick() {
		        setResponsePage(MitgliedDatenPage.class);
		    }
		});
		add(new Link("mitgliedDetailPaketeLink") {
		    @Override
		    public void onClick() {
		        setResponsePage(MitgliedPaketePage.class);
		    }
		});
		add(new Link("mitgliedDetailBuchungenLink") {
		    @Override
		    public void onClick() {
		        setResponsePage(MitgliedBuchungenPage.class);
		    }
		});
		add(new Link("mitgliedDetailRechnungenLink") {
		    @Override
		    public void onClick() {
		        setResponsePage(MitgliedRechnungenPage.class);
		    }
		});*/
	}

}
