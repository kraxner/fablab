package at.happylab.fablabtool;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class MitgliedDetailPage extends BasePage {

	public MitgliedDetailPage() {
		add(new Label("mitgliedDetailLabel","Name des Mitglieds"));
		add(new BookmarkablePageLink("mitgliedDetailDatenLink", MitgliedDatenPage.class));
		add(new BookmarkablePageLink("mitgliedDetailPaketeLink", MitgliedPaketePage.class));
        add(new BookmarkablePageLink("mitgliedDetailBuchungenLink", MitgliedBuchungenPage.class));
        add(new BookmarkablePageLink("mitgliedDetailRechnungenLink", MitgliedRechnungenPage.class));
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
