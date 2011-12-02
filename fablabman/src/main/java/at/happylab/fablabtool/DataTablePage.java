package at.happylab.fablabtool;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public class DataTablePage extends WebPage {
    
    public DataTablePage() {
        final UserProviderX userProvider = new UserProviderX();
        
        IColumn[] columns = new IColumn[1];
        columns[0] = new PropertyColumn(new StringResourceModel("firstNameTableHeaderLabel", this, null), "name.first", "name.first");
        columns[0] = new PropertyColumn(new Model("Last Name"), "name.last", "name.last");
        
        DefaultDataTable table = new DefaultDataTable("datatable", columns, userProvider, 10);
        
        add(table);
    }

}


