package at.happylab.fablabtool;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import at.happylab.fablabtool.model.Package;

public class StammdatenPage extends BasePage {

	public StammdatenPage() {
		navigation.selectStammdaten();
		add(new Label("stammdatenLabel","Stammdaten"));
		
		
		add(new Link("newPackage") {
		    public void onClick() {
		        setResponsePage(new PackageAddPage(new Package()));
		    }
		});
		
		add(new Link("packageList") {
		    public void onClick() {
		        setResponsePage(new PackageList());
		    }
		});
	}

}
