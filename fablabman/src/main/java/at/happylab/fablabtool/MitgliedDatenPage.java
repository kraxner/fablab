package at.happylab.fablabtool;

import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.User;

public class MitgliedDatenPage extends MitgliedDetailPage {
	
	//@Inject private MembershipManagement membershipMgmt;
	private MembershipManagement membershipMgmt;
	private Membership member;

	public MitgliedDatenPage(Membership member, MembershipManagement membershipMgmt) {
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		add(new FeedbackPanel("feedback"));
		add(new MemberForm("form", member));
	}

	class MemberForm extends Form {
		public MemberForm(String s, final Membership member) {
			super(s, new CompoundPropertyModel(member));

			ListView listView = new ListView("list", member.getUsers()) {
				protected void populateItem(ListItem item)
				{
					User user = (User)item.getModelObject();
					user.setId(member.getId());
					item.add(new TextField("first", new PropertyModel(user, "firstname")));
					item.add(new TextField("last", new PropertyModel(user, "lastname")));
					item.add(new TextField("email", new PropertyModel(user, "email")));
					item.add(new TextField("mobile", new PropertyModel(user, "mobile")));
				}
			};
			listView.setReuseItems(true);
			add(listView);

			add(new TextField("bankDetails.iban"));
			add(new TextArea<String>("comment"));
			add(new Button("submit"));
		}

		public void onSubmit() {
			membershipMgmt.addMembership(member);
			//setResponsePage(new MitgliedDatenPage(member, membershipMgmt));
			//setResponsePage(new MitgliederPage());
			//setResponsePage(MitgliederPage.class);
		}
	}

}
