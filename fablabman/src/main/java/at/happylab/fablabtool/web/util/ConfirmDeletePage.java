package at.happylab.fablabtool.web.util;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import at.happylab.fablabtool.BasePage;

/**
 * Component to render a new panel with a Message and two links
 * 
 * This is from {@linkplain https://cwiki.apache.org/WICKET/getting-user-confirmation.html}
 * 
 * @author Johannes Bauer
 * 
 */
public abstract class ConfirmDeletePage extends BasePage {

	private static final long serialVersionUID = -3307603540269399632L;

	public ConfirmDeletePage(String message) {
		super();

		add(new Label("message", message));
		add(new Link<String>("confirm") {
			private static final long serialVersionUID = 7722029462498852974L;

			@Override
			public void onClick() {
				onConfirm();
			}
		});
		add(new Link<String>("cancel") {
			private static final long serialVersionUID = -5150566517904588395L;

			@Override
			public void onClick() {
				onCancel();
			}
		});
	}

	protected abstract void onCancel();

	protected abstract void onConfirm();

}
