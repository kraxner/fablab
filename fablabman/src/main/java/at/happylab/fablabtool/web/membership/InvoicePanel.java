package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.InvoiceProvider;
import at.happylab.fablabtool.model.BusinessMembership;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PrivateMembership;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.invoice.InvoiceDetailPage;

public class InvoicePanel extends Panel {
	private static final long serialVersionUID = -7129490579199414107L;
	
	@Inject
	private InvoiceProvider invoicesOfMember;
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	

	public InvoicePanel(String id, Membership member, MembershipManagement membershipMgmt) {
		super(id);
		
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		
		invoicesOfMember.setMember(member);
		init();
	}
	
	private void init() {	
		Form<String> form = new Form<String>("main");

		IColumn[] columns = new IColumn[4];
		columns[0] = new LinkPropertyColumn(new Model<String>("Nr"), "id", "id") {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Invoice i = (Invoice) model.getObject();
				setResponsePage(new InvoiceDetailPage(member, membershipMgmt, i));
				
			}
		};
		columns[1] = new PropertyColumn(new Model<String>("Datum"), "dueDate", "dueDate");
		columns[2] = new PropertyColumn(new Model<String>("Status"), "state", "state");
		columns[3] = new LinkPropertyColumn(new Model<String>("Aktion"), new Model("status ändern")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Invoice i = (Invoice) model.getObject();
				setResponsePage(new InvoiceDetailPage(member, membershipMgmt, i));
			}
			 
		};

		form.add(new DefaultDataTable("invTable", columns, invoicesOfMember, 5));
		
		form.add(new Label("invCount", invoicesOfMember.size() + " Datensätze"));

		add(form);
	}

}
