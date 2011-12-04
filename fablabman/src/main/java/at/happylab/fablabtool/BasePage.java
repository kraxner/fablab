package at.happylab.fablabtool;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

public class BasePage extends WebPage {

    public BasePage() {
        add(new StyleSheetReference("stylesheetMain", BasePage.class, "css/main.css"));
        add(new StyleSheetReference("stylesheetForm", BasePage.class, "css/form.css"));
        add(new TopNavPanel("topNavPanel"));
    }

}
