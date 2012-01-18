package at.happylab.fablabtool;

import javax.inject.Inject;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import at.happylab.fablabtool.model.WebUser;
import at.happylab.fablabtool.session.FablabAuthenticatedWebSession;
import at.happylab.fablabtool.session.LoggedIn;
import at.happylab.fablabtool.web.invoice.InvoiceListPage;
import at.happylab.fablabtool.web.membership.MembershipListPage;

public class TopNavPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private BookmarkablePageLink<String> mitglieder;
	private BookmarkablePageLink<String> subscriptions;
	private BookmarkablePageLink<String> rechnungen;
	private BookmarkablePageLink<String> stammdaten;
	private BookmarkablePageLink<String> aufgaben;
	
	@SuppressWarnings("unused")
	@Inject @LoggedIn private WebUser user;
	
	public TopNavPanel(String id) {
        super(id);
        
        mitglieder = new BookmarkablePageLink<String>("mitgliederLink", MembershipListPage.class); 
        add(mitglieder);
        
        subscriptions = new BookmarkablePageLink<String>("subscriptionsLink", SubscriptionListPage.class); 
        add(subscriptions);
        
        rechnungen = new BookmarkablePageLink<String>("rechnungenLink", InvoiceListPage.class); 
        add(rechnungen);
        
        stammdaten = new BookmarkablePageLink<String>("stammdatenLink", StammdatenPage.class);
        add(stammdaten);
        
        aufgaben = new BookmarkablePageLink<String>("aufgabenLink", AufgabenPage.class);
        add(aufgaben);
        
        if (getSession() instanceof FablabAuthenticatedWebSession) {
        	FablabAuthenticatedWebSession fablabSession = (FablabAuthenticatedWebSession)getSession(); 
        	add(new Label("loggedInUser", fablabSession.getSessionScopeProducer().getLoggedInUser().getFullname()));
        } else {
        	throw new IllegalStateException("This class requires a " + FablabAuthenticatedWebSession.class.getSimpleName());
        }
    }
	
	public void selectMitglieder() {
		mitglieder.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectRechnungen() {
		rechnungen.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectSubscriptions() {
		subscriptions.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectStammdaten() {
		stammdaten.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectAufgaben() {
		aufgaben.add(new SimpleAttributeModifier("class", "selected"));
	}
}
