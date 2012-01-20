package at.happylab.fablabtool.markup.html.repeater.data.table;

import java.util.Arrays;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.model.InvoiceState;
import at.happylab.fablabtool.model.PaymentMethod;

public class DropDownColumn<T extends Enum<T>> extends PropertyColumn<T>{

	private static final long serialVersionUID = 1L;
	
	private Class<T> forClass;
	
	public DropDownColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, Class<T> forClass) {
		super(displayModel, sortProperty, propertyExpression);
		this.forClass = forClass;
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
		item.add(new DropDownPanel(componentId, new PropertyModel<T>(rowModel, getPropertyExpression())));
	}
	
	private class DropDownPanel extends Panel {

		private static final long serialVersionUID = 1L;

		public DropDownPanel(String componentId, final IModel<T> dropDownModel) {
			super(componentId);
			Object[] enumValues = null;
			if(forClass.equals(InvoiceState.class)){
				enumValues = InvoiceState.values();
			} else if(forClass.equals(PaymentMethod.class)){
				enumValues = PaymentMethod.values();
			}
			add(new DropDownChoice("dropdown", dropDownModel, Arrays.asList(enumValues), new EnumChoiceRenderer<T>()));
		}
	}
}
