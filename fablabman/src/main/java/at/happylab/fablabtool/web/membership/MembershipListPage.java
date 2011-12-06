package at.happylab.fablabtool.web.membership;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.MembershipProvider;
import at.happylab.fablabtool.model.BusinessMembership;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PrivateMembership;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class MembershipListPage extends BasePage {

	@Inject
	private MembershipManagement membershipMgmt;
	
	@Inject MembershipProvider membershipProvider;

	public MembershipListPage() {
		
		add(new Label("mitgliederLabel", "Mitglieder"));

		Form<String> form = new Form<String>("main");

		// form.add(new BookmarkablePageLink("mitgliedDatenLink",
		// MitgliedDatenPage.class));
		// form.add(new BookmarkablePageLink("mitgliedPaketeLink",
		// MitgliedPaketePage.class));
		// form.add(new BookmarkablePageLink("mitgliedBuchungenLink",
		// MitgliedBuchungenPage.class));
		// form.add(new BookmarkablePageLink("mitgliedRechnungenLink",
		// MitgliedRechnungenPage.class));
		
		

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
				//new TextFilteredPropertyColumn(new Model<String>("Name"),);
		
		columns[2] = new LinkPropertyColumn(new Model<String>("Aktion"), new Model("löschen")) {

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = (Membership) model.getObject();
				membershipMgmt.removeMembership(m);
			}
			 
		};
		 
		
		DefaultDataTable table = new DefaultDataTable("mitgliederTabelle", columns, membershipProvider, 5);
		
		form.add(new Label("mitgliederAnzahl", membershipProvider.size() + " DatensÃ¤tze"));

		form.add(table);

		//form.add(new BookmarkablePageLink("mitgliedAddLink", MitgliedAddPage.class));
		form.add(new Link("addPrivateMembershipLink") {
            public void onClick() {
                setResponsePage(new MembershipDetailPage(new PrivateMembership(), membershipMgmt));
            }
        });

		form.add(new Link("addBusinessMembershipLink") {
            public void onClick() {
                setResponsePage(new MembershipDetailPage(new BusinessMembership(), membershipMgmt));
            }
        });

		add(form);
	}

}


