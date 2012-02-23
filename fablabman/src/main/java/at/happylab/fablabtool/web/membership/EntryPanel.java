package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dao.InvoiceDAO;
import at.happylab.fablabtool.dataprovider.ConsumationEntryProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.CheckBoxColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Invoice;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PaymentMethod;

public class EntryPanel extends Panel {
	private static final long serialVersionUID = -9180787774643758400L;
	
	private Invoice invoice;
	private Membership member;
	
	@Inject
	ConsumationEntryProvider entryFromMembershipProvider;
	
	@Inject private InvoiceDAO invoiceDAO;
	
	private Set<ConsumationEntry> selected = new HashSet<ConsumationEntry>();

	public EntryPanel(String id, final Membership member) {
		super(id);

		entryFromMembershipProvider.setMember(member);

		this.invoice = new Invoice(member);
		this.member = member;
		
		Form<Invoice> form = new InvoiceForm("form", invoice);
		add(form);

		List<IColumn<ConsumationEntry>> columns = new ArrayList<IColumn<ConsumationEntry>>();
		
		columns.add(new CheckBoxColumn<ConsumationEntry>(Model.of("")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Boolean> newCheckBoxModel(
					final IModel<ConsumationEntry> rowModel) {
				return new AbstractCheckBoxModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public void unselect() {
						selected.remove(rowModel.getObject());
					}

					@Override
					public void select() {
						selected.add(rowModel.getObject());
					}

					@Override
					public boolean isSelected() {
						return selected.contains(rowModel.getObject());
					}

					@Override
					public void detach() {
						rowModel.detach();
					}
				};
			}
		});

		
		columns.add(new PropertyColumn<ConsumationEntry>(new Model<String>("Datum"), "date", "date"));
		columns.add(new PropertyColumn<ConsumationEntry>(new Model<String>("Beschreibung"), "text", "text"));
		columns.add(new PropertyColumn<ConsumationEntry>(new Model<String>("Einzelpreis"), "price", "price"));
		columns.add(new PropertyColumn<ConsumationEntry>(new Model<String>("Anzahl"), "quantity", "quantity"));
		columns.add(new PropertyColumn<ConsumationEntry>(new Model<String>("Einheit"), "unit", "unit"));
		columns.add(new PropertyColumn<ConsumationEntry>(new Model<String>("Gesamt"), "sum", "sum"));
		columns.add(new LinkPropertyColumn<ConsumationEntry>(new Model<String>("Aktionen"), new Model<String>("edit")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<ConsumationEntry> item, String componentId, IModel<ConsumationEntry> model) {
				ConsumationEntry e = (ConsumationEntry) model.getObject();
				setResponsePage(new ConsumationEntryDetailPage(member, e));
			}
			 
		});
		
		form.add(new DefaultDataTable<ConsumationEntry>("entryTable", columns, entryFromMembershipProvider, 5));
		
		form.add(new Link<String>("addEntry") {
			private static final long serialVersionUID = 9170539765267094210L;

			public void onClick() {
				ConsumationEntry entry = new ConsumationEntry();
                setResponsePage(new ConsumationEntryDetailPage(member, entry));
            }
        });
	}
	
	class InvoiceForm extends Form<Invoice> {
		
		private static final long serialVersionUID = -8312758114950893044L;

		public InvoiceForm(String s, final Invoice invoice) {
			super(s, new CompoundPropertyModel<Invoice>(invoice));
			
			add(new RequiredTextField<String>("recipient"));
			add(new RequiredTextField<String>("address.street"));
			add(new RequiredTextField<String>("address.city"));
			add(new RequiredTextField<String>("address.zipCode"));
			add(new RequiredTextField<String>("date"));
			add(new RequiredTextField<String>("dueDate"));
			
			DropDownChoice<PaymentMethod> payMeth = new DropDownChoice<PaymentMethod>("paymentMethod",
            		Arrays.asList(PaymentMethod.values()),
            		new EnumChoiceRenderer<PaymentMethod>(this));
            add(payMeth);
            
            add(new TextArea<String>("comment"));
            
			add(new Button("submit"));
		}
		
		public void onSubmit() {
			List<ConsumationEntry> list = new ArrayList<ConsumationEntry>(selected);
			invoice.setIncludesConsumationEntries(list);
			
			invoiceDAO.store(invoice);
			invoiceDAO.commit();
			setResponsePage(MembershipDetailPage.class, new PageParameters("id=" +  member.getId()));
		}
	}

}
