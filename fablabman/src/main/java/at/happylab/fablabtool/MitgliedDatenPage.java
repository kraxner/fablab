package at.happylab.fablabtool;

import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;

public class MitgliedDatenPage extends MitgliedDetailPage {
	
	//@Inject private MembershipManagement membershipMgmt;
	private MembershipManagement membershipMgmt;
	private Membership member;

	public MitgliedDatenPage(Membership member, MembershipManagement membershipMgmt) {
		this.member =  member;
		this.membershipMgmt = membershipMgmt;
        add(new FeedbackPanel("feedback"));
        add(new MemberForm("form", member));
    }

    class MemberForm extends Form {
        public MemberForm(String s, Membership member) {
            super(s, new CompoundPropertyModel(member));
            //add(new TextField("users.firstname"));
            add(new TextField("bankDetails.iban"));
            add(new TextArea<String>("comment"));
            add(new Button("submit"));
        }

        public void onSubmit() {
        	membershipMgmt.addMembership(member);
            //setResponsePage(new MitgliedDatenPage(member, membershipMgmt));
        	//setResponsePage(new MitgliederPage());
        	setResponsePage(MitgliederPage.class);
        }
    }

}
