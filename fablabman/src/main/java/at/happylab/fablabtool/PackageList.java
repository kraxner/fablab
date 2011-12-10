package at.happylab.fablabtool;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import at.happylab.fablabtool.dataprovider.PackageProvider;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class PackageList extends BasePage {
	
	@Inject PackageProvider packageProvider;
	
	public PackageList() {
		navigation.selectStammdaten();
		
		IColumn[] columns = new IColumn[4];
		
		columns[0] = new TextFilteredPropertyColumn(new Model<String>("Name"), "name","name");
		columns[1] = new TextFilteredPropertyColumn(new Model<String>("Beschreibung"), "description","description");
		columns[2] = new TextFilteredPropertyColumn(new Model<String>("Preis"), "price","price");
		columns[3] = new LinkPropertyColumn(new Model<String>("Bearbeiten"), new Model("edit")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Package p = (Package) model.getObject();
				setResponsePage(new PackageAddPage(p));
				
			}
		};
		
		DefaultDataTable<Package> table = new DefaultDataTable("packageTable", columns, packageProvider, 5);
		add(table);
		
	}
	
}
