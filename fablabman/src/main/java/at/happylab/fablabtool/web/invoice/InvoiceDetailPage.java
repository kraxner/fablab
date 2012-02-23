package at.happylab.fablabtool.web.invoice;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.converter.CustomBigDecimalConverter;
import at.happylab.fablabtool.dataprovider.ConsumationEntryProvider;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.web.BasePage;

public class InvoiceDetailPage extends WebPage{
	
	@Inject
	private ConsumationEntryProvider consEntrOfInvoice;
	
	private Invoice inv;
	private CustomBigDecimalConverter cv;
	
	private String totalSum;
	private String contactPerson;
	private String payMethod;
	private String iban;
	private String bic;
	
	public InvoiceDetailPage(Invoice inv) {
		add(new StyleSheetReference("stylesheetInv", BasePage.class, "/css/invoice.css"));
		
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
		add(new Label("recipient", new PropertyModel<Invoice>(inv,"recipient")));
		if(inv.getRelatedTo().getMembershipType().equals(MembershipType.BUSINESS)){
			contactPerson = "z.Hd. " + inv.getRelatedTo().getContactPerson();
		}
		add(new Label("contact", new PropertyModel<String>(this,"contactPerson")));
		
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
			public void populateItem(Item<ICellPopulator<ConsumationEntry>> item, String componentId, IModel<ConsumationEntry> rowModel) {
				ConsumationEntry cons = rowModel.getObject();
				item.add(new Label(componentId, cv.convertToString(cons.getSum(), null)));
			}
		};

		add(new DefaultDataTable<ConsumationEntry>("consEntrTable", columns, consEntrOfInvoice, 5));

		BigDecimal sum = new BigDecimal(0);
		for(ConsumationEntry cons : inv.getIncludesConsumationEntries()){
			sum = sum.add(cons.getSum());
		}
		totalSum = cv.convertToString(sum, null);
		add(new Label("totalSum", new PropertyModel<String>(this,"totalSum")));
		
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
		add(new Label("paymeth", new PropertyModel<String>(this,"payMethod")));
		add(new Label("iban", new PropertyModel<String>(this,"iban")));
		add(new Label("bic", new PropertyModel<String>(this,"bic")));
		add(new Label("comment", new PropertyModel<Invoice>(inv,"comment")));
	}
}
