package at.happylab.fablabtool.web;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.StyleSheetReference;


public class BasePage extends WebPage implements Serializable {
	protected MainMenuPanel navigation;
	
    public BasePage() {
        add(new StyleSheetReference("stylesheetMain", BasePage.class, "/css/main.css"));
        add(new StyleSheetReference("stylesheetForm", BasePage.class, "/css/form.css"));
        navigation = new MainMenuPanel("mainMenuPanel"); 
        add(navigation);
    }

}
