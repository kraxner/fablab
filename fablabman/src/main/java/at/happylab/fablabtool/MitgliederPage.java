package at.happylab.fablabtool;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import at.happylab.fablabtool.beans.MembershipManagement;
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
        //add(new BookmarkablePageLink("mitgliedAddLink", MitgliedDatenPage.class));
        
		final Component newMemberButton = new AjaxButton("newMember") {

			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form<?> arg1) {
				User user = new User();
				user.setUsername("max");
				user.setPassword("geheim");
				membershipMgmt.addMembership(user);
				
			}
		};
		form.add(newMemberButton);
		add(form);
	}
	
}
