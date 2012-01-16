package at.happylab.fablabtool.markup.html.tabs;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class AjaxTabbedPanelWithContext extends AjaxTabbedPanel {
	private static final long serialVersionUID = 1L;

	public AjaxTabbedPanelWithContext(String id, List<ITab> tabs, IModel<String> contextInfoModel) {
		super(id, tabs);
		((MarkupContainer)get("tabs-container")).
		add(new Label("contextinfo", contextInfoModel));
	}

}
