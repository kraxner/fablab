package at.happylab.fablabtool.web.membership;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.MembershipProvider;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class MembershipListPage extends AdminBasePage {

	@Inject
	private MembershipManagement membershipMgmt;
	
	@Inject MembershipProvider membershipProvider;

	public MembershipListPage() {
		
		add(new Label("mitgliederLabel", "Mitglieder"));

		Form<String> form = new Form<String>("main");

		IColumn[] columns = new IColumn[3];
		columns[0] = new LinkPropertyColumn(new Model<String>("Nr"), "id", "id") {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = (Membership) model.getObject();
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));
				
			}
		};
		columns[1] = new LinkPropertyColumn(new Model<String>("Name"),  "name","name") {

			
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = (Membership) model.getObject();
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));
				
			}
			 
		};
		columns[2] = new LinkPropertyColumn(new Model<String>("Aktion"), new Model("löschen")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = (Membership) model.getObject();
				membershipMgmt.removeMembership(m);
			}
			 
		};

		form.add(new DefaultDataTable("mitgliederTabelle", columns, membershipProvider, 5));
		
		form.add(new Label("mitgliederAnzahl", membershipProvider.size() + " Datensätze"));

		form.add(new Link("addPrivateMembershipLink") {
            public void onClick() {
                Membership m = new Membership();
                m.addUser(new User());
                m.setMembershipType(MembershipType.PRIVATE);
                setResponsePage(new MembershipDetailPage(m, membershipMgmt));
            }
        });
//		form.add(new Link("addBusinessMembershipLink") {
//            public void onClick() {
//               Membership m = new Membership();
//               m.setMembershipType(MembershipType.BUSINESS);
//               setResponsePage(new MembershipDetailPage(m, membershipMgmt));
//            }
//        });

		add(form);
	}

}


