package at.happylab.fablabtool.web.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.BasePage;
import at.happylab.fablabtool.beans.InvoiceManagement;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.ConsumationEntryProvider;
import at.happylab.fablabtool.dataprovider.InvoiceProvider;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.membership.MembershipDetailPage;
import at.happylab.fablabtool.web.membership.MembershipListPage;
import at.happylab.fablabtool.converter.CustomBigDecimalConverter;

public class InvoiceDetailPage extends WebPage{
	
	@Inject
	private ConsumationEntryProvider consEntrOfInvoice;
	
	@Inject
	private InvoiceManagement invoiceMgmt;
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	private Invoice inv;
	private CustomBigDecimalConverter cv;
	
	private String totalSum;
	private String contactPerson;
	private String payMethod;
	private String iban;
	private String bic;
	
	public InvoiceDetailPage(Membership member, MembershipManagement membershipMgmt, Invoice inv) {
		add(new StyleSheetReference("stylesheetInv", BasePage.class, "/css/invoice.css"));
		
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		this.inv = inv;
		this.cv = new CustomBigDecimalConverter();
		
		contactPerson = "";
		totalSum = "";
		payMethod = "";
		iban = "";
		bic = "";
		
		consEntrOfInvoice.setInvoice(inv);
		init();
	}

	private void init(){
		//add(new Image("logo", new Model<String>("img/innoc2.JPG")));
		
		add(new Label("recipient", new PropertyModel<Invoice>(inv,"recipient")));
		if(inv.getRelatedTo().getMembershipType().equals(MembershipType.BUSINESS)){
			contactPerson = "z.Hd. " + inv.getRelatedTo().getContactPerson();
		}
		add(new Label("contact", new PropertyModel(this,"contactPerson")));
		
		add(new Label("street", new PropertyModel<Invoice>(inv,"Address.street")));
		add(new Label("zip", new PropertyModel<Invoice>(inv,"Address.zipCode")));
		add(new Label("city", new PropertyModel<Invoice>(inv,"Address.city")));
		
		add(new Label("date", new PropertyModel<Invoice>(inv,"date")));
		add(new Label("invoiceNumber", new PropertyModel<Invoice>(inv,"invoiceNumber")));
		add(new Label("dueDate", new PropertyModel<Invoice>(inv,"dueDate")));
		
		IColumn[] columns = new IColumn[4];
		columns[0] = new PropertyColumn<ConsumationEntry>(new Model<String>("Bezeichnung"), "text");
		columns[1] = new PropertyColumn<ConsumationEntry>(new Model<String>("Preis"), "price");
		columns[2] = new PropertyColumn<ConsumationEntry>(new Model<String>("Anzahl"), "quantity");
		columns[3] = new PropertyColumn<ConsumationEntry>(new Model<String>("Gesamt"), ""){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item item, String componentId, IModel rowModel) {
				ConsumationEntry cons = (ConsumationEntry) rowModel.getObject();
				item.add(new Label(componentId, cv.convertToString(cons.getSum(), null)));
			}
		};

		add(new DefaultDataTable("consEntrTable", columns, consEntrOfInvoice, 5));
		
		BigDecimal sum = new BigDecimal(0);
		for(ConsumationEntry cons : inv.getIncludesConsumationEntries()){
			sum = sum.add(cons.getSum());
		}
		totalSum = cv.convertToString(sum, null);
		add(new Label("totalSum", new PropertyModel(this,"totalSum")));
		
		switch(inv.getPaymentMethod()){
		case DEBIT:
			payMethod = "Der Rechnungsbetrag wird von folgendem Konto abgebucht:";
			iban = "Kontonummer: " + inv.getRelatedTo().getBankDetails().getIban();
			bic = "BLZ: " + inv.getRelatedTo().getBankDetails().getBic();
			break;
		case ON_ACCOUNT:
			payMethod = "Wir ersuchen um Überweisung des oben angeführten Rechnungsbetrags.";
			break;
		case CASH_IN_ADVANCE:
			payMethod = "Betrag bar bezahlt.";
			break;
		default:
			break;
		}
		add(new Label("paymeth", new PropertyModel(this,"payMethod")));
		add(new Label("iban", new PropertyModel(this,"iban")));
		add(new Label("bic", new PropertyModel(this,"bic")));
		add(new Label("comment", new PropertyModel(this,"comment")));
	}
	
