package at.happylab.fablabtool.web.util;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class CheckBoxColumn<T> extends AbstractColumn<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5783474254434475259L;

	public CheckBoxColumn(IModel<String> displayModel) {
		super(displayModel);
	}
	
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(new CheckPanel(componentId,
		newCheckBoxModel(rowModel)));
	}
	
	protected CheckBox newCheckBox(String id, IModel<Boolean> checkModel) {
		return new CheckBox("check", checkModel);
	}
	
	protected abstract IModel<Boolean> newCheckBoxModel(IModel<T> rowModel);
	
	private class CheckPanel extends Panel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8741580224214515396L;

		public CheckPanel(String id, IModel<Boolean> checkModel) {
			super(id);
			add(newCheckBox("check", checkModel));
		}
	}
}