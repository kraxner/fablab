package at.happylab.fablabtool;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

public class TopNavPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public TopNavPanel(String id) {
        super(id);
        add(new BookmarkablePageLink("mitglieder", MitgliederPage.class));
        add(new BookmarkablePageLink("rechnungen", RechnungenPage.class));
        add(new BookmarkablePageLink("stammdaten", StammdatenPage.class));
    }
}
