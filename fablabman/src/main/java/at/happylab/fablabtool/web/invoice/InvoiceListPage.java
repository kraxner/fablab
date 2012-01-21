package at.happylab.fablabtool.web.invoice;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;

import at.happylab.fablabtool.beans.InvoiceManagement;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.dataprovider.InvoiceProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.DropDownColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.TextFieldColumn;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class InvoiceListPage extends AdminBasePage{

	@Inject
	private InvoiceProvider invoices;
	
	@Inject
	private InvoiceManagement invoiceMgmt;
	
	@Inject
	private MembershipManagement membershipMgmt;
	
	private InvListForm invForm;
	private String numOfRows;
	private Label resultDiv;

	public InvoiceListPage() {
		navigation.selectRechnungen();
		
		invoices.setFilter();
		add(new Label("invoiceLabel", "Rechnungen"));
		
		init();
		
		invForm = new InvListForm("form");
		add(invForm);
		
		numOfRows = invoices.size() + " Datensätze";
		resultDiv = new Label("invCount", new PropertyModel(this,"numOfRows") );
		add(resultDiv);
	}
	
	private void init(){
		Form<InvoiceProvider> form = new Form<InvoiceProvider>("filterForm", new CompoundPropertyModel<InvoiceProvider>(invoices));
		form.add(new TextField<Date>("fromFilter"));
		form.add(new TextField<Date>("toFilter"));
		form.add(new TextField<String>("filter"));
		Button apply = new Button("apply"){
			private static final long serialVersionUID = 1L;
			public void onSubmit() {
				invForm = new InvListForm("form");
				numOfRows = invoices.size() + " Datensätze";
			}
		};
		form.add(apply);

		add(form);
	}
	
	class InvListForm extends Form{

		private static final long serialVersionUID = 2780639970765950200L;
		
		public InvListForm(String s) {
			super(s);
			
			IColumn[] columns = new IColumn[12];
			
			columns[0] = new PropertyColumn<Invoice>(new Model<String>("Mitgliedsnummer"), "relatedTo.memberId", "relatedTo.memberId");
			columns[1] = new PropertyColumn<String>(new Model<String>("Vorname"), "first", ""){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item item, String componentId, IModel rowModel) {
					Invoice inv = (Invoice) rowModel.getObject();
					MembershipType type = inv.getRelatedTo().getMembershipType();
					String name;
					if(type.equals(MembershipType.PRIVATE)){
						name = inv.getRelatedTo().getUsers().get(0).getFirstname();
					} else {
						name = "";
					}
					item.add(new Label(componentId, name));
				}
			};
			columns[2] = new PropertyColumn<String>(new Model<String>("Nachname"), "last", ""){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item item, String componentId, IModel rowModel) {
					Invoice inv = (Invoice) rowModel.getObject();
					MembershipType type = inv.getRelatedTo().getMembershipType();
					String name;
					if(type.equals(MembershipType.PRIVATE)){
						name = inv.getRelatedTo().getUsers().get(0).getLastname();
					} else {
						name = inv.getRelatedTo().getContactPerson();
					}
					item.add(new Label(componentId, name));
				}
			};
			columns[3] = new PropertyColumn<String>(new Model<String>("Firma"), "company", ""){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item item, String componentId, IModel rowModel) {
					Invoice inv = (Invoice) rowModel.getObject();
					MembershipType type = inv.getRelatedTo().getMembershipType();
					String name;
					if(type.equals(MembershipType.PRIVATE)){
						name = "";
					} else {
						name = inv.getRelatedTo().getCompanyName();
					}
					item.add(new Label(componentId, name));
				}
			};
			columns[4] = new PropertyColumn<String>(new Model<String>("Rechnungsnummer"), "invoiceNumber", "invoiceNumber");
			columns[5] = new PropertyColumn<String>(new Model<String>("Betrag"), "amount", ""){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item item, String componentId, IModel rowModel) {
					BigDecimal sum = new BigDecimal(0);
					CustomBigDecimalConverter bigDecConv = new CustomBigDecimalConverter();
					Invoice inv = (Invoice) rowModel.getObject();
					for(ConsumationEntry ce : inv.getIncludesConsumationEntries()){
						sum = sum.add(ce.getSum());
					}
					item.add(new Label(componentId, bigDecConv.convertToString(sum, null)));
				}
			};
			columns[6] = new DropDownColumn<PaymentMethod>(new Model<String>("Zahlungsart"), "paymentMethod", "paymentMethod", PaymentMethod.class);
			columns[7] = new TextFilteredPropertyColumn<Invoice, Date>(new Model<String>("Rechnungsdatum"), "date", "date");
			columns[8] = new TextFilteredPropertyColumn<Invoice, Date>(new Model<String>("Fälligkeitsdatum"), "dueDate", "dueDate");
			columns[9] = new TextFieldColumn<Invoice>(new Model<String>("Zahlungseingangsdatum"), "payedAt", "payedAt");
			columns[10] = new DropDownColumn<InvoiceState>(new Model<String>("Status"), "state", "state", InvoiceState.class);
			columns[11] = new LinkPropertyColumn<String>(new Model<String>("Aktion"), new Model("Detailansicht"), new PopupSettings(10).setHeight(1024).setWidth(680)) {
				@Override
				public void onClick(Item item, String componentId, IModel model) {
					Invoice inv = (Invoice) model.getObject();
					Membership member = inv.getRelatedTo();
					setResponsePage(new InvoiceDetailPage(member, membershipMgmt, inv));
				}
				 
			};
			
			add(new DefaultDataTable("invTable", columns, invoices, 50));
			
			add(new Button("submit"));
			Button bankExport = new Button("bankExport"){
				private static final long serialVersionUID = 7607265405984709816L;
				public void onSubmit() {
					WebResponse response = (WebResponse) getResponse();
					response.setAttachmentHeader("bankExport.csv"); //TODO: better name
					response.setContentType("text/csv");
					OutputStream out = getResponse().getOutputStream();
					invoices.export(new PrintWriter(out));
                }
            };
            bankExport.setDefaultFormProcessing(false); //TODO: should exporting save changes before exporting?
            add(bankExport);
            Button csvExport = new Button("csvExport"){
				private static final long serialVersionUID = 7607265405984709816L;
				public void onSubmit() {
//					WebResponse response = (WebResponse) getResponse();
//					response.setAttachmentHeader("csvExport.csv"); //TODO: better name
//					response.setContentType("text/csv");
//					OutputStream out = getResponse().getOutputStream();
//					invoices.export(new PrintWriter(out));
                }
            };
            csvExport.setDefaultFormProcessing(false); //TODO: should exporting save changes before exporting?
            add(csvExport);
            
		}

		public void onSubmit() {
			Iterator<Invoice> invIter = invoices.iterator(0, invoices.size());
			while(invIter.hasNext()){
				invoiceMgmt.storeInvoice(invIter.next());
			}
		}
	}
	
}
