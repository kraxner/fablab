package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.PageMap;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.InvoiceManagement;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.dataprovider.InvoiceProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.CheckBoxColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.DropDownColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.EnumPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.TextFieldColumn;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipStatus;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.web.invoice.InvoiceDetailPage;

public class InvoicePanel extends Panel {
	private static final long serialVersionUID = -7129490579199414107L;
	
	@Inject
	private InvoiceProvider invoicesOfMember;
	
	@Inject
	private InvoiceManagement invoiceMgmt;
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	private InvForm invForm;
	private String numOfRows;
	private Label resultDiv;
	private String filter;

	public InvoicePanel(String id, final Membership member, final MembershipManagement membershipMgmt) {
		super(id);
		
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		
		invoicesOfMember.setMember(member);

		init();
		
		invForm = new InvForm("form");
		add(invForm);
		
		numOfRows = invoicesOfMember.size() + " Datensätze";
		resultDiv = new Label("invCount", new PropertyModel(this,"numOfRows") );
		add(resultDiv);
	}
	
	private void init(){
		Form<InvoiceProvider> form = new Form<InvoiceProvider>("filterForm", new CompoundPropertyModel<InvoiceProvider>(invoicesOfMember));
		//SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		//final TextField<Date> from = new TextField<Date>("fromFilter");
		//from.add(new SimpleAttributeModifier("value", sdf.format(invoicesOfMember.getFromFilter())));
		//form.add(from);
		//final TextField<Date> to = new TextField<Date>("toFilter");
		//to.add(new SimpleAttributeModifier("value", sdf.format(invoicesOfMember.getToFilter())));
		//form.add(to);
		//form.add(new Button("apply"));
		form.add(new TextField<Date>("fromFilter"));
		form.add(new TextField<Date>("toFilter"));
		form.add(new TextField<String>("filter"));
		Button apply = new Button("apply"){
		private static final long serialVersionUID = 1L;
			public void onSubmit() {
				//invoicesOfMember.setFilter(from.getValue(), to.getValue());
				//setResponsePage(new MembershipDetailPage(member, membershipMgmt, 3));
				invForm = new InvForm("form");
				numOfRows = invoicesOfMember.size() + " Datensätze";
			}
		};
		
/*
<<<<<<< HEAD
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
=======
*/
		form.add(apply);


		add(form);
	}
	
	class InvForm extends Form{

		private static final long serialVersionUID = 2780639970765950200L;
		
		public InvForm(String s) {
			super(s);
			
			IColumn[] columns = new IColumn[8];
			columns[0] = new PropertyColumn<String>(new Model<String>("Rechnungsnummer"), "invoiceNumber", "invoiceNumber");
			columns[1] = new PropertyColumn<String>(new Model<String>("Betrag"), "amount", ""){
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
			columns[2] = new DropDownColumn<PaymentMethod>(new Model<String>("Zahlungsart"), "paymentMethod", "paymentMethod", PaymentMethod.class);
			columns[3] = new TextFilteredPropertyColumn<Invoice, Date>(new Model<String>("Rechnungsdatum"), "date", "date");
			columns[4] = new TextFilteredPropertyColumn<Invoice, Date>(new Model<String>("Fälligkeitsdatum"), "dueDate", "dueDate");
			columns[5] = new TextFieldColumn<Invoice>(new Model<String>("Zahlungseingangsdatum"), "payedAt", "payedAt");
			columns[6] = new DropDownColumn<InvoiceState>(new Model<String>("Status"), "state", "state", InvoiceState.class);
			columns[7] = new LinkPropertyColumn<String>(new Model<String>("Aktion"), new Model("Detailansicht"), new PopupSettings(10).setHeight(1024).setWidth(680)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item item, String componentId, IModel model) {
					Invoice inv = (Invoice) model.getObject();
					setResponsePage(new InvoiceDetailPage(member, membershipMgmt, inv));
				}
				 
			};
			PopupSettings popupSettings = new PopupSettings().setHeight(500).setWidth(500);
			
			add(new DefaultDataTable("invTable", columns, invoicesOfMember, 5));
			
			add(new Button("submit"));
//			Button cancel = new Button("cancel"){
//				private static final long serialVersionUID = 7607265405984709816L;
//				public void onSubmit() {
//                	setResponsePage(new MembershipDetailPage(member, membershipMgmt, 3));
//                }
//            };
//            cancel.setDefaultFormProcessing(false);
//            add(cancel);
		}

		public void onSubmit() {
			Iterator<Invoice> invIter = invoicesOfMember.iterator(0, invoicesOfMember.size());
			while(invIter.hasNext()){
				invoiceMgmt.storeInvoice(invIter.next());
			}
		}
	}
}
