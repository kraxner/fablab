package at.happylab.fablabtool;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PrivateMembership;
import at.happylab.fablabtool.model.User;

public class MitgliederPage extends BasePage {
	

	@Inject private MembershipManagement membershipMgmt;
	
	public MitgliederPage() {
		add(new Label("mitgliederLabel","Mitglieder"));
		
		Form<String> form = new Form<String>("main");
		
		form.add(new BookmarkablePageLink("mitgliedDatenLink", MitgliedDatenPage.class));
		form.add(new BookmarkablePageLink("mitgliedPaketeLink", MitgliedPaketePage.class));
		form.add(new BookmarkablePageLink("mitgliedBuchungenLink", MitgliedBuchungenPage.class));
		form.add(new BookmarkablePageLink("mitgliedRechnungenLink", MitgliedRechnungenPage.class));
        //form.add(new BookmarkablePageLink("mitgliedAddLink", MitgliedAddPage.class));
        form.add(new Link("mitgliedAddLink") {
            public void onClick() {
                setResponsePage(new MitgliedDatenPage(new PrivateMembership(), membershipMgmt));
            }
        });
        
		/*final Component newMemberButton = new AjaxButton("mitgliedAddLink") {

			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form<?> arg1) {
				Membership member = new PrivateMembership();
				member.setComment("testcomment");
				//user.setUsername("max");
				//user.setPassword("geheim");
				membershipMgmt.addMembership(member);
				
			}
		};
		form.add(newMemberButton);*/
		add(form);
	}
	
}
