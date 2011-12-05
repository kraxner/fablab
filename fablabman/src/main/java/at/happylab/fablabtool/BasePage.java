package at.happylab.fablabtool;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

public class BasePage extends WebPage implements Serializable {
	TopNavPanel navigation;
	
    public BasePage() {
        add(new StyleSheetReference("stylesheetMain", BasePage.class, "/css/main.css"));
        add(new StyleSheetReference("stylesheetForm", BasePage.class, "/css/form.css"));
        
        navigation = new TopNavPanel("topNavPanel"); 
        add(navigation);
    }

}