//	private void init() {	
//		Form<String> form = new Form<String>("main");
//
//		IColumn[] columns = new IColumn[7];
//		columns[0] = new PropertyColumn(new Model<String>("Nr"), "id", "id");
//		columns[1] = new PropertyColumn(new Model<String>("Datum"), "date", "date");
//		columns[2] = new PropertyColumn(new Model<String>("Preis"), "price", "price");
//		columns[3] = new PropertyColumn(new Model<String>("Anzahl"), "quantity", "quantity");
//		columns[4] = new PropertyColumn(new Model<String>("Bezeichnung"), "text", "text");
//		columns[5] = new PropertyColumn(new Model<String>("consumedby_id"), "consumedBy", "consumedBy");
//		columns[6] = new PropertyColumn(new Model<String>("consumeditem:id"), "consumedItem", "consumedItem");
//
//		form.add(new DefaultDataTable("consEntrTable", columns, consEntrOfInvoice, 5));
//		
//		form.add(new Label("consEntrCount", consEntrOfInvoice.size() + " Datens�tze"));
//
//		add(form);
//		add(new InvForm("form", inv));
//	}
//	
//	class InvForm extends Form<Object>{
//
//		private static final long serialVersionUID = 2780639970765950200L;
//
//		public InvForm(String s, final Invoice inv) {
//			super(s, new CompoundPropertyModel<Object>(inv));
//			
//			add(new TextField<Object>("id").setEnabled(false));
//			add(new TextField<Object>("date").setEnabled(false));
//			add(new TextField<Object>("dueDate").setEnabled(false));
//			add(new TextField<Object>("recipient").setEnabled(false));
//			add(new TextField<Object>("Address.street").setEnabled(false));
//			add(new TextField<Object>("Address.city").setEnabled(false));
//			add(new TextField<Object>("Address.zipCode").setEnabled(false));
//			add(new TextField<Object>("generatedAt").setEnabled(false));
//			add(new TextField<Object>("settlementPeriodFrom").setEnabled(false));
//			add(new TextField<Object>("settlementPeriodTo").setEnabled(false));
//			add(new TextField<Object>("paymentMethod").setEnabled(false));
//			add(new TextField<Object>("payedAt").setEnabled(false));
//			add(new TextArea<Object>("comment").setEnabled(false));
//
//			DropDownChoice<InvoiceState> invState = new DropDownChoice<InvoiceState>("state");
//			invState.setChoices(new LoadableDetachableModel<List<InvoiceState>>() {
//
//				private static final long serialVersionUID = 4565611533591204089L;
//
//				public List<InvoiceState> load() {
//                    List<InvoiceState> list = new ArrayList<InvoiceState>(3);
//                    list.add(InvoiceState.OPEN);
//                    list.add(InvoiceState.PAID);
//                    list.add(InvoiceState.CANCELLED);
//                    return list;
//                }
//            });
//            add(invState);
//			
//			add(new Button("submit"));
//			Button cancel = new Button("cancel"){
//				private static final long serialVersionUID = 7607265405984709816L;
//				public void onSubmit() {
//                	setResponsePage(new MembershipDetailPage(member, membershipMgmt, 3));
//                }
//            };
//            cancel.setDefaultFormProcessing(false);
//            add(cancel);
//		}
//
//		public void onSubmit() {
//			invoiceMgmt.storeInvoice(inv);
//			setResponsePage(new MembershipDetailPage(member, membershipMgmt, 3));
//		}
//	}
}
