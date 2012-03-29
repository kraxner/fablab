package at.happylab.fablabtool.web.membership;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.dao.InvoiceDAO;
import at.happylab.fablabtool.dataprovider.InvoiceProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.DropDownColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.TextFieldColumn;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.web.invoice.InvoiceDetailPage;

public class InvoicePanel extends Panel {
	private static final long serialVersionUID = -7129490579199414107L;
	
	@Inject private InvoiceProvider invoicesOfMember;
	
	@Inject	private InvoiceDAO invoiceDAO;
	
	private InvForm invForm;
	/**
	 * is used for display of the row count (by wicket)
	 */
	private String numOfRows;
	private Label resultDiv;

	public InvoicePanel(String id, Membership member) {
		super(id);
		
		invoicesOfMember.setMember(member);

		addFilterForm();
		
		invForm = new InvForm("form");
		add(invForm);
		
		numOfRows = invoicesOfMember.size() + " Datensätze";
		resultDiv = new Label("invCount", new PropertyModel<String>(this,"numOfRows") );
		add(resultDiv);
	}
	
	private void addFilterForm(){
		Form<InvoiceProvider> form = new Form<InvoiceProvider>("filterForm", new CompoundPropertyModel<InvoiceProvider>(invoicesOfMember));
		form.add(new TextField<Date>("fromFilter"));
		form.add(new TextField<Date>("toFilter"));
		form.add(new TextField<String>("filter"));
		Button apply = new Button("apply"){
		private static final long serialVersionUID = 1L;
			public void onSubmit() {
				invForm = new InvForm("form");
				numOfRows = invoicesOfMember.size() + " Datensätze";
			}
		};
		form.add(apply);


		add(form);
	}
	
	class InvForm extends Form<Invoice>{

		private static final long serialVersionUID = 2780639970765950200L;
		
		public InvForm(String s) {
			super(s);
			
			IColumn[] columns = new IColumn[8];
			columns[0] = new PropertyColumn<Invoice>(new Model<String>("Rechnungsnummer"), "invoiceNumber", "invoiceNumber");
			columns[1] = new PropertyColumn<Invoice>(new Model<String>("Betrag"), "amount", ""){
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
					BigDecimal sum = new BigDecimal(0);
					CustomBigDecimalConverter bigDecConv = new CustomBigDecimalConverter();
					Invoice inv = rowModel.getObject();
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
			columns[7] = new LinkPropertyColumn<String>(new Model<String>("Aktion"), new Model<String>("Detailansicht"), new PopupSettings(10).setHeight(1024).setWidth(680)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(Item item, String componentId, IModel model) {
					Invoice inv = (Invoice) model.getObject();
					setResponsePage(new InvoiceDetailPage(inv));
				}
				 
			};
			
			add(new DefaultDataTable<Invoice>("invTable", columns, invoicesOfMember, 5));
			
			add(new Button("submit"));
		}

		public void onSubmit() {
			Iterator<Invoice> invIter = invoicesOfMember.iterator(0, invoicesOfMember.size());
			while(invIter.hasNext()){
				invoiceDAO.store(invIter.next());
			}
			invoiceDAO.commit();			
		}
	}
}
