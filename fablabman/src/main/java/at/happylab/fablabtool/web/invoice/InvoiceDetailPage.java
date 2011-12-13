package at.happylab.fablabtool.web.invoice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.InvoiceManagement;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.ConsumationEntryProvider;
import at.happylab.fablabtool.dataprovider.InvoiceProvider;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.membership.MembershipDetailPage;
import at.happylab.fablabtool.web.membership.MembershipListPage;

public class InvoiceDetailPage extends BasePage{
	
	@Inject
	private ConsumationEntryProvider consEntrOfInvoice;
	
	@Inject
	private InvoiceManagement invoiceMgmt;
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	private Invoice inv;
	
	public InvoiceDetailPage(Membership member, MembershipManagement membershipMgmt, Invoice inv) {
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		this.inv = inv;
		
		consEntrOfInvoice.setInvoice(inv);
		init();
	}

	private void init() {	
		Form<String> form = new Form<String>("main");

		IColumn[] columns = new IColumn[7];
		columns[0] = new PropertyColumn(new Model<String>("Nr"), "id", "id");
		columns[1] = new PropertyColumn(new Model<String>("Datum"), "date", "date");
		columns[2] = new PropertyColumn(new Model<String>("Preis"), "price", "price");
		columns[3] = new PropertyColumn(new Model<String>("Anzahl"), "quantity", "quantity");
		columns[4] = new PropertyColumn(new Model<String>("Bezeichnung"), "text", "text");
		columns[5] = new PropertyColumn(new Model<String>("consumedby_id"), "consumedBy", "consumedBy");
		columns[6] = new PropertyColumn(new Model<String>("consumeditem:id"), "consumedItem", "consumedItem");

		form.add(new DefaultDataTable("consEntrTable", columns, consEntrOfInvoice, 5));
		
		form.add(new Label("consEntrCount", consEntrOfInvoice.size() + " Datensätze"));

		add(form);
		add(new InvForm("form", inv));
	}
	
	class InvForm extends Form<Object>{

		private static final long serialVersionUID = 2780639970765950200L;

		public InvForm(String s, final Invoice inv) {
			super(s, new CompoundPropertyModel<Object>(inv));
			
			add(new TextField<Object>("id").setEnabled(false));
			add(new TextField<Object>("date").setEnabled(false));
			add(new TextField<Object>("dueDate").setEnabled(false));
			add(new TextField<Object>("recipient").setEnabled(false));
			add(new TextField<Object>("Address.street").setEnabled(false));
			add(new TextField<Object>("Address.city").setEnabled(false));
			add(new TextField<Object>("Address.zipCode").setEnabled(false));
			add(new TextField<Object>("generatedAt").setEnabled(false));
			add(new TextField<Object>("settlementPeriodFrom").setEnabled(false));
			add(new TextField<Object>("settlementPeriodTo").setEnabled(false));
			add(new TextField<Object>("paymentMethod").setEnabled(false));
			add(new TextField<Object>("payedAt").setEnabled(false));
			add(new TextArea<Object>("comment").setEnabled(false));

			DropDownChoice<InvoiceState> invState = new DropDownChoice<InvoiceState>("state");
			invState.setChoices(new LoadableDetachableModel<List<InvoiceState>>() {

				private static final long serialVersionUID = 4565611533591204089L;

				public List<InvoiceState> load() {
                    List<InvoiceState> list = new ArrayList<InvoiceState>(3);
                    list.add(InvoiceState.OPEN);
                    list.add(InvoiceState.PAID);
                    list.add(InvoiceState.CANCELLED);
                    return list;
                }
            });
            add(invState);
			
			add(new Button("submit"));
			Button cancel = new Button("cancel"){
				private static final long serialVersionUID = 7607265405984709816L;
				public void onSubmit() {
                	setResponsePage(new MembershipDetailPage(member, membershipMgmt, 3));
                }
            };
            cancel.setDefaultFormProcessing(false);
            add(cancel);
		}

		public void onSubmit() {
			invoiceMgmt.storeInvoice(inv);
			setResponsePage(new MembershipDetailPage(member, membershipMgmt, 3));
		}
	}
}
