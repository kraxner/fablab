package at.happylab.fablabtool.web.maintenance;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.WebUserManagement;
import at.happylab.fablabtool.dataprovider.WebUserProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.WebUser;
import at.happylab.fablabtool.session.FablabAuthenticatedWebSession;
import at.happylab.fablabtool.session.SessionScopeProducer;
import at.happylab.fablabtool.web.BasePage;
import at.happylab.fablabtool.web.util.ConfirmDeletePage;
import at.happylab.fablabtool.web.util.WarningPage;

public class WebUserListPage extends BasePage {

	@Inject
	WebUserProvider webUserProvider;
	@Inject
	WebUserManagement webUserMgmt;

	public WebUserListPage() {

		List<IColumn<WebUser>> columns = new ArrayList<IColumn<WebUser>>();
		columns.add(new PropertyColumn<WebUser>(new Model<String>("ID"), "id", "id"));
		columns.add(new PropertyColumn<WebUser>(new Model<String>("Vorname"), "firstname", "firstname"));
		columns.add(new PropertyColumn<WebUser>(new Model<String>("Nachname"), "lastname", "lastname"));
		columns.add(new LinkPropertyColumn<WebUser>(new Model<String>("Bearbeiten"), new Model<String>("edit")) {
			private static final long serialVersionUID = -523422943144381848L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				WebUser webUser = (WebUser) model.getObject();
				setResponsePage(new WebUserAddPage(webUser));

			}
		});
		columns.add(new LinkPropertyColumn<WebUser>(new Model<String>("Entfernen"), new Model<String>("delete")) {
			private static final long serialVersionUID = -3524734341372805625L;

			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(Item item, String componentId, final IModel model) {
				
				WebUser delUser = (WebUser) model.getObject();
				SessionScopeProducer sessionScopeProducer = ((FablabAuthenticatedWebSession)getSession()).getSessionScopeProducer();
				WebUser currentUser = sessionScopeProducer.getLoggedInUser();
				if(currentUser.getId() != delUser.getId()){
					
					setResponsePage(new ConfirmDeletePage("Wollen sie diesen WebUser wirklich löschen?") {
						private static final long serialVersionUID = 215242593335920710L;

						@Override
						protected void onConfirm() {
							WebUser webUser = (WebUser) model.getObject();
							webUserMgmt.removeWebUser(webUser);
							
							setResponsePage(WebUserListPage.this);
						}

						@Override
						protected void onCancel() {
							setResponsePage(WebUserListPage.this);
						}

					});
					
				} else {
					setResponsePage(new WarningPage("Diese Keycard kann nicht gelöscht werden.") {
						@Override
						protected void onConfirm() {
							setResponsePage(WebUserListPage.this);
						}
					});
				}
			}
		});
		DefaultDataTable<WebUser> table = new DefaultDataTable<WebUser>("webUserTable", columns, webUserProvider, 20);
		add(table);

		add(new Label("webUserCount", webUserProvider.size() + " Datensätze"));

		add(new Link<String>("addWebUser") {
			private static final long serialVersionUID = 877465087033681295L;

			public void onClick() {
				setResponsePage(new WebUserAddPage(new WebUser()));
			}
		});
		
		Link<String> goBackButton = new Link<String>("goBack") {
			private static final long serialVersionUID = -3527050342774869192L;

			public void onClick() {
				setResponsePage(new MasterDataPage());
			}
		};
		add(goBackButton);

	}

}
