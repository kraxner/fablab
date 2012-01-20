package at.happylab.fablabtool.markup.html.repeater.data.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Component to render links in datatables
 * 
 * This is from {@linkplain https://cwiki.apache.org/WICKET/adding-links-in-a-defaultdatatable.html}
 * 
 * @author Michael Kraxner
 *
 * @param <T>
 */
public abstract class LinkPropertyColumn<T> extends PropertyColumn<T> {

	private static final long serialVersionUID = 1L;
	
	PopupSettings popupSettings;
	IModel labelModel;

	public LinkPropertyColumn(IModel displayModel, String sortProperty,
			String propertyExpression, PopupSettings popupSettings) {
		this(displayModel, sortProperty, propertyExpression);
		this.popupSettings = popupSettings;
	}

	public LinkPropertyColumn(IModel displayModel, IModel labelModel) {
		super(displayModel, null);
		this.labelModel = labelModel;
	}
	
	public LinkPropertyColumn(IModel displayModel, IModel labelModel, PopupSettings popupSettings) {
		super(displayModel, null);
		this.labelModel = labelModel;
		this.popupSettings = popupSettings;
	}

	public LinkPropertyColumn(IModel displayModel, String sortProperty,
			String propertyExpression) {
		super(displayModel, sortProperty, propertyExpression);
	}

	public LinkPropertyColumn(IModel displayModel, String propertyExpressions) {
		super(displayModel, propertyExpressions);
	}

	@Override
	public void populateItem(Item item, String componentId, IModel model) {
		item.add(new LinkPanel(item, componentId, model));
	}

	/** 
	* Override this method to react to link clicks.
	* Your own/internal row id will most likely be inside the model.
	*/
	public abstract void onClick(Item item, String componentId, IModel model);


	public class LinkPanel extends Panel {

		public LinkPanel(final Item item, final String componentId,
				final IModel model) {
			super(componentId);

			Link link = new Link("link") {

				@Override
				public void onClick() {
					LinkPropertyColumn.this.onClick(item, componentId, model);
				}
			};
			link.setPopupSettings(popupSettings);

			add(link);

			IModel tmpLabelModel = labelModel;


			if (labelModel == null) {
				tmpLabelModel = createLabelModel(model);
			}

			link.add(new Label("label", tmpLabelModel));
		}
	}

}
