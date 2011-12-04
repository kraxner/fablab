package at.happylab.fablabtool;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.PrivateMembership;

public class MitgliederPage extends BasePage {

	@Inject
	private MembershipManagement membershipMgmt;

	public MitgliederPage() {
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
		
		final UserProviderX userProvider = new UserProviderX();

		IColumn[] columns = new IColumn[2];
		columns[0] = new PropertyColumn(new Model<String>("First Name"), "name.first","name.first");
		columns[1] = new PropertyColumn(new Model<String>("Last Name"), "name.last", "name.last");
		
		DefaultDataTable table = new DefaultDataTable("mitgliederTabelle", columns, userProvider, 5);
		
		form.add(new Label("mitgliederAnzahl", userProvider.getCount() + " Datensätze"));

		form.add(table);

		//form.add(new BookmarkablePageLink("mitgliedAddLink", MitgliedAddPage.class));
		form.add(new Link("mitgliedAddLink") {
            public void onClick() {
                setResponsePage(new MitgliedDatenPage(new PrivateMembership(), membershipMgmt));
            }
        });

		add(form);
	}

}


