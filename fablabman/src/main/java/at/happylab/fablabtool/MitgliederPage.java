package at.happylab.fablabtool;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class MitgliederPage extends BasePage {

	public MitgliederPage() {
		add(new Label("mitgliederLabel","Mitglieder"));
		add(new BookmarkablePageLink("mitgliedDatenLink", MitgliedDatenPage.class));
		add(new BookmarkablePageLink("mitgliedPaketeLink", MitgliedPaketePage.class));
        add(new BookmarkablePageLink("mitgliedBuchungenLink", MitgliedBuchungenPage.class));
        add(new BookmarkablePageLink("mitgliedRechnungenLink", MitgliedRechnungenPage.class));
        //add(new BookmarkablePageLink("mitgliedAddLink", MitgliedDatenPage.class));
	}
	
}
