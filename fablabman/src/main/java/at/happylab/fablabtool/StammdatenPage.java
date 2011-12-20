package at.happylab.fablabtool;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.web.accessgrant.AccessGrantListPage;
import at.happylab.fablabtool.web.accessgrant.KeycardListPage;

public class StammdatenPage extends BasePage {

	public StammdatenPage() {
		navigation.selectStammdaten();
		add(new Label("stammdatenLabel","Stammdaten"));
		
		add(new Link("PackageLink") {
		    public void onClick() {
		        setResponsePage(new PackageList());
		    }
		});
		
		add(new Link("KeycardLink") {
		    public void onClick() {
		        setResponsePage(new KeycardListPage());
		    }
		});
		
		add(new Link("AccessGrantLink") {
		    public void onClick() {
		        setResponsePage(new AccessGrantListPage());
		    }
		});
	}

}
