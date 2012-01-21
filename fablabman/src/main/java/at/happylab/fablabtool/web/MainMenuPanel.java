package at.happylab.fablabtool.web;

import javax.inject.Inject;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import at.happylab.fablabtool.model.WebUser;
import at.happylab.fablabtool.session.FablabAuthenticatedWebSession;
import at.happylab.fablabtool.session.LoggedIn;
import at.happylab.fablabtool.session.SessionScopeProducer;
import at.happylab.fablabtool.web.invoice.InvoiceListPage;
import at.happylab.fablabtool.web.maintenance.MasterDataPage;
import at.happylab.fablabtool.web.maintenance.SubscriptionListPage;
import at.happylab.fablabtool.web.membership.MembershipListPage;

public class MainMenuPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private BookmarkablePageLink<String> mitglieder;
	private BookmarkablePageLink<String> subscriptions;
	private BookmarkablePageLink<String> rechnungen;
	private BookmarkablePageLink<String> masterdata;
	private BookmarkablePageLink<String> aufgaben;
	
	@SuppressWarnings("unused")
	@Inject @LoggedIn private WebUser user;
	
	public MainMenuPanel(String id) {
        super(id);
        
        mitglieder = new BookmarkablePageLink<String>("mitgliederLink", MembershipListPage.class); 
        add(mitglieder);
        
        subscriptions = new BookmarkablePageLink<String>("subscriptionsLink", SubscriptionListPage.class); 
        add(subscriptions);
        
        rechnungen = new BookmarkablePageLink<String>("rechnungenLink", InvoiceListPage.class); 
        add(rechnungen);
        
        masterdata = new BookmarkablePageLink<String>("masterdataLink", MasterDataPage.class);
        add(masterdata);
        
        aufgaben = new BookmarkablePageLink<String>("aufgabenLink", AccountingPage.class);
        add(aufgaben);
        
        String username = "";
        if (getSession() instanceof FablabAuthenticatedWebSession ) {
        	SessionScopeProducer sessionScopeProducer = ((FablabAuthenticatedWebSession)getSession()).getSessionScopeProducer();
        	if (sessionScopeProducer != null) {
        		username = sessionScopeProducer.getLoggedInUser().getFullname();
        	}
        }
        add(new Label("loggedInUser", username));        
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
	
	public void selectMasterData() {
		masterdata.add(new SimpleAttributeModifier("class", "selected"));
	}
	
	public void selectAufgaben() {
		aufgaben.add(new SimpleAttributeModifier("class", "selected"));
	}
}
