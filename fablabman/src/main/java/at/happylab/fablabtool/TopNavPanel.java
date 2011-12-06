package at.happylab.fablabtool;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import at.happylab.fablabtool.web.membership.MitgliederPage;

public class TopNavPanel extends Panel {

	private static final long serialVersionUID = 1L;

	BookmarkablePageLink mitglieder;
	BookmarkablePageLink rechnungen;
	BookmarkablePageLink stammdaten;
	
	public TopNavPanel(String id) {
        super(id);
        
        mitglieder = new BookmarkablePageLink("mitgliederLink", MitgliederPage.class); 
        add(mitglieder);
        
        rechnungen = new BookmarkablePageLink("rechnungenLink", RechnungenPage.class); 
        add(rechnungen);
        
        stammdaten = new BookmarkablePageLink("stammdatenLink", StammdatenPage.class);
        add(stammdaten);
    }
	
	public void selectMitglieder() {
		mitglieder.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectRechnungen() {
		rechnungen.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectStammdaten() {
		stammdaten.add(new SimpleAttributeModifier("class", "selected"));
	}
	
}
