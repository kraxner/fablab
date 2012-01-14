package at.happylab.fablabtool;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dataprovider.PackageProvider;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PackageType;
import at.happylab.fablabtool.model.TimePeriod;
import at.happylab.fablabtool.panels.EnumPropertyColumn;
import at.happylab.fablabtool.panels.LinkPropertyColumn;

public class PackageList extends BasePage {

	@Inject
	PackageProvider packageProvider;

	public PackageList() {
		navigation.selectStammdaten();

		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new PropertyColumn<Package>(new Model<String>("ID"), "id", "id"));
		columns.add(new PropertyColumn<Package>(new Model<String>("Name"), "name", "name"));
		columns.add(new PropertyColumn<Package>(new Model<String>("Preis"), "price", "price"));
		columns.add(new EnumPropertyColumn<PackageType>(new Model<String>("Type"), "type", "type", PackageType.class, this));
		columns.add(new EnumPropertyColumn<TimePeriod>(new Model<String>("Abrechnungsperiode"), "billingCycle", "billingCycle", TimePeriod.class, this));
		columns.add(new EnumPropertyColumn<TimePeriod>(new Model<String>("Kündigung möglich"), "cancelationPeriodAdvance", "cancelationPeriodAdvance", TimePeriod.class, this));
		columns.add(new PropertyColumn<Package>(new Model<String>("Kündigungsfrist (Monate)"), "cancelationPeriod", "cancelationPeriod"));
		columns.add(new LinkPropertyColumn<Package>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = -302452659162757001L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Package p = (Package) model.getObject();
				setResponsePage(new PackageAddPage(p));

			}
		});

		DefaultDataTable<Package> table = new DefaultDataTable("packageTable", columns, packageProvider, 5);
		add(table);

		add(new Label("packageCount", packageProvider.size() + " Datensätze"));

		add(new Link<String>("addPackage") {
			private static final long serialVersionUID = -8867862121072697306L;

			public void onClick() {
				setResponsePage(new PackageAddPage(new Package()));
			}
		});

	}

}
