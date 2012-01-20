package at.happylab.fablabtool.web.util;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import at.happylab.fablabtool.web.BasePage;

/**
 * Component to render a new panel with a Message and a link
 * 
 * This is from {@linkplain https://cwiki.apache.org/WICKET/getting-user-confirmation.html}
 * 
 * @author Johannes Bauer
 * 
 */
public abstract class WarningPage extends BasePage {

	private static final long serialVersionUID = -3307603540269399632L;

	public WarningPage(String message) {
		super();

		add(new Label("message", message));
		add(new Link<String>("confirm") {
			private static final long serialVersionUID = 7722029462498852974L;

			@Override
			public void onClick() {
				onConfirm();
			}
		});
	}

	protected abstract void onConfirm();

}
