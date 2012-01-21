package at.happylab.fablabtool.markup.html.repeater.data.table;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import at.happylab.fablabtool.converter.EnumConverter;

public class EnumPropertyColumn<T extends Enum<T>> extends PropertyColumn<T> {
	private static final long serialVersionUID = 1L;

	private IConverter converter; 
	
	public EnumPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, Class<T> forClass, Component resourceSource) {
		super(displayModel, sortProperty, propertyExpression);
		converter = new EnumConverter<T>(forClass, resourceSource);
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> item,
			String componentId, IModel<T> rowModel) {
		
		final IConverter myConverter = converter;
		
		item.add(new Label(componentId, new PropertyModel<T>(rowModel, getPropertyExpression())){
			private static final long serialVersionUID = 1L;

			@Override
			public IConverter getConverter(Class<?> type) {
				return myConverter;
			}
		});
	}
}
