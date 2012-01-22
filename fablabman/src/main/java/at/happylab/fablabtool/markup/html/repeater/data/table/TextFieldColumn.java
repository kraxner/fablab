package at.happylab.fablabtool.markup.html.repeater.data.table;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


//public class TextFieldColumn<T,F> extends FilteredPropertyColumn<T>{
public class TextFieldColumn<T> extends PropertyColumn<T>{

	private static final long serialVersionUID = 1L;
	
	public TextFieldColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
		super(displayModel, sortProperty, propertyExpression);
	}

//	public Component getFilter(String componentId, FilterForm<?> form){
//		return new TextFilter<F>(componentId, getFilterModel(form), form);
//	}
//	
//	protected IModel<F> getFilterModel(FilterForm<?> form){
//		return new PropertyModel<F>(form.getDefaultModel(), getPropertyExpression());
//	}
	
	@Override
	public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
		item.add(new TextFieldPanel(componentId, new PropertyModel<T>(rowModel, getPropertyExpression())));
	}
	
	private class TextFieldPanel extends Panel {

		private static final long serialVersionUID = 6748516901995412375L;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public TextFieldPanel(String componentId, final IModel<T> textFieldModel) {
			super(componentId);
			add(new TextField("textfield", textFieldModel));
		}
	}
}