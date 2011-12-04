package at.happylab.fablabtool;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import at.happylab.fablabtool.model.Package;

public class PackageList extends BasePage {

	private List packageList;
	
	public PackageList() {
		
		packageList = new ArrayList();
		packageList.add(new Package("Test"));
		packageList.add(new Package("Noch ein Test"));
		
		ListView listview = new ListView("packageList", packageList) {
			protected void populateItem(ListItem item) {
				final Package p = (Package) item.getModelObject();
				
				item.add(new Label("label", p.getName()));
				
				item.add(new Link("editPackage") {
                    public void onClick() {
                        setResponsePage(new PackageAddPage(p));
                    }
                });
				
			}
		};
		add(listview);
		
	}
	
}
